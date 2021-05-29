package io.github.vialdevelopment.attendance.manager.impl.asm;

import io.github.vialdevelopment.attendance.attender.Attender;

/**
 * A class loader to create the dispatcher classes
 *
 * @author nirvana
 */
public class AttendanceClassLoader extends ClassLoader {

    // I assume this is fine :sunglasses:
    public static final AttendanceClassLoader INSTANCE = new AttendanceClassLoader(Attender.class.getClassLoader());

    public AttendanceClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    /**
     * Define the class
     * @param name class name
     * @param bytes class bytes
     * @return Class
     */
    public Class<?> defineClass(String name, byte[] bytes) {
        return  defineClass(name, bytes, 0, bytes.length);
    }

}
