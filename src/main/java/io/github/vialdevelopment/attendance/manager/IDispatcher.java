package io.github.vialdevelopment.attendance.manager;

/**
 * Dispatcher interface
 */
public interface IDispatcher {

    /**
     * Dispatches the event
     * @param event event
     */
    void dispatch(Object event);

}
