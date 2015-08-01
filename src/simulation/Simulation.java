package simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class Simulation {

	private static ArrayList<Double> times = new ArrayList<Double>();
	private static boolean newRoom = true;
	//private static Experiment tempExp;
	private static int exitWidth;
	
	
	//testing area
	public static void main(String[] args) throws InterruptedException, IOException{
		final Scanner s = new Scanner(System.in);
		int exitIteration = 0;
		while (newRoom) {
			//create new experiment
			//ask for room properties
			System.out.println("Room width: ");
			final int roomWidth = s.nextInt();
			System.out.println("Room height: ");
			final int roomHeight = s.nextInt();
			System.out.println("Number of evacuees: ");
			final int noOfEvac = s.nextInt();
			System.out.println("Evacuee speed: ");
			double evacSpeed = s.nextDouble();
			
			//ask for exit properties
			System.out.println("Exit width: ");
			int exitSize = s.nextInt();
			exitWidth = exitSize;
			
			System.out.println("How many experiments would you like to run?");
			final int noExp = s.nextInt();
			
			//timeStep kept consistent in order to prevent time scaling issues
			final double timeStep = 1.0;
			//ask if graphics desired
			System.out.println("Finally, would you like to view graphics? (say true or false)");
			final boolean graphics = s.nextBoolean();
			//model evacuee
			final Evacuee modelEvac = new Evacuee(15, 0.75, 0.5, evacSpeed);
			int count = 0;
			//executor (uses multithreading to process all trials)
			ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());	
			for (int i = 0; i < noExp; i++){
				count++;
				//create new thread that calculates and adds times to 'times' arraylist
				executor.execute(retrieveSubExpRunnable(roomWidth, roomHeight, noOfEvac, modelEvac, graphics, timeStep, i));
			}
			//begin closing threads
			executor.shutdown();
			//wait until threads closed before continuing
			while (!executor.awaitTermination(10, TimeUnit.SECONDS)){
				Thread.sleep(10);
			}
			//calculate average
			System.out.println(count);
			double avg = calculateAverage(count,times);
			System.out.println(times.size());
			//calculate error
			double error = calculateUncertaintyOnMean(count, avg);
			//save to file
			FileWriter fw = new FileWriter("src\\results.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			//write in format: roomWidth roomHeight #Evacuees EvacSpeed ExitSize Avg Error
			bw.write(""+roomWidth + " " + roomHeight + " " + noOfEvac + " " + evacSpeed + " " + exitSize + " " + avg + " " + error + "\n");
			bw.close();
			fw.close();
			//print to console
			System.out.println("Avg: " + avg + " +/- "+ error + "\n");
			//pause to allow printing above
			Thread.sleep(10);
			//ask if more simulations needed
			System.out.println("Would you like to try a new room configuration? (say true or false)");
			newRoom = s.nextBoolean();
			if (newRoom == false){
				s.close();
				System.exit(0);
			}
			else {
				//reset times ready for next experiment
				times = new ArrayList<Double>();
			}
			exitIteration++;
		}
	}
	
	private static double calculateAverage(int noOfDataPoints, ArrayList<Double> values){
		double sum = 0;
		for (int i = 0; i < values.size(); i++){
			sum += values.get(i);
		}
		return (sum/noOfDataPoints);
	}
	
	//helper function for error
	private static double calculateUncertaintyOnMean(int noOfDataPoints, double mean){
		//calculate standard deviation
		ArrayList<Double> diffSquares = new ArrayList<Double>();
		for (int i = 0; i < times.size(); i++){
			//add the difference between value and mean squared to diffSquares
			diffSquares.add(Math.pow((times.get(i)-mean), 2));
		}
		//get sum of diffSquares:
		double sum = 0;
		for (int i = 0; i < diffSquares.size(); i++){
			sum += diffSquares.get(i);
		}
		//divide by noOfDataPoints
		double temp = sum/noOfDataPoints;
		//square root to get std.dev
		return Math.sqrt(temp);
	}
	
	public static Runnable retrieveSubExpRunnable(final int roomWidth, final int roomHeight, final int noOfEvac, final Evacuee modelEvac, final boolean graphics, final double timeStep, final int i){
		return new Runnable()
		{

			@Override
			public void run() {
				Experiment tempExp = new Experiment(roomWidth, roomHeight, noOfEvac, modelEvac, new Exit(exitWidth, 0, (roomHeight-exitWidth)/2), graphics, timeStep, i+1);
				while (!tempExp.hasFinished()){
					//wait for 1 second each time the exp has not finished, then skip loop when exp finished to continue to next exp
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					
				}
				if (tempExp.hasFinished()){
					System.out.println("adding time");
					times.add(tempExp.getTime());
				}
			}
			
		};
	}
}
