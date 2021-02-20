package io.github.vialdevelopment.attendance.manager.impl.checkless;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import io.github.vialdevelopment.attendance.manager.impl.ParentEventManager;

import java.util.List;

/**
 * @author cats
 * @since October 25, 2020
 *
 * This is an instance of {@link ParentEventManager}
 * Only, without the {@link ParentEventManager#isAttended(Object)} checks
 * It should technically make {@link ParentEventManager#dispatch(Object)} faster
 * Especially when there are many events of that type
 */
@SuppressWarnings("rawtypes")
public class ChecklessParentEventManager extends ParentEventManager {

    /**
     * the dispatcher
     */
    @SuppressWarnings("unchecked")
    private final IDispatcher dispatcher = event -> {
        // Throws a NPE if you don't have any attenders of that type
        final List<Attender> attenders = getAttenderMap().get(event.getClass());
        if (attenders == null) return;

        final int size = attenders.size();

        if (size == 0) return;

        for (final Attender attender : attenders) {
            attender.dispatch(event);
        }
    };

    /**
     * This is the same as the previous one
     * However the {@link ParentEventManager#isAttended(Object)} is removed
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @Override
    public synchronized void dispatch(Object event) {
        dispatcher.dispatch(event);
    }

    // Make this do nothin
    @Deprecated
    @Override
    public synchronized boolean isAttended(Object object) {
        return false;
    }

    // Make this do nothin
    @Deprecated
    @Override
    public synchronized void setAttending(Object object, boolean state) {
    }
}
