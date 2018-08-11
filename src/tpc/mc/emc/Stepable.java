package tpc.mc.emc;

/**
 * A progress that can be processed step by step
 * */
@FunctionalInterface
public interface Stepable {
	
	/**
	 * Process the current step and return the next step, return {@link #NOP} means there were nothing to process next
	 * */
	public Stepable next();
	
	/**
	 * Nothing to process, the value of it is null
	 * */
	public static final Stepable NOP = null;
}
