/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */



package SimpArc;

import java.awt.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;

public class efp extends ViewableDigraph {


public efp (){
    super("efp");
    //ViewableAtomic sp = new proc("proc",25);
   //ViewableDigraph  sp  = new DandC3("d",30);
     //ViewableDigraph  sp  = new pipeSimple("p",30);
    ViewableDigraph  sp  = new multiServer("m",30,3);



    ViewableDigraph  expf = new ef("ExpFrame",10,1000);



    add(expf);

    add(sp);

    addTestInput("start",new entity());

     addCoupling(this,"start",expf,"start");
     addCoupling(this,"stop",expf,"stop");

     addCoupling(expf,"out",sp,"in");
     addCoupling(sp,"out",expf,"in");

   initialize();

    preferredSize = new Dimension(549, 181);
    expf.setPreferredLocation(new Point(239, 23));
    sp.setPreferredLocation(new Point(15, 50));
 }
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(957, 579);
        ((ViewableComponent)withName("ExpFrame")).setPreferredLocation(new Point(239, 23));
        ((ViewableComponent)withName("d")).setPreferredLocation(new Point(97, 297));
    }
 }
