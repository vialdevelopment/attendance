package io.github.vialdevelopment.attendance.manager.impl;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import io.github.vialdevelopment.attendance.manager.IEventManager;

import java.util.*;

/**
 * @author cats
 * @since August 22, 2020
 *
 * implementation of {@link IEventManager}, this implementation lets you input singular field
 */
@SuppressWarnings("rawtypes")
public class FieldEventManager implements IEventManager<Attender> {

    // the map
    private final Map<Class<?>, List<Attender>> attenderMap = new HashMap<>();

    /**
     * the dispatcher
     */
    @SuppressWarnings("unchecked")
    private final IDispatcher dispatcher = event -> {
        final List<Attender> attenders = getAttenderMap().get(event.getClass());
        if (attenders == null) return;

        int size = attenders.size();

        if (size == 0) return;

        for (final Attender attender : attenders) {
            if (attender.isAttending()) {
                attender.dispatch(event);
            }
        }
    };

    /**
     * Dispatches an event to all events in the map, I overrode the default because
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public synchronized void dispatch(Object event) {
        dispatcher.dispatch(event);
    }

    /**
     * Registers an {@link Attender} to the map and list
     *
     * @param attender the {@link Attender} to register
     */
    @Override
    public synchronized void registerAttender(Attender attender) {

        if (!this.getAttenderMap().containsKey(attender.getConsumerClass())) {
            this.getAttenderMap().put(attender.getConsumerClass(), Collections.synchronizedList(new ArrayList<>()));
        }

        final List<Attender> attenders = this.getAttenderMap().get(attender.getConsumerClass());

        attenders.add(attender);
        attenders.sort(Comparator.comparing(Attender::getSortingPriority));
    }

    /**
     * Removes the {@link Attender} from the list in the map
     *
     * @param attender the {@link Attender} to remove from
     */
    @Override
    public synchronized void unregisterAttender(Attender attender) {

        if(attender != null) {
            // this might thrown a NPE if you try to unregister an Attender that has not been registered
            this.getAttenderMap().get(attender.getConsumerClass()).remove(attender);
        }
    }

    /**
     * @deprecated just interact with the {@link Attender} directly
     *
     * @param attender the {@link Attender} to check
     * @return if that object is attended
     */
    @Deprecated
    @Override
    public synchronized boolean isAttended(Attender attender) {

        for (Attender checkingAttender : this.getAttenderMap().get(attender.getConsumerClass())) {
            if (checkingAttender == attender) {
                return checkingAttender.isAttending();
            }
        }

        return false;
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
        for (Attender checkingAttender : this.getAttenderMap().get(attender.getConsumerClass())) {
            if (checkingAttender == attender) {
                checkingAttender.setAttending(state);
            }
        }
    }

    @Override
    public Map<Class<?>, List<Attender>> getAttenderMap() {
        return this.attenderMap;
    }
}
