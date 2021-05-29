package io.github.vialdevelopment.attendance.attender;

import java.lang.reflect.Method;

/**
 * @author cats
 * @since June 21, 2020
 *
 * The attender class, stores the values and consumers and other fun things
 * hooray
 */
@SuppressWarnings("rawtypes")
public abstract class Attender {

    /**
     * if anything dispatched should be attended to
     */
    private boolean attending;

    /**
     * the parent object that this was declared in
     */
    private final Object parent;

    /**
     * the priority of this, so we can organize them
     */
    private final long priority;

    /**
     * the method that is ran by this Attender
     */
    private final Method method;

    private final Class eventClass;

    // They won't happen
    public Attender(Object parent, Method method, long priority) {
        this.eventClass = method.getParameterTypes()[0];
        this.method = method;
        this.priority = priority;
        this.parent = parent;
    }

    /**
     * @param event runs the event through the consumer if we're attending
     */
    public void dispatch(Object event) {
        if (attending) this.invoke(this.parent, event);
    }

    /**
     * @return the priority to be used for sorting
     */
    public long getSortingPriority() {
        // return negative priority so we don't have to reverse the list
        return -this.getPriority();
    }

    // getters and setters

    public boolean isAttending() {
        return this.attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

    public Method getMethod() {
        return this.method;
    }

    public Class getEventClass() {
        return this.eventClass;
    }

    public long getPriority() {
        return this.priority;
    }

    public Object getParent() {
        return this.parent;
    }

    /**
     * This invoke is filled in on runtime
     * It invokes the {@link }
     */
    protected void invoke(Object parent, Object event) {}
}
