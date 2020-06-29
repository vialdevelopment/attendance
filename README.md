#Attendance

Attendance is a small and (hopefully) decently quick event handler that supports priority and other fun things

It's made to be as small and simple as I can get it.

This was made for fun, and although I probably will use it in a lot of my projects, I cannot recommend using it in any projects. It's not exactly made for commercial use and likely has a lot of issues.

## Usage
```java
import io.github.vialdevelopment.attendance.attender.Attend;
import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.attender.EventManager;

public class Main {

    private static EventManager manager = new EventManager();

    public static void main(String[] args) {
        // registers the attenders and adds em to the list
        final Main main = new Main();
        manager.registerAttender(main);
        
        // Sets them as attending
        manager.setAttending(main, true);
        // Dispatches the event
        manager.dispatch(new Event());
    }

    // When this is ran, these should print out 3, 2, 1, then 0 in that order because of the priority

    @Attend(priority = 3)
    public Attender<Event> event3 = new Attender<>(Event.class, event -> {
        System.out.println("3");
    });

    @Attend(priority = 1)
    public Attender<Event> event1 = new Attender<>(Event.class, event -> {
        System.out.println("1");
    });

    @Attend(priority = 2)
    public Attender<Event> event2 = new Attender<>(Event.class, event -> {
        System.out.println("2");
    });

    @Attend(priority = 0)
    public Attender<Event> event0 = new Attender<>(Event.class, event -> {
        System.out.println("0");
    });

    static class Event {
    }
}
```