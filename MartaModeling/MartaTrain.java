/**
Author: Lei Zhang
File Creation Date: Apr 22, 2014
Class Description:
*/
package MartaModeling;

import GenCol.entity;

public class MartaTrain extends entity{
	
	
	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	
	private int getCapacity() {
		return capacity;
	}

	private void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getNumOnBoard() {
		return numOnBoard;
	}

	public void setNumOnBoard(int numOnBoard) {
		this.numOnBoard = numOnBoard;
	}

	public boolean isTrainStuffed() {
		return isTrainStuffed;
	}

	public void setTrainStuffed(boolean isTrainStuffed) {
		this.isTrainStuffed = isTrainStuffed;
	}

	//The arrival time of a train
	private int endTime;

	//How many ppl maximum a train can hold
	private int capacity;
	
	//How many passengers are currently on board
	private int numOnBoard;
	
	//whether this train can ride more ppl
	private boolean isTrainStuffed;
	
	//The departure time of a train, in which minute of a day. eg.  400 means 6.40am
	private int startTime;
	
	//If this train is empty(no more pasgners get off anymore)
	private boolean isTrainEmpty;
	
	public boolean isTrainEmpty() {
		return isTrainEmpty;
	}

	public void setTrainEmpty(boolean isTrainEmpty) {
		this.isTrainEmpty = isTrainEmpty;
	}

	public MartaTrain(String trainNname) {
		super(trainNname);
		this.capacity = MartaConst.CAR_CAPACITY*MartaConst.CARS_IN_TRAIN;
		this.setTrainStuffed(false);
		this.setTrainEmpty(true);
	}
	
	/**
	 * To model the passengers board on a train
	 * @return how many passengers fail to board on this train
	 * **/
	public int boardOnTrain(int numOfPsngrs) {
		int res;
		if( this.numOnBoard + numOfPsngrs <= this.capacity ) { // enuf space to hold incoming ppl
			res =  0;
			this.numOnBoard = this.getNumOnBoard() + numOfPsngrs;
			this.setTrainStuffed(false);
		} else { // not enuf space
			int spaceAvailable = this.capacity - this.getNumOnBoard();
			res = numOfPsngrs - spaceAvailable;
			this.numOnBoard = this.getNumOnBoard() + spaceAvailable;
			this.setTrainStuffed(true);
		}
		
		if(res > 0) 
			System.out.println("--------------------Sorry, there are ppl cant board on");
		
		
		
		if(numOfPsngrs > 0)
			this.setTrainEmpty(false);
		return res;
	}
	
	/***
	 * The departure model needs to be reconsidered
	 * @return # of ppl succefully get off
	 * **/
	public int getOffTrain(int num2getOff) {
		int res = 0;
		if(num2getOff >= this.numOnBoard) {
			this.numOnBoard = 0;
			this.setTrainEmpty(true);
			res = this.numOnBoard;
		} else {
			this.numOnBoard = 	this.numOnBoard - num2getOff;
			this.setTrainEmpty(false);
			res = num2getOff;
		}
		if(num2getOff >0)
			this.setTrainStuffed(false);
		return res;
	}
}
