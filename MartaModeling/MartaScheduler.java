/**
Author: Lei Zhang
File Creation Date: Apr 21, 2014
Class Description:
*/
package MartaModeling;

import javax.swing.text.html.parser.Entity;

import genDevs.modeling.content;
import genDevs.modeling.message;
import GenCol.entity;
import simView.ViewableAtomic;
import statistics.rand;

public class MartaScheduler extends ViewableAtomic {

	private int numOfWating;
	private int globalWaitingQueue[][];
	private int mergedGWaitingQ[];
	private boolean ifNeedNewTrain;
	private int trainNum;
	private int lastTrainMinute;
	private boolean isAdjustTrain;
	private int period = 0;
	
	public MartaScheduler(String name) {
		super(name);
		globalWaitingQueue = new int[MartaConst.NUM_OF_STATIONS][MartaConst.SIMULATION_END_TIME];
		mergedGWaitingQ = new int[MartaConst.SIMULATION_END_TIME];
		addOutport("scheduleOut");
		addInport("scheduleIn");
		addOutport("send");
		this.ifNeedNewTrain = false;
		this.isAdjustTrain = false;
		period = -2;
		trainNum = 0;
	}
	
	/**
	 * remember that stationNum starts from 1
	 * **/
	public int[] getWaitingQ(int stationNum) {
		return this.globalWaitingQueue[stationNum-1];
	}
	
	public void initialize(){
	   holdIn("active", 0);
	}
	
	public void  deltext(double e, message x) {
		Continue(e);
//		for (int i=0; i< x.getLength();i++){
//			//ScheduleMsg msg = (ScheduleMsg)(x.getValOnPort("scheduleIn", i));
// 			entity m = x.getValOnPort("scheduleIn", i);
//			ScheduleMsg msg = (ScheduleMsg)m;
////			this.updateWatingQ(msg);
//			this.mergeGWatingQ();
//			System.out.println("Globally, there are " + MartaModelingUtil.sumIntArray(this.mergedGWaitingQ) 
//								+ " wating in all stations the satisfactory rate is " + this.getSatisfactoryRate());
//			//int time = (int )this.getSimulationTime();
//			if(this.getSatisfactoryRate( ) < MartaConst.STATISFACTION_THRESHOLD ) {
////				this.
//				this.ifNeedNewTrain = true;
//				//holdIn("active", 0);
//			}
//			m = null;
//		}
	}
	
	private void updateWatingQ(ScheduleMsg msg) {
		int[] stationWatingQ = msg.getstationWaitQ();
		for(int i=0; i<stationWatingQ.length; i++)
			globalWaitingQueue[msg.getStationNum()-1][i] = stationWatingQ[i];
	}
	
	private void mergeGWatingQ() {
		for(int i=0;i<mergedGWaitingQ.length;i++) {
			mergedGWaitingQ[i] = 0;
			for(int j=0;j<MartaConst.NUM_OF_STATIONS;j++)
				mergedGWaitingQ[i] +=  globalWaitingQueue[j][i];
		}
	}
	
	private double getSatisfactoryRate() {
		double res;
		int total = 0;
		int unsafisfied = 0;
		int minute = (int)this.getSimulationTime();
		
		this.mergeGWatingQ();
		
		for(int i=0; i< minute; i++) {
			total += mergedGWaitingQ[i];
			if( minute - i > MartaConst.SATISFACTION_WATING_LIMIT)
				unsafisfied += mergedGWaitingQ[i];
		}
		if(total == 0) res = 1;
		else res = (double)1 - (double)unsafisfied/(double)total;
		return res;
	}
	
	public void  deltint()
	{
		if(phaseIs("active")) {
			holdIn("active", 2);
			period++;
			if(period == (MartaConst.MARTA_TRAIN_INTERVAL) /2 || period < 0) {
				this.ifNeedNewTrain = true;
				this.isAdjustTrain = false;
				period = 0;
			} else if(this.getSatisfactoryRate( ) < MartaConst.STATISFACTION_THRESHOLD ) {
				this.ifNeedNewTrain = true;
				this.isAdjustTrain = true;
				period = 0;
			}
		}
	}
	
	public message out()
	{
		message  m = new message();
		if(this.ifNeedNewTrain) {
			MartaTrain train = null;
			//create a new train
			if(this.isAdjustTrain)
				train = new MartaTrain("Rescheduled Train" + this.trainNum);
			else
				train = new MartaTrain("Regular Train" + this.trainNum);
			this.trainNum++;
			content con = makeContent("scheduleOut",train);
			m.add(con);
//			if(this.isAdjustTrain) {
			String strMsg = "Quality is " + this.getSatisfactoryRate();
			MartaModelingUtil.printNozero(this.mergedGWaitingQ);
			content con1 = makeContent("send",new entity(strMsg));
			m.add(con1);
//			}
			this.lastTrainMinute = (int)this.getSimulationTime();
			this.ifNeedNewTrain = false;
		}
		return m;
	}
}
