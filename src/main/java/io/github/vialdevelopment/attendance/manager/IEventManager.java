package io.github.vialdevelopment.attendance.manager;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.impl.*;

import java.util.*;

/**
 * @author cats
 * @since August 22, 2020
 *
 * @param <T> this makes it easier for me to set types to use in this Attender
 *
 * This will show warnings if you use it as a declaration without defining {@link T}, just define it as an instance of that class
 *
 * An interface to standardize separate managers, like {@link ParentEventManager} or {@link FieldEventManager}
 * also makes it easier if I want to add other types in the future
 */
@SuppressWarnings("rawtypes")
public interface IEventManager<T> {

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * This can be overridden but it does what it needs to at the current moment
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
     void dispatch(Object event);

    /**
     * Registers all of the {@link Attender}s from an object class and adds them to the list
     *
     * @param generic the {@link T} to check
     */
    void registerAttender(T generic);


    /**
     * Removes the {@link Attender}s from the list if they are in the given object
     *
     * @param generic the {@link T} to remove from
     */
    void unregisterAttender(T generic);

    /**
     * @param generic an {@link T} containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    void setAttending(T generic, boolean state);

    /**
     * a map of the consumer classes and the {@link Attender}s that are attending to them
     */
    Map<Class<?>, List<Attender>> getAttenderMap();
}
