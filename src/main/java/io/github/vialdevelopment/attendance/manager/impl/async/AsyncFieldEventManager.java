package io.github.vialdevelopment.attendance.manager.impl.async;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IEventManager;
import io.github.vialdevelopment.attendance.manager.impl.FieldEventManager;

import java.util.List;
import java.util.Map;

/**
 * @author cats
 * @since August 22, 2020
 *
 * Implementation of {@link IEventManager}, this implementation lets you input singular field
 */
public class AsyncFieldEventManager implements IEventManager<Attender> {

    private final IEventManager<Attender> fieldEventManager = new FieldEventManager();

    /**
     * Dispatches an event to all events in the map, I overrode the default because
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public synchronized void dispatch(Object event) {
        fieldEventManager.dispatch(event);
    }

    /**
     * Registers an {@link Attender} to the map and list
     *
     * @param attender the {@link Attender} to register
     */
    @Override
    public synchronized void registerAttender(Attender attender) {
        fieldEventManager.registerAttender(attender);
    }

    /**
     * Removes the {@link Attender} from the list in the map
     *
     * @param attender the {@link Attender} to remove from
     */
    @Override
    public synchronized void unregisterAttender(Attender attender) {
        fieldEventManager.unregisterAttender(attender);
    }

    /**
     * @deprecated just interact with the {@link Attender} directly
     *
     * @param attender an {@link Attender}
     * @param state the state that all of the {@link Attender}
     */
    @Deprecated
    @Override
    public synchronized void setAttending(Attender attender, boolean state) {
        fieldEventManager.setAttending(attender, state);
    }

    /**
     * Get attenders map
     * @return attenders
     */
    @Override
    public synchronized Map<Class<?>, List<Attender>> getAttenderMap() {
        return fieldEventManager.getAttenderMap();
    }

    /**
     * Builds the dispatcher
     */
    @Override
    public synchronized void build() {
        fieldEventManager.build();
    }

}
