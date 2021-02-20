package io.github.vialdevelopment.attendance.manager.impl.asm;

import io.github.vialdevelopment.attendance.attender.Attender;
import io.github.vialdevelopment.attendance.manager.IDispatcher;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.objectweb.asm.Opcodes.*;

public class DispatcherFactory {
    
    private static int generatedCounter = 0;

    private static final DispatcherClassLoader classLoader = new DispatcherClassLoader(DispatcherFactory.class.getClassLoader());
    
    public static IDispatcher generate(List<Attender> attenders) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        int[] classesHashCodes;
        {
            Set<Integer> classesHashCodesSet = new TreeSet<>();
            for (Attender attender : attenders) {
                classesHashCodesSet.add(attender.getConsumerClass().hashCode());
            }
            classesHashCodes = new int[classesHashCodesSet.size()];
            int i = 0;
            for (Integer integer : classesHashCodesSet) {
                classesHashCodes[i] = integer;
                i++;
            }
        }
        Label[] labels;
        {
            labels = new Label[classesHashCodes.length];
            for (int i = 0; i < labels.length; i++) {
                labels[i] = new Label();
            }
        }

        generatedCounter++;
        String generatedName = "io/github/vialdevelopment/attendance/manager/impl/asm/DispatcherInstance"+generatedCounter;
        MethodVisitor mv;
        FieldVisitor fv;
        ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_1, ACC_PUBLIC, generatedName, null, "java/lang/Object", new String[]{"io/github/vialdevelopment/attendance/manager/IDispatcher"});
        {
            for (int i = 0; i < attenders.size(); i++) {
                fv = cw.visitField(ACC_PRIVATE, "Attender" + i, "Lio/github/vialdevelopment/attendance/attender/Attender;", null, null);
                fv.visitEnd();
            }
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>",  "(Ljava/util/List;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            {
                // need to setup our fields
                for (int i = 0; i < attenders.size(); i++) {
                    // get nth element from the list,
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(i);
                    mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
                    mv.visitTypeInsn(CHECKCAST, "io/github/vialdevelopment/attendance/attender/Attender");
                    mv.visitFieldInsn(PUTFIELD, generatedName, "Attender" + i, "Lio/github/vialdevelopment/attendance/attender/Attender;");
                }
            }
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "dispatch", "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            // get the hash code of the class
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "hashCode", "()I", false);
            Label defaultLabel = new Label();
            mv.visitLookupSwitchInsn(defaultLabel, classesHashCodes, labels);
            for (int i = 0; i < labels.length; i++) {
                mv.visitLabel(labels[i]);
                for (int i1 = 0; i1 < attenders.size(); i1++) {
                    if (attenders.get(i1).getConsumerClass().hashCode() == classesHashCodes[i]) {
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, generatedName, "Attender" + i1, "Lio/github/vialdevelopment/attendance/attender/Attender;");
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "io/github/vialdevelopment/attendance/attender/Attender", "dispatch", "(Ljava/lang/Object;)V", false);
                    }
                }
                mv.visitInsn(RETURN);
            }
            mv.visitLabel(defaultLabel);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        return (IDispatcher) classLoader.defineClass(generatedName.replace('/', '.'), cw.toByteArray()).getConstructor(List.class).newInstance(attenders);
    }

}
