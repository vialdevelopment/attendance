# Attendance
Attendance is a small and (hopefully) decently quick event handler that supports priority and other fun things

It's made to be as small and simple as I can get it.

This was made for fun, and although I probably will use it in a lot of my projects, I cannot recommend using it in any projects. It's not exactly made for commercial use and likely has a lot of issues.

## Usage
```java
import io.github.vialdevelopment.attendance.attender.Attend;
import io.github.vialdevelopment.attendance.manager.IEventBus;
import io.github.vialdevelopment.attendance.manager.impl.EventBus;

public class Main {

    private static final IEventBus bus = new EventBus();

    public static void main(String[] args) {
        // registers the attenders and adds em to the list
        final Main main = new Main();
        bus.register(main);
        // build the ASM handler
        bus.build();
        // Sets them as attending
        bus.setAttending(main, true);
        // Dispatches the event
        bus.dispatch(new Event("hello"));
    }

    // When this is ran, these should print with prefixes 3, 2, 1, then 0 in that order because of the priority
    @Attend
    public void onEvent0(Event event) {
        System.out.println("0 " + event.text);
    }

    @Attend(1)
    public void onEvent1(Event event) {
        System.out.println("1 " + event.text);
    }

    @Attend(2)
    public void onEvent2(Event event) {
        System.out.println("2 " + event.text);
    }

    @Attend(3)
    public void onEvent3(Event event) {
        System.out.println("3 " + event.text);
    }


    public static class Event {
        public final String text;

        public Event(String text) {
            this.text = text;
        }
    }
}
```
