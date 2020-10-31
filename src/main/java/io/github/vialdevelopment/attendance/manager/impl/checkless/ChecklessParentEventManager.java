package io.github.vialdevelopment.attendance.manager.impl.checkless;

import io.github.vialdevelopment.attendance.attender.Attender;
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
     * This is the same as the previous one
     * However the {@link ParentEventManager#isAttended(Object)} is removed
     *
     * @param event an event to dispatch to ALL the attending {@link Attender}s
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void dispatch(Object event) {
        // Throws a NPE if you don't have any attenders of that type
        final List<Attender> attenders = this.getAttenderMap().get(event.getClass());
        if (attenders == null) return;

        final int size = attenders.size();

        if (size == 0) return;

        for (int i = 0; i < size; i++) {
            final Attender attender = attenders.get(i);
            attender.dispatch(event);
        }
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
