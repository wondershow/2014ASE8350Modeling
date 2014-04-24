/**
Author: Lei Zhang
File Creation Date: Apr 20, 2014
Class Description:
*/
package MartaModeling;

import java.util.Random;

import GenCol.entity;
import genDevs.modeling.content;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class MartaStation extends ViewableAtomic{
	
	private MartaStation previous;
	private MartaStation next;
	private int numOfWatingPsngers;
	private int waitingQueue[];
	private MartaTrain curTrain;
	private int stationNum;
	private StationType type;
	
	
	public int getStationNum() {
		return stationNum;
	}

	public void setStationNum(int stationNum) {
		this.stationNum = stationNum;
	}

	//in which minute we last calcualted the passengers
	private int lastUpdateMinute;
	
	public MartaStation(int number, StationType t,int[] waitingQ) {
		super(Integer.toString(number));
		addInport(MartaConst.STATION_MODEL_IN_PORT_NAME_PREFIX + number);
		addOutport(MartaConst.STATION_MODEL_OUT_PORT_NAME_PREFIX + number);
		addOutport(MartaConst.STATION_MODEL_CTRL_PORT_NAME_PREFIX+number);
		waitingQueue = waitingQ;
		this.stationNum = number;
		this.type = t;
	}
	
	public MartaStation getNextStation() {
		return this.next;
	}
	
	public void  deltext(double e,message x)
	{
		Continue(e);
		for (int i=0; i< x.getLength();i++){
			 String inPort = MartaConst.STATION_MODEL_IN_PORT_NAME_PREFIX + this.getStationNum();
			 //String outPort = MartaConst.STATION_MODEL_OUT_PORT_NAME_PREFIX+this.getStationNum();
			 if (messageOnPort(x,inPort , i)) {
				 curTrain = (MartaTrain)x.getValOnPort(inPort, i);
				 this.handleTrainArrival(curTrain);
				 System.out.println("Before leaving station " + this.getName() + ", there are" 
						 			+ curTrain.getNumOnBoard() + " on " + curTrain.getName() 
						 			+ " at station " + this.getName() );
				 //holdIn("active", MartaConst.TRAVEL_TIME_BTWN_STATIONS);
				 holdIn("active", 2);
				 break;
			 }
		}
	}
	
	private void handleTrainArrival(MartaTrain t) {
		
		this.stationGetOff(t);
		
		//To simulate the passengers arrival since last train left
		this.updateWaitingQueue();
		
		//Passengers on this station get on board
		this.stationBoardOnTrain(t);
	}
	
	public void  deltint()	{
		Random r = new Random();
		if(phaseIs("active")){
			   //this.handleTrainArrival(this.curTrain);
			   holdIn("active", MartaConst.STATION_REPORT_INTERVAL + r.nextDouble());
			   //this.passivate();
		}
	}
	
	public MartaStation getPreviousStation() {
		return this.previous;
	}
	
	private void stationBoardOnTrain(MartaTrain t) {
		int sum=0;
		int failedToboard = 0;
		boolean need2DebugPrint = false;
		for(int i=0;i<waitingQueue.length;i++) {
			if(!t.isTrainStuffed()) {// when train is not full
				sum += waitingQueue[i];
				waitingQueue[i] = t.boardOnTrain(waitingQueue[i]);
				if(waitingQueue[i]!=0) {
					failedToboard += waitingQueue[i];
					need2DebugPrint = true;
				}
				sum -= waitingQueue[i];
			}
		}
		if(need2DebugPrint) {
			System.out.println("At station " + this.getName() + "," +sum + " passengers board on train, but  someone failed"+ this.getSimulationTime());
			MartaModelingUtil.printNozero(this.waitingQueue);
		}
		else
			System.out.println("At station " + this.getName() + ",all the " +sum + " passengers board on train time is " + this.getSimulationTime());
	}
	
	private int stationGetOff(MartaTrain t) {
		int getOffNumer = 0;
		int tmp = 0;
		int minute = (int)this.getSimulationTime();
		for(int i=0; i<= minute; i++) {
			double lambda = RealMartaData.getDepartureLambda(i/60, this.type);
			if(!t.isTrainEmpty()) {
				tmp = MartaModelingUtil.Possion(lambda);
				//tmp = this.stationNum ;
				getOffNumer += t.getOffTrain(tmp);
			}
			//getOffNumer +=  MartaModelingUtil.Possion(lambda);
		}
		System.out.println("At station " + this.getName() + " " + getOffNumer + " passengers got off");
		return getOffNumer;
	}
	
	public message out() {
		
		
		//System.out.println(name+" out count "+count);
		String inPort = MartaConst.STATION_MODEL_IN_PORT_NAME_PREFIX+this.getStationNum();
		String outPort = MartaConst.STATION_MODEL_OUT_PORT_NAME_PREFIX+this.getStationNum();
		String ctrlPort = MartaConst.STATION_MODEL_CTRL_PORT_NAME_PREFIX+this.getStationNum();
		
		message  m = new message();
		content con = null; 
		if(this.curTrain == null) { //regular report to center
			this.updateWaitingQueue();
			
			
			String msgName = this.getName() + "msg";
			con = makeContent(ctrlPort,new entity("reportMsg" + this.getName()));
			
			
			
			//ScheduleMsg msg = new ScheduleMsg(msgName,this.getStationNum());
			//msg.copyMsg(this.waitingQueue);
			//			System.out.println("Station " + this.getName() + ", report that there are " + 
			//					MartaModelingUtil.sumIntArray(msg.stationWaitingQueue) + " waiting ");
			
			
			this.updateWaitingQueue();
		} else { // pass train to next station
			con = makeContent(outPort,this.curTrain);
			this.curTrain = null;
		}
//		msg.setTimeStamp(this.gett);
		m.add(con);
		//m.add()
		return m;
	}
	
	private void updateWaitingQueue() {
		int sum = 0;
		int tmp = 0;
		//System.out.println()
		
		int minute = (int)this.getSimulationTime();
		
		for(int i=lastUpdateMinute+1; i<= minute; i++) {
			
			double lambda = RealMartaData.getArrivalLambda(i/60, this.type);
			System.out.println("lambda is " + lambda);
			
			this.waitingQueue[i] = MartaModelingUtil.Possion(lambda);
			
//			tmp = this.stationNum * 5;
//			this.waitingQueue[i] = tmp;
			
			sum += waitingQueue[i];
		}
		System.out.println("At station " + this.getName() + "," +sum + " passengers arrivded");
		//MartaModelingUtil.printNozero(this.waitingQueue);
		
		
		this.lastUpdateMinute = minute;
	}
	
	
}
