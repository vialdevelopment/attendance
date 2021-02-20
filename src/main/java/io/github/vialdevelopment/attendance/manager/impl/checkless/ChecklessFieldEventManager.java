package io.github.vialdevelopment.attendance.manager.impl.checkless;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import io.github.vialdevelopment.attendance.manager.impl.FieldEventManager;
import io.github.vialdevelopment.attendance.manager.impl.ParentEventManager;

import java.util.List;

/**
 * @author cats
 * @since October 30, 2020
 *
 * This is an instance of {@link FieldEventManager}
 * Only, without the {@link FieldEventManager#isAttended(Attender)} checks
 * It should technically make {@link FieldEventManager#dispatch(Object)} faster
 * Especially when there are many events of that type
 */
@SuppressWarnings("rawtypes")
public class ChecklessFieldEventManager extends FieldEventManager {

    /**
     * the dispatcher
     */
    @SuppressWarnings("unchecked")
    private final IDispatcher dispatcher = event -> {
        final List<Attender> attenders = getAttenderMap().get(event.getClass());
        if (attenders == null) return;

        int size = attenders.size();

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
    public synchronized boolean isAttended(Attender attender) {
        return false;
    }

    // Make this do nothin
    @Deprecated
    @Override
    public synchronized void setAttending(Attender attender, boolean state) {
    }
}
