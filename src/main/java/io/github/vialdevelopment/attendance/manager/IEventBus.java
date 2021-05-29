package io.github.vialdevelopment.attendance.manager;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.impl.async.AsyncEventBus;

import java.util.*;

/**
 * @author cats
 * @since August 22, 2020
 *
 *
 * An interface to standardize separate managers, like {@link AsyncEventBus} or {@link io.github.vialdevelopment.attendance.manager.impl.EventBus}
 * also makes it easier if I want to add other types in the future
 */
public interface IEventBus {

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * This can be overridden but it does what it needs to at the current moment
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
     void dispatch(Object event);

    /**
     * Registers the {@link Attender}
     *
     * @param attender the {@link Attender} to check
     */
    void registerAttender(Attender attender);

    /**
     * Creates and {@link IEventBus#registerAttender(Attender)}s any found {@link Attender}s
     * @param object the object to check
     */
    default void register(Object object) {}

    /**
     * Removes the {@link Attender} from the list
     *
     * @param attender the {@link Attender} to remove
     */
    void unregisterAttender(Attender attender);

    /**
     * any {@link Attender}s found are {@link IEventBus#unregisterAttender(Attender)}
     *
     * @param object the object to check
     */
    default void unregister(Object object) {}

    /**
     * @param generic an {@link Attender}
     * @param state the state that the {@link Attender} should be set to
     */
    void setAttending(Attender generic, boolean state);

    default void setAttending(Object object, boolean state) {}

    /**
     * Builds the dispatcher, only for ASM impl
     */
    default void build() {}

    /**
     * a map of the consumer classes and the {@link Attender}s that are attending to them
     */
    Map<Class<?>, List<Attender>> getAttenderMap();

    /**
     * @return the classloader used to generate this project
     */
    ClassLoader getClassLoader();
}
