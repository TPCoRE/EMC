package tpc.mc.emc;

/**
 * A progress that can process step by step
 * */
public interface Stepable {
	
	/**
	 * Process the current step and return the next step, return null means there were nothing to process next
	 * */
	public Stepable next();
	
	/**
	 * A step that do nothing, and it will end up the progress
	 * */
	public static final Stepable NOP = new Stepable() {
		
		@Override
		public Stepable next() {
			return null;
		}
	};
}
