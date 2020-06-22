package io.github.vialdevelopment.attendance;

import io.github.vialdevelopment.attendance.attender.Attend;
import io.github.vialdevelopment.attendance.attender.Attender;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author cats
 * @since June 21, 2020
 *
 * This is the manager, create an instance of this in your project
 */
public class EventManager {

    /**
     * a list of the {@link Attender}s that have been registered
     */
    private List<Attender> attenders = new CopyOnWriteArrayList<Attender>();

    /**
     * This dispatches any Object as an event to any listener that takes it
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    // Won't happen
    @SuppressWarnings("unchecked")
    public void dispatch(Object event) {
        for (Attender attender : this.getAttenders()) {
            // Only dispatch if the attender is attending
            if (attender.isAttending()) {
                if (attender.getConsumerClass() == event.getClass()) {
                    attender.dispatch(event);
                }
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
                    listener.setParent(object.getClass());
                    this.getAttenders().add(listener);
                }
            }
        }
        // Sorts after adding the Attenders
        this.getAttenders().sort(Comparator.comparing(Attender::getSortingPriority));
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
                    this.getAttenders().remove(listener);
                }
            }
        }
    }

    /**
     * @param object an object to check
     * @return if that object's {@link Attender}s (if it has any) are attending
     */
    public boolean isAttended(Object object) {
        for (Attender attender : this.getAttenders()) {
            if (attender.getParent() == object.getClass()) {
                return attender.isAttending();
            }
        }
        return false;
    }

    /**
     * @param object an object containing {@link Attender}s
     * @param state the state that all of the {@link Attender}s' attending state should be set to
     */
    public void setAttending(Object object, boolean state) {
        for (Attender attender : this.getAttenders()) {
            if (attender.getParent() == object.getClass()) {
                attender.setAttending(state);
            }
        }
    }

    // Getter for attenders
    public List<Attender> getAttenders() {
        return this.attenders;
    }
}
