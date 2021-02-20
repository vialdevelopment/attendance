package io.github.vialdevelopment.attendance.manager.impl.async;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IEventManager;
import io.github.vialdevelopment.attendance.manager.impl.ParentEventManager;

import java.util.List;
import java.util.Map;

/**
 * @author cats
 * @since August 22, 2020
 *
 * This is similar to the previous IEventManager, but it should, ideally, run faster
 */
public class AsyncParentEventManager implements IEventManager<Object> {

    private final IEventManager<Object> parentEventManager = new ParentEventManager();

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public synchronized void dispatch(Object event) {
        parentEventManager.dispatch(event);
    }

    /**
     * Registers all of the {@link Attender}s from an object class and adds them to the list
     *
     * @param object the object to check
     */
    public synchronized void registerAttender(Object object) {
        parentEventManager.registerAttender(object);
    }

    /**
     * Removes the {@link Attender}s from the list if they are in the given object
     *
     * @param object the object to remove from
     */
    public synchronized void unregisterAttender(Object object) {
        parentEventManager.unregisterAttender(object);
    }

    /**
     * @param object an object containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    public synchronized void setAttending(Object object, boolean state) {
        parentEventManager.setAttending(object, state);
    }

    /**
     * Attenders getter
     * @return attenders
     */
    public synchronized Map<Class<?>, List<Attender>> getAttenderMap() {
        return parentEventManager.getAttenderMap();
    }

    /**
     * Build the dispatcher
     */
    @Override
    public synchronized void build() {
        parentEventManager.build();
    }

}
