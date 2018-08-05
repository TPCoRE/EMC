package tpc.mc.emc;

/**
 * A progress that can be processed step by step
 * */
@FunctionalInterface
public interface Stepable {
	
	/**
	 * Process the current step and return the next step, return null means there were nothing to process next
	 * */
	public Stepable next();
	
	/**
	 * Nothing to process
	 * */
	public static final Stepable NOP = null;
}
