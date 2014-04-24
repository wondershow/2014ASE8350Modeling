/**
Author: Lei Zhang
File Creation Date: Apr 21, 2014
Class Description:
*/
package MartaModeling;
public enum HourType {
	EARLY_MORNING,
	MORNING_RUSH_HOUR,
	DAY_TIME,
	AFTERNOON_RUSH_HOUR,
	EVENING_HOUR;
	
	
	public static int hour2Num(HourType h) {
		switch(h) {
			case EARLY_MORNING:
				return 0;
			case MORNING_RUSH_HOUR:
				return 1;
			case DAY_TIME:
				return 2;
			case AFTERNOON_RUSH_HOUR:
				return 3;
			case	
				EVENING_HOUR:
				return 4;
			default:
				return -1;
		}
	}
	
	public static HourType num2Hour(int h) {
		switch(h) {
			case 0:
				return EARLY_MORNING;
			case 1:
				return MORNING_RUSH_HOUR;
			case 2:
				return DAY_TIME;
			case 3:
				return AFTERNOON_RUSH_HOUR;
			case 4:
				return EVENING_HOUR;
			default:
				return EARLY_MORNING;
		}
	}
	
	public static int[] getLambda() {
		
		return new int[] {};
	}
}


