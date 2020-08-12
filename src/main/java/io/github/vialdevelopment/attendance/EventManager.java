package io.github.vialdevelopment.attendance;

import io.github.vialdevelopment.attendance.attender.Attend;
import io.github.vialdevelopment.attendance.attender.Attender;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author cats
 * @since June 21, 2020
 *
 * This is the manager, create an instance of this in your project
 *
 * edited on August 2nd, 2020
 * I switched over to using hashmaps to organize attenders based on their consumer classes
 */
public class EventManager {

    /**
     * a map of the consumer classes and the {@link Attender}s that are attending to them
     */
    private final Map<Class, List<Attender>> attenderMap = new HashMap<>();

    /**
     * a map of the parent classes and the {@link Attender}s
     * this is a bit odd, but it is used to
     */
    private final Map<Object, List<Attender>> parentMap = new HashMap<>();

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    // Won't happen
    @SuppressWarnings("unchecked")
    public void dispatch(Object event) {
        // Throws a NPE if you don't have any attenders of that type
        final List<Attender> attenders = this.getAttenderMap().get(event.getClass());
        if (attenders == null) return;

        int size = attenders.size();
        for (int i = 0; i < size; i++) {
            final Attender attender = attenders.get(i);
            if (attender.isAttending()) {
                attender.dispatch(event);
            }
        }
    }

    /**
     * Registers all of the {@link Attender}s from an object class and adds them to the list
     *
     * @param object the object to check
     */
    public void registerAttender(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {

            Attend annotation = field.getAnnotation(Attend.class);

            if (annotation != null) {
                // I recommend having something to ensure you don't unintentionally register the same Attender twice
                // but I don't really think that needs to be done on this side, just check it in your project
                Attender listener = null;

                // Thanks to Tigermouthbear, I used his as reference when writing this
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    listener = (Attender) field.get(object);
                    field.setAccessible(accessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(listener != null) {
                    // Sets the needed information
                    listener.setPriority(annotation.priority());
                    listener.setParent(object);

                    if (!this.getAttenderMap().containsKey(listener.getConsumerClass())) {
                        this.getAttenderMap().put(listener.getConsumerClass(), Collections.synchronizedList(new ArrayList<>()));
                    }

                    if (!this.getParentMap().containsKey(object)) {
                        this.getParentMap().put(object, Collections.synchronizedList(new ArrayList<>()));
                    }

                    this.getParentMap().get(object).add(listener);

                    final List<Attender> attenders = this.getAttenderMap().get(listener.getConsumerClass());

                    attenders.add(listener);
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
    public void unregisterAttender(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Attend.class) != null) {
                Attender listener = null;

                // Thanks to Tigermouthbear, I used his as reference when writing this
                try {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    listener = (Attender) field.get(object);
                    field.setAccessible(accessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(listener != null) {
                    // this might thrown a NPE if you try to unregister an Attender that has not been registered
                    this.getAttenderMap().get(listener.getConsumerClass()).remove(listener);
                    this.getParentMap().get(object).remove(listener);
                }
            }
        }
    }

    /**
     * @param object an object to check
     * @return if that object's {@link Attender}s (if it has any) are attending
     */
    public boolean isAttended(Object object) {
        // this could in concept throw a NPE, but if it does, how

        return this.getParentMap().get(object).get(0).isAttending();
    }

    /**
     * @param object an object containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    public void setAttending(Object object, boolean state) {
        // this could throw a NPE if you haven't properly registered it before setting the state
        final List<Attender> attenders = this.getParentMap().get(object);

        int size = attenders.size();
        for (int i = 0; i < size; i++) {
            final Attender attender = attenders.get(i);
            attender.setAttending(state);
        }
    }

    // Getter for attenders
    public Map<Class, List<Attender>> getAttenderMap() {
        return this.attenderMap;
    }

    public Map<Object, List<Attender>> getParentMap() {
        return this.parentMap;
    }
}
