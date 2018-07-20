package tpc.mc.emc;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public final class EMC$Starter {
	
	public static final void premain(String str, Instrumentation inst) throws Throwable {
		System.out.println("EMC -> Starts(Version: 0.0.0)!");
		
		Class.forName("tpc.mc.emc.EMC$CodeRuler");
		inst.addTransformer(new ClassFileTransformer() {
			
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				try {
					return EMC$CodeRuler.codefix(classfileBuffer, className);
				} catch(Throwable e) {
					e.printStackTrace();
					EMC$Util.exit(0);
					
					return null;
				}
			}
		}, true);
		
		System.out.println("EMC -> Retransform!");
		
		//回滚加载过的classes
		Class[] cs = inst.getAllLoadedClasses();
		for(Class c : cs) {
			if(inst.isModifiableClass(c)) inst.retransformClasses(c);
		}
		
		System.out.println("EMC -> Listening!");
	}
}
