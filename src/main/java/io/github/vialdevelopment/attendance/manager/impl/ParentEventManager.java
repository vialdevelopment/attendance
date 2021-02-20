package io.github.vialdevelopment.attendance.manager.impl;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import io.github.vialdevelopment.attendance.manager.IEventManager;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author cats
 * @since August 22, 2020
 *
 * This is similar to the previous IEventManager, but it should, ideally, run faster
 */
@SuppressWarnings("rawtypes")
public class ParentEventManager implements IEventManager<Object> {

    // map
    private final Map<Class<?>, List<Attender>> attenderMap = new HashMap<>();

    /**
     * The dispatcher
     */
    @SuppressWarnings("unchecked")
    private final IDispatcher dispatcher = event -> {
        // Throws a NPE if you don't have any attenders of that type
        final List<Attender> attenders = getAttenderMap().get(event.getClass());
        if (attenders == null) return;

        int size = attenders.size();

        if (size == 0) return;

        for (final Attender attender : attenders) {
            attender.dispatch(event);
        }
    };

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public synchronized void dispatch(Object event) {
        dispatcher.dispatch(event);
    }

    /**
     * Registers all of the {@link Attender}s from an object class and adds them to the list
     *
     * @param object the object to check
     */
    public synchronized void registerAttender(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {

            if (field.getType() == Attender.class) {
                // I recommend having something to ensure you don't unintentionally register the same Attender twice
                // but I don't really think that needs to be done on this side, just check it in your project
                Attender attender = null;

                // Thanks to Tigermouthbear, I used his as reference when writing this
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    attender = (Attender) field.get(object);
                    field.setAccessible(accessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(attender != null) {
                    // Sets the needed information
                    attender.setParent(object);

                    if (!this.getAttenderMap().containsKey(attender.getConsumerClass())) {
                        this.getAttenderMap().put(attender.getConsumerClass(), Collections.synchronizedList(new ArrayList<>()));
                    }

                    final List<Attender> attenders = this.getAttenderMap().get(attender.getConsumerClass());

                    attenders.add(attender);
                    attenders.sort(Comparator.comparing(Attender::getSortingPriority));

                }
            }
        }
    }

    /**
     * Removes the {@link Attender}s from the list if they are in the given object
     *
     * @param object the object to remove from
     */
    public synchronized void unregisterAttender(Object object) {
        for (Map.Entry<Class<?>, List<Attender>> classListEntry : getAttenderMap().entrySet()) {
            for (Attender attender : classListEntry.getValue()) {
                if (attender.getParent().equals(object)) attender.setAttending(false);
            }
        }
    }

    /**
     * @param object an object containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    public synchronized void setAttending(Object object, boolean state) {
        // this could throw a NPE if you haven't properly registered it before setting the state
        for (Map.Entry<Class<?>, List<Attender>> classListEntry : getAttenderMap().entrySet()) {
            for (Attender attender : classListEntry.getValue()) {
                if (attender.getParent().equals(object)) attender.setAttending(state);
            }
        }
    }

    // Getter for attenders
    public Map<Class<?>, List<Attender>> getAttenderMap() {
        return this.attenderMap;
    }

}
