package io.github.vialdevelopment.attendance.manager.impl;

import io.github.vialdevelopment.attendance.attender.Attend;
import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import io.github.vialdevelopment.attendance.manager.IEventBus;
import io.github.vialdevelopment.attendance.manager.impl.asm.AttenderFactory;
import io.github.vialdevelopment.attendance.manager.impl.asm.DispatcherFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author cats
 * @since August 22, 2020
 *
 * This is similar to the previous IEventManager, but it should, ideally, run faster
 */
public class EventBus implements IEventBus {

    /** Map holding attenders */
    private final Map<Class<?>, List<Attender>> attenderMap = new HashMap<>();

    /** The dispatcher */
    private IDispatcher dispatcher;

    /**
     * the classloader
     */
    private final ClassLoader classLoader;

    public EventBus(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public EventBus() {
        this.classLoader = this.getClass().getClassLoader();
    }

    /**
     * This dispatches any Object as an event to any listener that takes it
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public void dispatch(Object event) {
        dispatcher.dispatch(event);
    }

    /**
     * Registers an {@link Attender} directly
     *
     * @param attender the {@link Attender} to check
     */
    public void registerAttender(Attender attender) {
        if (!this.attenderMap.containsKey(attender.getEventClass())) {
            this.attenderMap.put(attender.getEventClass(), Collections.synchronizedList(new ArrayList<>()));
        }

        final List<Attender> attenders = this.attenderMap.get(attender.getEventClass());

        attenders.add(attender);
        attenders.sort(Comparator.comparing(Attender::getSortingPriority));
    }

    /**
     * Unregisters an {@link Attender} directly
     *
     * @param attender the {@link Attender} to remove
     */
    public void unregisterAttender(Attender attender) {
        this.attenderMap.get(attender.getEventClass()).remove(attender);
    }

    /**
     * Registers all of the {@link Attender}s from an object class and adds them to the list
     *
     * @param object the object to check
     */
    public void register(Object object) {
        for (Method method : object.getClass().getMethods()) {
            final Attend annotation = method.getAnnotation(Attend.class);

            if (annotation != null) {
                try {
                    registerAttender(AttenderFactory.generate(object, method, annotation.value(), this.classLoader));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes the {@link Attender}s from the list if they are in the given object
     *
     * @param object the object to remove from
     */
    public void unregister(Object object) {
        for (Map.Entry<Class<?>, List<Attender>> classListEntry : getAttenderMap().entrySet()) {
            final List<Attender> attenders = new ArrayList<>();

            for (Attender attender : classListEntry.getValue()) {
                if (attender.getParent().equals(object)) {
                    attenders.add(attender);
                }
            }

            classListEntry.getValue().removeAll(attenders);
        }

    }

    /**
     * @param object an object containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    public void setAttending(Object object, boolean state) {
        // this could throw a NPE if you haven't properly registered it before setting the state
        for (Map.Entry<Class<?>, List<Attender>> classListEntry : getAttenderMap().entrySet()) {
            for (Attender attender : classListEntry.getValue()) {
                if (attender.getParent().equals(object)) setAttending(attender, state);
            }
        }
    }

    // This might seem stupid, but I'm trying to keep a proper interface setup
    public void setAttending(Attender attender, boolean state) {
        attender.setAttending(state);
    }

    /**
     * Attenders getter
     * @return attenders
     */
    public Map<Class<?>, List<Attender>> getAttenderMap() {
        return this.attenderMap;
    }

    /**
     * Build the dispatcher
     */
    @Override
    public void build() {
        List<Attender> allAttenders = new ArrayList<>();
        for (Map.Entry<Class<?>, List<Attender>> classListEntry : getAttenderMap().entrySet()) {
            allAttenders.addAll(classListEntry.getValue());
        }
        try {
            dispatcher = DispatcherFactory.generate(allAttenders);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            System.out.println("FAILED TO CREATE DISPATCHER! ABORT!");
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
}
