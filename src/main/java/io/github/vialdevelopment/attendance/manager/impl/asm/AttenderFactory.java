package io.github.vialdevelopment.attendance.manager.impl.asm;

import io.github.vialdevelopment.attendance.attender.Attender;
import org.objectweb.asm.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * This is a factory used for creating {@link Attender}s safely
 *
 * it inserts a reference to the requested method in the {@link Attender#invoke(Object)} method
 * @author cats
 * @author nirvana (because it's basically a modified {@link DispatcherFactory})
 */
public class AttenderFactory {

    /**
     * Counter for generated factories to avoid duplicates
     */
    private static int generatedCounter = 0;

    private static final String ATTENDER = "io/github/vialdevelopment/attendance/attender/Attender";

    /**
     * Generate the {@link Attender}
     *
     * @param parent the desired parent object of the {@link Attender}
     * @param method the desired method of the {@link Attender}
     * @param priority the desired priority of the {@link Attender}
     *
     * @return the {@link Attender} instance
     */
    public static Attender generate(Object parent, Method method, long priority) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        generatedCounter++;

        String generatedName = "io/github/vialdevelopment/attendance/manager/impl/asm/" + method.getName() + "Attender" + generatedCounter;
        MethodVisitor mv;
        final ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_1, ACC_PUBLIC, generatedName, null, ATTENDER, new String[]{});

        // Create a super init method
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;Ljava/lang/reflect/Method;J)V", null, null);
            mv.visitCode();

            mv.visitVarInsn(ALOAD, 0);

            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(LLOAD, 3);
            //mv.visitTypeInsn(CHECKCAST, "long");
            mv.visitMethodInsn(INVOKESPECIAL, ATTENDER, "<init>", "(Ljava/lang/Object;Ljava/lang/reflect/Method;J)V", false);
            mv.visitInsn(RETURN);

            mv.visitMaxs(8, 5);
            mv.visitEnd();
        }

        // Overwrite the indicator method
        {
            mv = cw.visitMethod(ACC_PROTECTED, "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
            mv.visitCode();

            final String parentName = method.getDeclaringClass().getName().replace('.', '/');

            final String parentMethodParameters = method.getParameterTypes()[0].getName().replace('.', '/');

            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, parentName);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitTypeInsn(CHECKCAST, parentMethodParameters);
            mv.visitMethodInsn(INVOKEVIRTUAL, parentName, method.getName(), "(L" + parentMethodParameters + ";)V", false);
            mv.visitInsn(RETURN);

            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }

        // load and init the new dispatcher
        return (Attender) AttendanceClassLoader.INSTANCE.defineClass(generatedName.replace('/', '.'), cw.toByteArray()).getConstructor(Object.class, Method.class, long.class).newInstance(parent, method, priority);
    }
}
