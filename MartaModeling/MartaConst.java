/**
Author: Lei Zhang
File Creation Date: Apr 20, 2014
Class Description:
*/
package MartaModeling;
public class MartaConst {
	
//	//in which minute of a day does the simulation start 300 means starts in 5am in the morning
//	public static final int SIMULATION_START_TIME = 300;
//	
	//in which minute of a day does the simulation end, 1410 means 11.30 pm
	public static final int SIMULATION_END_TIME = 1440;
	
	//the threshhold of a client to wait and being unsatisfied
	public static final int SATISFACTION_WATING_LIMIT = 10;
	
	//the threshold of a marta system to be evaluated as "Satisfied"
	public static final double STATISFACTION_THRESHOLD = 0.9;
	
	//the minutes that a train can travel in between two stations.
	public static final int TRAVEL_TIME_BTWN_STATIONS = 2; 
	
	public static final int CAR_CAPACITY = 100;
	
	public static final int MARTA_TRAIN_INTERVAL = 10;
	
	//every what minute the station reports to the center
	public static final int STATION_REPORT_INTERVAL = 3;
	
	//How many cars in a train
	public static final int CARS_IN_TRAIN = 2;
	
	public static final int NUM_OF_STATIONS = 4;
	
	public static final String STATION_MODEL_IN_PORT_NAME_PREFIX = "trainIn";
	
	public static final String STATION_MODEL_OUT_PORT_NAME_PREFIX = "trainOut";
	
	public static final String STATION_MODEL_CTRL_PORT_NAME_PREFIX = "control";
	

	
	private MartaConst() {
		
	}
	
	public static final int[] lambda = {};
	
	//from which hour to start
	public static final int startHour = 6;
}
