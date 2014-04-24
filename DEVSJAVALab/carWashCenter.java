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

public class carWashCenter extends ViewableAtomic{

double carServingTime = 10;
double truckServingTime =25;
entity car,currentJob = null;
protected DEVSQueue q;

public carWashCenter() {this("carWashCenter");}

public carWashCenter(String name){
    super(name);
    addInport("carIn");
    addInport("truckIn");
    addOutport("out");

    addTestInput("carIn",new entity("testCar"));
    addTestInput("truckIn",new entity("testCar"),5);
}

public void initialize(){
     q = new DEVSQueue();
passivate();
}


public void  deltext(double e,message x)
{
Continue(e);

if(phaseIs("passive")){
   for (int i=0; i< x.getLength();i++){
     if (messageOnPort(x, "carIn", i)) {
       car = x.getValOnPort("carIn", i);
       currentJob = car;
       holdIn("active", carServingTime);
     }
     if (messageOnPort(x, "truckIn", i)) {
       car = x.getValOnPort("truckIn", i);
       currentJob = car;
       holdIn("active", truckServingTime);
     }
   }
}

 else if(phaseIs("active")){
    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "carIn", i)) {
        car = x.getValOnPort("carIn", i);
        q.add(car);
      }
    }
 }
}


public void  deltint( )
{
if(phaseIs("active")){
   if(!q.isEmpty()) {
     currentJob = (entity)q.first();
     holdIn("active",carServingTime);
     q.remove();
   }
   else
          passivate();
 }
}

public message  out( )
{
   message  m = new message();
   content con = makeContent("out",
            new entity(currentJob.getName()+"_Finished"));
   m.add(con);

  return m;

}

public String getTooltipText(){
	return super.getTooltipText()+"\n number of cars in queue:"+q.size()+
	"\n my current job is:" + currentJob.toString();
}



}

