/**
Author: Lei Zhang
File Creation Date: Apr 20, 2014
Class Description:
*/
package MartaModeling;

import java.util.Random;

public class MartaModelingUtil {

	
	
	
	public MartaModelingUtil() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * This function inputs a lambda, then output 
	 * a value that generated according to a standard
	 * Possion model.
	 * **/
	public static int Possion (double lambda) {
		Random r = new Random();
	    double L = Math.exp(-lambda);
	    int k = 0;
	    double p = 1.0;
	    do {
	        p = p * r.nextDouble();
	        k++;
	    } while (p > L);
	    return k - 1;
	}
	
	
	public static void main(String a[]) {
		double sum = 0;
		double lambda = 11571/(30*60);
		for(int j=0;j<=30;j++) {
			sum = 0;
			for (int i=0;i<1800;i++) {
				sum += MartaModelingUtil.Possion(lambda);
			}
			System.out.println("Sum = " + sum);
		}
	}
	
	
	/**
	 * Given the minute of a day(e.g. 80 means 1.20 am, 1410 means 11.30pm),
	 * return the hourtype of that time. E.g. if you are given 510(8.30), you
	 * might be returned MORNING_RUSH_HOUR type.
	 * ***/
	public static HourType getHourType(int minute) {
		HourType res;
		if( minute <= 450 ) //Earlier than 7.30am will be considered as early morning hours
			res = HourType.EARLY_MORNING;
		else if( minute <= 600 ) //7.30am througth 10am is considered as monring rush hour
			res = HourType.MORNING_RUSH_HOUR;
		else if (minute <= 960)  //10am - 4pm will be considerd as day_time hour
			res = HourType.DAY_TIME;
		else if (minute <= 1140) // 4pm - 7pm is rush hour in the afternoon 
			res = HourType.AFTERNOON_RUSH_HOUR;
		else
			res = HourType.EVENING_HOUR;
		
		return res;
	}
	
	public static int sumIntArray(int a[]) {
		int res = 0;
		for(int i=0;i<a.length;i++)
			res += a[i];
		return res;
	}
	
	public static void printNozero(int a[]) {
		for(int i=0;i<a.length;i++) { 
			if(a[i] != 0)
			System.out.print(i + ">>>>" + a[i] + ",");
		}
		System.out.println();
	}
	
	public static void printNozero(int a[], int from, int to) {
		for(int i=from;i<to;i++) { 
			if(a[i] != 0)
			System.out.print(i + ">>>>" + a[i] + ",");
		}
		System.out.println();
	}
	
}
