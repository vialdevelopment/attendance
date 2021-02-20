package io.github.vialdevelopment.attendance.manager.impl;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import io.github.vialdevelopment.attendance.manager.IEventManager;
import io.github.vialdevelopment.attendance.manager.impl.asm.DispatcherFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author cats
 * @since August 22, 2020
 *
 * Implementation of {@link IEventManager}, this implementation lets you input singular field
 */
public class FieldEventManager implements IEventManager<Attender> {

    /** Attenders map */
    private final Map<Class<?>, List<Attender>> attenderMap = new HashMap<>();

    /** The dispatcher */
    private IDispatcher dispatcher;

    /**
     * Dispatches an event to all events in the map, I overrode the default because
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public void dispatch(Object event) {
        dispatcher.dispatch(event);
    }

    /**
     * Registers an {@link Attender} to the map and list
     *
     * @param attender the {@link Attender} to register
     */
    @Override
    public void registerAttender(Attender attender) {

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
    public void unregisterAttender(Attender attender) {
        attender.setAttending(false);
    }

    /**
     * @deprecated just interact with the {@link Attender} directly
     *
     * @param attender an {@link Attender}
     * @param state the state that all of the {@link Attender}
     */
    @Deprecated
    @Override
    public void setAttending(Attender attender, boolean state) {
        for (Attender checkingAttender : this.getAttenderMap().get(attender.getConsumerClass())) {
            if (checkingAttender == attender) {
                checkingAttender.setAttending(state);
            }
        }
    }

    /**
     * Get attenders map
     * @return attenders
     */
    @Override
    public Map<Class<?>, List<Attender>> getAttenderMap() {
        return this.attenderMap;
    }

    /**
     * Builds the dispatcher
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

}
