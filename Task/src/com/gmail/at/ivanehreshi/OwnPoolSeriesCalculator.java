package com.gmail.at.ivanehreshi;

import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class OwnPoolSeriesCalculator {
	
	private int nThreads;
	private int N;
	private Thread[] threads;
	private SeriesSeqCalculator subcalcs[];

	public OwnPoolSeriesCalculator(int N, int nThreads){
		this.N = N;
		this.nThreads = nThreads;
		if(nThreads > N){
			this.nThreads = N;
		}
		
		threads = new Thread[this.nThreads];
		subcalcs = new SeriesSeqCalculator[this.nThreads];
	}

	public BigInteger compute(){
		int len = (int) Math.ceil((double)N / nThreads);
		for(int i = 1; i < nThreads; i++){
			int low = (i - 1)*len + 1;
			int high = i*len;
			subcalcs[i-1] = new SeriesSeqCalculator(low, high);
		}
		
		subcalcs[nThreads-1] = new SeriesSeqCalculator((nThreads - 1)*len + 1, N);
		
		for(int i = 0; i < nThreads; i++){
			threads[i] = new Thread(subcalcs[i]);
			threads[i].start();
		}
		
		for(int i = 0; i < nThreads; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		BigInteger result = new BigInteger("0");
		for(int i = 0; i < nThreads; i++)
			result = result.add(subcalcs[i].resultCapturer);
		
		return result;
	}
}
