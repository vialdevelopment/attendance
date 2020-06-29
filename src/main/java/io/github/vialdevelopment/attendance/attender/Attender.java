package io.github.vialdevelopment.attendance.attender;

import java.util.function.Consumer;

/**
 * @author cats
 * @since June 21, 2020
 *
 * The attender class, stores the values and consumers and other fun things
 * hooray
 * @param <T> the Event to listen for
 */
public class Attender<T> {

    /**
     * if anything dispatched should be attended to
     */
    private boolean attending;

    /**
     * the consumer, used for doing the
     */
    private Consumer<T> consumer;

    /**
     * the class that the consumer uses
     */
    private Class<T> consumerClass;

    /**
     * the parent object that this was declared in
     */
    private Object parent;

    /**
     * the priority of this, so we can organize them
     */
    private long priority;

    // They won't happen
    public Attender(Class<T> consumerClass, Consumer<T> consumer) {
        this.consumerClass = consumerClass;
        this.consumer = consumer;
    }

    /**
     * @param event runs the event through the consumer
     */
    public void dispatch(T event) {
        this.consumer.accept(event);
    }

    /**
     * @return the priority to be used for sorting
     */
    public long getSortingPriority() {
        // return negative priority so we don't have to reverse the list
        return -this.priority;
    }

    // getters and setters

    public Consumer<T> getConsumer() {
        return this.consumer;
    }

    public void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public boolean isAttending() {
        return this.attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

    public Class<T> getConsumerClass() {
        return this.consumerClass;
    }

    public long getPriority() {
        return this.priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public Object getParent() {
        return this.parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
}
