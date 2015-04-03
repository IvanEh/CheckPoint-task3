package com.gmail.at.ivanehreshi;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public class ThreadPoolSeriesCalculator {
	private final ThreadPoolExecutor executor;
	private int nThreads;
	private int N;

	
	public ThreadPoolSeriesCalculator(int n, int thrds){
		this.N = n;
		this.nThreads = thrds;
		if(nThreads > N){
			nThreads = N;
		}
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
	}
	
	public BigInteger compute(){
		LinkedList<Future<BigInteger>> results = new LinkedList<Future<BigInteger>>();
		
		int len = (int) Math.ceil((double)N / nThreads);
		for(int i = 1; i < nThreads; i++){
			int low = (i - 1)*len + 1;
			int high = i*len;
			results.add(executor.submit((Callable)new SeriesSeqCalculator(low, high)));
		}
		
		results.add(executor.submit((Callable)
				new SeriesSeqCalculator((nThreads - 1)*len + 1, N)));
		
		BigInteger result = new BigInteger("0");
		while(!results.isEmpty()){
			try {
				result = result.add(results.poll().get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
		return result;
	}

	
}
