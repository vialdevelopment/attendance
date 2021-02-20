package io.github.vialdevelopment.attendance.manager.impl.asm;

/**
 * A class loader to create the dispatcher classes
 */
public class DispatcherClassLoader extends ClassLoader {

    public DispatcherClassLoader(ClassLoader classLoader) {
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
