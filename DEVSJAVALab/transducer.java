/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package DEVSJAVALab;

import simView.*;


import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

public class transducer extends  ViewableAtomic{
 protected double  arrived, solved;
 protected double clock,observation_time;
 
 protected int numOfarrivingcars = 0;
 protected int numOfFinishedCars = 0;

 public transducer(String  name,double Observation_time){
  super(name);
   addInport("in");
   addOutport("out");
  addInport("ariv");
  addInport("solved");
  observation_time = Observation_time;
  addTestInput("ariv",new entity("val"));
  addTestInput("solved",new entity("val"));
  initialize();
 }

 public transducer() {this("transd", 800);}

 public void initialize(){
  phase = "active";
  sigma = observation_time;
  clock = 0;
  super.initialize();
 }


 public void  deltext(double e,message  x){
  clock = clock + e;
  Continue(e);
  entity  val;
  for(int i=0; i< x.size();i++){
    if(messageOnPort(x,"ariv",i)){
       val = x.getValOnPort("ariv",i);
       if(val.getName().startsWith("car3")) arrived=clock;
       numOfarrivingcars++;
    }
    if(messageOnPort(x,"solved",i)){
       val = x.getValOnPort("solved",i);
       if(val.getName().startsWith("car3"))  solved = clock;
       numOfFinishedCars++;
    }
  }
 }

 public void  deltint(){
  clock = clock + sigma;
  System.out.println("the total service time for car 3 is: "+(solved-arrived));
  System.out.println("arriving car: "+numOfarrivingcars+"  finshed car:"+numOfFinishedCars);
  passivate();
  show_state();
 }

 public  message    out( ){
  message  m = new message();
  content  con = makeContent("out",new entity("stop"));
  m.add(con);
  return m;
 }



 public void  show_state(){

  System.out.println("car3 arrive time "  +  arrived );
  System.out.println("car3 solved time "  +  solved );
 }
}
