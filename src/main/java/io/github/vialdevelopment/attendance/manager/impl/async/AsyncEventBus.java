package io.github.vialdevelopment.attendance.manager.impl.async;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IEventBus;
import io.github.vialdevelopment.attendance.manager.impl.EventBus;

import java.util.List;
import java.util.Map;

/**
 * @author cats
 * @since August 22, 2020
 *
 * This is similar to the previous IEventManager, but it should, ideally, run faster
 */
public class AsyncEventBus implements IEventBus {

    private final IEventBus eventBus = new EventBus();

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public synchronized void dispatch(Object event) {
        eventBus.dispatch(event);
    }

    /**
     * Registers an individual {@link Attender}
     *
     * @param attender the {@link Attender} to check
     */
    @Override
    public void registerAttender(Attender attender) {
        eventBus.registerAttender(attender);
    }

    /**
     * Registers all of the {@link Attender}s from an object class and adds them to the list
     *
     * @param object the object to check
     */
    @Override
    public synchronized void register(Object object) {
        eventBus.register(object);
    }

    /**
     * Unregisters an {@link Attender} directly
     *
     * @param attender the {@link Attender} to remove
     */
    @Override
    public void unregisterAttender(Attender attender) {
        eventBus.unregisterAttender(attender);
    }

    /**
     * Removes the {@link Attender}s from the list if they are in the given object
     *
     * @param object the object to remove from
     */
    public synchronized void unregister(Object object) {
        eventBus.unregister(object);
    }

    /**
     * @param object an object containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    @Override
    public synchronized void setAttending(Object object, boolean state) {
        eventBus.setAttending(object, state);
    }

    // mmmm
    @Override
    public synchronized void setAttending(Attender attender, boolean state) {
        eventBus.setAttending(attender, state);
    }

    /**
     * Attenders getter
     * @return attenders
     */
    public synchronized Map<Class<?>, List<Attender>> getAttenderMap() {
        return eventBus.getAttenderMap();
    }

    /**
     * Build the dispatcher
     */
    @Override
    public synchronized void build() {
        eventBus.build();
    }

}
