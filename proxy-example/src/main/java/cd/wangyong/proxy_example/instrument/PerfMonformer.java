package cd.wangyong.proxy_example.instrument;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

public class PerfMonformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        System.out.println("--Transforming " + className);
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if (!cl.isInterface()) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i = 0; i < methods.length; i++) {
                    if (!methods[i].isEmpty()) {
                        doMethod(methods[i]);
                    }
                }
                transformed = cl.toBytecode();
            }
        }
        catch (Exception e) {
            System.out.println("could not instrument " + className + ", exception:" + e.getMessage());
        }
        finally {
            if (cl != null)
                cl.detach();
        }
        return transformed;
    }

    private void doMethod(CtBehavior method) throws CannotCompileException {
        method.insertBefore("long st = System.nanoTime();");
        method.insertAfter("System.out.println(\"leave " + method.getName() + " and time:\" + (System.nanoTime() - st));");
    }
}
