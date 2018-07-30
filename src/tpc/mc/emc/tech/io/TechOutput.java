package tpc.mc.emc.tech.io;

import java.io.DataOutput;
import java.io.IOException;

import tpc.mc.emc.tech.ITech;

/**
 * Allowed to write tech object
 * */
public interface TechOutput extends DataOutput {
	
	/**
	 * Write a tech object
	 * */
	public default void writeTech(ITech tech) throws IOException {
		IO.write(tech, this);
	}
}
