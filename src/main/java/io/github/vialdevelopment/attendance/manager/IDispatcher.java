package io.github.vialdevelopment.attendance.manager;

/**
 * Dispatcher interface
 *
 * @author nirvana
 */
public interface IDispatcher {

    /**
     * Dispatches the event
     * @param event event
     */
    void dispatch(Object event);

}
