package com.gmail.at.ivanehreshi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class App implements Runnable{

	private String inputFile;
	private String outputFile;
	private Scanner scanner = new Scanner(System.in);
	private String[] args;
	PrintWriter writer;
	
	enum InputMode{CONSOLE, FILE};
	
	public App(String[] args) {
		this.args = args;
	}
	
	private void println(String mess){
		System.out.println(mess);
		if(writer != null)
			writer.println(mess);
	}
	
	@Override
	public void run(){
		InputMode mode = menu(args);
		
		if(mode == InputMode.CONSOLE)
			System.out.print("Series parameter N: ");
		int N = scanner.nextInt();
		
		if(mode == InputMode.CONSOLE)
			System.out.print("Threads count: ");
		int nThreads = scanner.nextInt();	
		String result;
		
		try {
			writer = new PrintWriter(new File(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		
		long time1, time2, delta;
		println("Executor(FixedThreadPool): ");
		time1 = System.currentTimeMillis();
		
		ThreadPoolSeriesCalculator calc1 = new ThreadPoolSeriesCalculator(N, nThreads);
		result = calc1.compute().toString();
		
		time2 = System.currentTimeMillis();
		delta = time2 - time1;
		println("result: " + result);
		println("wasted time: " + delta  + " ms");
		
		println("");
		
		println("Own thread pool");
		time1 = System.currentTimeMillis();
		
		OwnPoolSeriesCalculator calc2 = new OwnPoolSeriesCalculator(N, nThreads);
		result = calc2.compute().toString();
		
		time2 = System.currentTimeMillis();
		delta = time2 - time1;
		println("result: " + result);
		println("wasted time: " + delta  + " ms");
		
		scanner.close();
		writer.close();
	}
	
	private InputMode menu(String[] args) {
		System.out.println("You can use command line arguments to specify program parameters: \n" + 
						   " <input> <output>.\n" + 
							"Where <input> could be a path or you can write   console    keyword to read from console");
		
		InputMode mode = InputMode.CONSOLE;
		if(args.length >= 1){
			if(args[0].equalsIgnoreCase("console"))
				mode = InputMode.CONSOLE;
			else
			{
				mode = InputMode.FILE;
				inputFile = args[0];
			}
		}else{
			System.out.println("Input file(or console): ");
			inputFile = scanner.next();
			if(inputFile.equalsIgnoreCase("console"))
				mode = InputMode.CONSOLE;
			else
				mode = InputMode.FILE;
		}
		
		if(args.length >= 2){
			outputFile = args[1];
		}else{
			System.out.println("Enter output file(console output will be anyway )");
			outputFile = scanner.next();
		}
		
		if(mode == InputMode.FILE)
		{
			try {
				scanner = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("input: " +( (mode == InputMode.CONSOLE) ? "console":inputFile ));
		System.out.println("output: console + " + outputFile);
		
		
		return mode;
	}

	public static void main(String[] args) {
		App app = new App(args);
		app.run();
	}
	
}
