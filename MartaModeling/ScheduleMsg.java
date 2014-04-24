/**
Author: Lei Zhang
File Creation Date: Apr 22, 2014
Class Description:
*/
package MartaModeling;

import GenCol.entity;

public class ScheduleMsg extends entity{
	String stationName;
	int stationWaitingQueue[];
	int timeStamp;
	int stationNum;
	
	public int getStationNum() {
		return stationNum;
	}

	public void setStationNum(int stationNum) {
		this.stationNum = stationNum;
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ScheduleMsg(String name,int number) {
		super(name);
		this.stationNum = number;
		this.stationName = name;
		stationWaitingQueue = new int[MartaConst.SIMULATION_END_TIME];
	}
	
	public void copyMsg(int[] stationQ) {
		System.arraycopy(stationQ, 0, this.stationWaitingQueue, 0, stationQ.length);
	}
	
	public int[] getstationWaitQ() {
		return this.stationWaitingQueue;
	}
}
