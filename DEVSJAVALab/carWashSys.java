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

import java.awt.*;
import java.io.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class carWashSys extends ViewableDigraph{


public carWashSys(){
    this("carWashSys");
}

public carWashSys(String nm){
    super(nm);
    carWashSysConstruct();
}

public void carWashSysConstruct(){

    this.addOutport("out");

    ViewableAtomic car_genr = new carGenr("carGenr",7);
    ViewableAtomic truck = new truckGenr("myTruckG",30);
    ViewableAtomic wash_center = new carWashCenter("carWashCenter");
    ViewableAtomic trand = new transducer ();

     add(car_genr);
     add(wash_center);
     add(truck);
     add(trand);

     addCoupling(car_genr,"out",wash_center,"carIn");
     addCoupling(car_genr,"out",trand,"ariv");
     addCoupling(wash_center,"out",trand,"solved");
     addCoupling(truck,"out",wash_center,"truckIn");
//     addCoupling(trand,"out",car_genr,"in");
     addCoupling(wash_center,"out",this,"out");
}


    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(934, 453);
        if((ViewableComponent)withName("carWashCenter")!=null)
             ((ViewableComponent)withName("carWashCenter")).setPreferredLocation(new Point(401, 260));
        if((ViewableComponent)withName("transd")!=null)
             ((ViewableComponent)withName("transd")).setPreferredLocation(new Point(357, 93));
        if((ViewableComponent)withName("carGenr")!=null)
             ((ViewableComponent)withName("carGenr")).setPreferredLocation(new Point(84, 78));
        if((ViewableComponent)withName("myTruckG")!=null)
             ((ViewableComponent)withName("myTruckG")).setPreferredLocation(new Point(149, 279));
    }
}
