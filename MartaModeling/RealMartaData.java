/**
Author: Lei Zhang
File Creation Date: Apr 22, 2014
Class Description:
*/
package MartaModeling;
public class RealMartaData {

	private static final int OFFSET = MartaConst.startHour;
	public RealMartaData() {
		// TODO Auto-generated constructor stub
	}
	
	public static int[] urban_Entry = new int[] {
			382,
			104,
			46,
			21,
			178,
			530,
			2156,
			3349,
			3370,
			3484,
			4236,
			5052,
			6017,
			5950,
			7528,
			9719,
			11507,
			9898,
			5883,
			4017,
			2737,
			2283,
			1658,
			1750,
	};
	
	public static int urban_Exit[] = new int[] {
			485,
			189,
			59,
			23,
			68,
			1022,
			4865,
			9166,
			12296,
			9774,
			6755,
			5624,
			6138,
			5495,
			5190,
			4023,
			3782,
			3430,
			2520,
			1793,
			1446,
			1503,
			1500,
			961,
	}; 
	
	
	public static int suburban_Entry[] = new int[] {
			474,
			90,
			22,
			18,
			549,
			5370,
			8326,
			11571,
			10749,
			7740,
			6329,
			6619,
			7075,
			7383,
			6649,
			7749,
			7207,
			5611,
			4348,
			3376,
			2834,
			2867,
			2468,
			1475
	};
	
	public static int suburban_Exit[] = new int[] {
		1947,
		605,
		35,
		24,
		49,
		1211,
		2991,
		5090,
		6205,
		4386,
		3891,
		4255,
		4795,
		5713,
		6940,
		9178,
		10811,
		12299,
		9356,
		6342,
		4800,
		4096,
		5097,
		4687
	};
	
	
	/**
	 * Get passenger arrival rate of a station
	 * */
	
	public static double getArrivalLambda(int hour, StationType t) {
		double res = 0;
		if(t == StationType.URBAN_STATION)
			res = urban_Entry[hour+OFFSET]/(60*30);
		else
			res = suburban_Entry[hour+OFFSET]/(60*30);
		return res;
	}
	
	public static double getDepartureLambda(int hour, StationType t) {
		double res = 0;
		if(t == StationType.URBAN_STATION)
			res = urban_Exit[hour+OFFSET]/(60*30);
		else
			res = suburban_Exit[hour+OFFSET]/(60*30);
		return res;
	}
}
