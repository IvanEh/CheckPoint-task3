package com.gmail.at.ivanehreshi.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gmail.at.ivanehreshi.OwnPoolSeriesCalculator;
import com.gmail.at.ivanehreshi.ThreadPoolSeriesCalculator;


@RunWith(Parameterized.class)
public class Tests {
	@Parameters
	public static Collection<Object[]> data(){
		return Arrays.asList(new Object[][]{
			{1, 1, "4"},
			{2, 2, "6"}, // 4 2
			{2, 1, "6"},
			{3, 2, "22"}, // 4 2 16
			{3, 12, "22"}, // +1 Achievement: found a bug with JUnit.  
			// http://www.wolframalpha.com/input/?i=sum%282^%28i+-+%28-1%29^i%29%29+from+i+%3D+1..100
			{100, 100, "2535301200456458802993406410750"} 
		});
	}

	private int nThreads;
	private int N;
	private String res;
	
	
	public Tests(int N, int nThreads, String res){
		this.N = N;
		this.nThreads = nThreads;
		this.res = res;
	}
	
	@org.junit.Test
	public void test1() {
		OwnPoolSeriesCalculator calc = new OwnPoolSeriesCalculator(N, nThreads);
		String res = calc.compute().toString();
		assertTrue(res.equals(this.res));
	}
	
	@org.junit.Test
	public void test2() {
		ThreadPoolSeriesCalculator calc = new ThreadPoolSeriesCalculator(N, nThreads);
		String res = calc.compute().toString();
		assertTrue(res.equals(this.res));
	}

}
