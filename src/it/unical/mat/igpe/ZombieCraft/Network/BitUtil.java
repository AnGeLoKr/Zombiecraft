package it.unical.mat.igpe.ZombieCraft.Network;

public class BitUtil {
	
	public static int getNeededBitsCount(int value) {		
		return 32 - Integer.numberOfLeadingZeros(value);
	}
	
}