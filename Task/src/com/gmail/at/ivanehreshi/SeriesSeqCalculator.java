package com.gmail.at.ivanehreshi;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class SeriesSeqCalculator implements Callable<BigInteger>, Runnable {

	private int high;
	private int low;
	public BigInteger resultCapturer;

	public SeriesSeqCalculator(int index1, int index2) {
		this.low = index1;
		this.high = index2;
	}
	
	private static int intEven(int exp){
		return (exp % 2 == 0)? 1: -1;
	}
	
	@Override
	public BigInteger call() throws Exception {
		BigInteger result = new BigInteger("0");
		BigInteger term = new BigInteger("2");
		BigInteger two = new BigInteger("2");
		BigInteger eight = new BigInteger("8");
		
		term = term.pow(low - intEven(low));
		result = result.add(term);
		
		for(int i = low + 1; i <= high; i++){
			if(i % 2 == 0){
				term = term.divide(two);
			}else{
				term = term.multiply(eight);
			}
			result = result.add(term);
		}

		resultCapturer = result;
		return result;

	}

	
	@Override
	public void run() {
		try {
			call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
