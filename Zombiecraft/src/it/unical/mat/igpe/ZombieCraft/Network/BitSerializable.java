package it.unical.mat.igpe.ZombieCraft.Network;

import java.io.IOException;

import com.cubes.network.BitInputStream;
import com.cubes.network.BitOutputStream;

public abstract interface BitSerializable {
	
	public abstract void write(BitOutputStream bitOutputStream);

	public abstract void read(BitInputStream bitInputStream)
			throws IOException;
	
}
