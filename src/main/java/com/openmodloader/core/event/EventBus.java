package com.openmodloader.core.event;

import com.openmodloader.api.event.Event;
import com.openmodloader.api.event.EventPhase;
import com.openmodloader.loader.ModInfo;
import com.openmodloader.loader.OpenModLoader;
import net.minecraft.util.Pair;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus {
    public static EventPhase[] CANCEL_PHASES = new EventPhase[]{EventPhase.CANCELLATION, EventPhase.DEFAULT};
    public static EventPhase[] NORMAL_PHASES = new EventPhase[]{EventPhase.DEFAULT};
    public Map<Class<? extends Event>, Set<Pair<Pair<String, Object>, Method>>> subscribers = new HashMap<>();

    public void register(Object target) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Event.Subscribe.class))
                continue;
            if (method.getParameterTypes().length == 0)
                throw new RuntimeException("Attempted to use an @Event.Subscribe with no event");
            Event.Subscribe subscribe = method.getAnnotation(Event.Subscribe.class);
            Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
            Class<?> returnType = method.getReturnType();
            if (subscribe.phase() == EventPhase.CANCELLATION) {
                if (!Event.Cancellable.class.isAssignableFrom(eventClass))
                    throw new RuntimeException("Attempted to use Cancellation phase with a non cancellable event");
                if (returnType != Boolean.TYPE)
                    throw new RuntimeException("Attempted to use Cancellation phase with a non boolean return type");
            } else {
                if (Event.WithResult.class.isAssignableFrom(eventClass)) {
                    if (returnType == Void.TYPE)
                        throw new RuntimeException("Attempted to use an event with result and a void return type");
                    if (method.getParameterTypes().length < 2)
                        throw new RuntimeException("You must accept the previous result as a parameter in events with results");
                } else {
                    if (returnType != Void.TYPE)
                        throw new RuntimeException("Attempted to use an non special event and a non void return type");
                }
            }

            if (!subscribers.containsKey(eventClass))
                subscribers.put(eventClass, new HashSet<>());
            subscribers.get(eventClass).add(new Pair<>(new Pair<>(OpenModLoader.getActiveMod().getModId(), target), method));
        }
    }

    public <T extends Event> T post(@Nonnull T event) {
        EventContext context = new EventContext();
        if (event instanceof Event.WithResult)
            context.result = ((Event.WithResult) event).getDefaultResult();
        ModInfo previousMod = OpenModLoader.getActiveMod();
        for (EventPhase phase : event instanceof Event.PhaseLimit ? ((Event.PhaseLimit) event).getPossiblePhases() : event instanceof Event.Cancellable ? CANCEL_PHASES : NORMAL_PHASES) {
            context.phase = phase;
            post(event, context);
        }
        OpenModLoader.setCurrentPhase(null);
        OpenModLoader.setActiveMod(previousMod);
        return event;
    }

    public <T> T post(@Nonnull Event.WithResult<T> event) {
        EventContext context = new EventContext();
        context.result = ((Event.WithResult) event).getDefaultResult();
        ModInfo previousMod = OpenModLoader.getActiveMod();
        for (EventPhase phase : event instanceof Event.PhaseLimit ? ((Event.PhaseLimit) event).getPossiblePhases() : event instanceof Event.Cancellable ? CANCEL_PHASES : NORMAL_PHASES) {
            context.phase = phase;
            post(event, context);
        }
        OpenModLoader.setCurrentPhase(null);
        OpenModLoader.setActiveMod(previousMod);
        return (T) context.result;
    }

    public boolean post(@Nonnull Event.Cancellable event) {
        EventContext context = new EventContext();
        if (event instanceof Event.WithResult)
            context.result = ((Event.WithResult) event).getDefaultResult();
        ModInfo previousMod = OpenModLoader.getActiveMod();
        for (EventPhase phase : event instanceof Event.PhaseLimit ? ((Event.PhaseLimit) event).getPossiblePhases() : CANCEL_PHASES) {
            context.phase = phase;
            post(event, context);
        }
        OpenModLoader.setCurrentPhase(null);
        OpenModLoader.setActiveMod(previousMod);
        return context.cancelled;
    }

    private void post(@Nonnull Event event, EventContext context) {
        if (!subscribers.containsKey(event.getClass()))
            return;
        OpenModLoader.setCurrentPhase(context.phase);
        for (Pair<Pair<String, Object>, Method> pair : subscribers.get(event.getClass())) {
            Pair<String, Object> modContext = pair.getFirst();
            OpenModLoader.setActiveMod(OpenModLoader.getModInfo(modContext.getFirst()));
            Method method = pair.getSecond();
            Event.Subscribe subscribe = method.getAnnotation(Event.Subscribe.class);
            if (subscribe.phase() != context.phase)
                continue;
            boolean validMethod = true;
            if (event instanceof Event.Generic) {
                Type[] genericParameterTypes = method.getGenericParameterTypes();
                for (int i = 0; i < genericParameterTypes.length; i++) {
                    Type type = genericParameterTypes[i];
                    if (type instanceof ParameterizedType) {
                        if (!((Event.Generic) event).matchesGenericType((Class<? extends Event.Generic>) event.getClass(), i, ((ParameterizedType) type).getActualTypeArguments()[0])) {
                            validMethod = false;
                            break;
                        }
                    }
                }
            }
            if (!validMethod)
                continue;
            try {
                Object ret;
                if (context.phase != EventPhase.CANCELLATION && event instanceof Event.WithResult)
                    ret = method.invoke(modContext.getSecond(), event, context.result);
                else
                    ret = method.invoke(modContext.getSecond(), event);
                if (context.phase == EventPhase.CANCELLATION && !context.cancelled)
                    context.cancelled = (boolean) ret;
                else if (context.phase != EventPhase.CANCELLATION)
                    context.result = ret;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}