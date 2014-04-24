package genDevs.simulation;

import GenCol.*;
import java.util.*;
import genDevs.modeling.*;
import util.*;

public class coordinator extends atomicSimulator implements CoordinatorInterface{

protected coupledDevs myCoupled;
protected digraph myGraph;
protected couprel cr;
protected Function modelToSim;    // store brother models and simulators
protected Function internalModelTosim; // store children modles and simulators
protected ensembleSet simulators;
protected ensembleSet newSimulators, deletedSimulators;
protected double tN,tL,e;
protected double  INFINITY  = DevsInterface.INFINITY;
protected couprel coupInfo,extCoupInfo;


public coordinator(){} //added for use with SimView

public coordinator(coupledDevs c,boolean core){
myCoupled = c;
myGraph = (digraph)c;
myCoupled.setCoordinator(this);
myModel = (IOBasicDevs)c;
simulators = new ensembleSet();
newSimulators = new ensembleSet();
deletedSimulators = new ensembleSet();
cr = c.getCouprel();
modelToSim = new Function();
internalModelTosim = new Function();
coupInfo = new couprel();
extCoupInfo = new couprel();
input = new message();
output = new message();
}

public coordinator(coupledDevs c) {this(c, true, null);}

public coordinator(coupledDevs c, boolean setSimulators, Object dummyParameter){
myCoupled = c;
myGraph = (digraph)c;
myCoupled.setCoordinator(this);
myModel = (IOBasicDevs)c;
cr = c.getCouprel();
modelToSim = new Function();
internalModelTosim = new Function();
coupInfo = new couprel();
extCoupInfo = new couprel();
input = new message();
output = new message();
simulators = new ensembleSet();
newSimulators = new ensembleSet();
deletedSimulators = new ensembleSet();
//c.setCoordinator(this);
if (setSimulators) {
    setSimulators();
    informCoupling();
}
}

public coupledDevs getCoupled(){
return myCoupled;
}

public digraph getDigraph(){
  return myGraph;
}


public void setSimulators(){
  componentIterator cit = myCoupled.getComponents().cIterator();
  while (cit.hasNext()){
    IOBasicDevs iod = cit.nextComponent();
    if(iod instanceof atomic)    //do a check on what model is
      addSimulator(iod);
    else if(iod instanceof digraph)
      addCoordinator((Coupled) iod);
  }

  tellAllSimsSetModToSim();
}

protected void tellAllSimsSetModToSim()
{
  Class [] classes = {ensembleBag.getTheClass("GenCol.Function")};
  Object [] args  = {modelToSim};
  simulators.tellAll("setModToSim",classes,args);
}
public void addSimulator(IOBasicDevs comp){
coupledSimulator s = new coupledSimulator(comp);
s.setRootParent(this);      // set the parent
simulatorCreated(s, comp);
// later will download modelToSim to its children and then will be updated by its parents
// so after initialization, modelToSim store the brother models and simulators
// internalModelTosim store its children models and simulators
}

public void setNewSimulator(IOBasicDevs iod){
    if(iod instanceof atomic){    //do a check on what model is
        coupledSimulator s = new coupledSimulator(iod);
        s.setRootParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
    else if(iod instanceof digraph){
        coupledCoordinator s = new coupledCoordinator((Coupled) iod);
        s.setRootParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
}

public void addCoordinator(Coupled comp){
coupledCoordinator s = new coupledCoordinator(comp);
s.setRootParent(this);       // set the parent
simulatorCreated(s, comp);
// later will download modelToSim to its children and then will be updated by its parents
// so after initialization, modelToSim store the brother models and simulators
// internalModelTosim store its children models and simulators
}

    protected void simulatorCreated(atomicSimulator simulator, IOBasicDevs devs)
    {
        simulators.add(simulator);
        modelToSim.put(devs.getName(), simulator);
        internalModelTosim.put(devs.getName(), simulator);
    }

public void putMyMessages(ContentInterface c){
output.add(c);
//System.out.println("----------print Top output message!-----------");
//output.print();
//System.out.println("----------finish printing Top output message!-----------");
}

public void showCoupling(){
System.out.println("The coupling is: ");
extCoupInfo.print();
}

public void informCoupling(){
  Iterator it = cr.iterator();
  while (it.hasNext()){
    Pair pr = (Pair)it.next();
    Pair cs = (Pair)pr.getKey();
    Pair cd = (Pair)pr.getValue();
    String src =  (String)cs.getKey();
    String dst =  (String)cd.getKey();
    if(src.equals(myCoupled.getName())){
        addExtPair(cs,cd);
        //showCoupling();
        }
    else {                                         // download coupling info to the src simulator
      if(modelToSim.get(src) instanceof CoupledSimulatorInterface){
         CoupledSimulatorInterface sim = (CoupledSimulatorInterface)modelToSim.get(src);
         sim.addPair(cs,cd);
        // sim.showCoupling();
      }
      else if(modelToSim.get(src) instanceof CoupledCoordinatorInterface){
         CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)modelToSim.get(src);
         sim.addPair(cs,cd);
      // sim.showCoupling();
      }
    }
  }

}

// the code below is for variable structure DEVS
public void addCoupling(String src, String p1, String dest, String p2){
//    System.out.println("addCoupling:"+src+":"+p1+"  To  "+dest+":"+p2);
    Pair cs = new Pair(src,p1);
    Pair cd = new Pair(dest,p2);
    if(src.equals(myCoupled.getName())){
        addExtPair(cs,cd);
//        showCoupling();
        }
    else {                                         // download coupling info to the src simulator
      if(internalModelTosim.get(src) instanceof CoupledSimulatorInterface){
         CoupledSimulatorInterface sim = (CoupledSimulatorInterface)internalModelTosim.get(src);
         sim.addPair(cs,cd);
//         sim.showCoupling();
      }
      else if(internalModelTosim.get(src) instanceof CoupledCoordinatorInterface){
         CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)internalModelTosim.get(src);
         sim.addPair(cs,cd);
//         sim.showCoupling();
      }
    }
}

public void removeCoupling(String src, String p1, String dest, String p2){
//    System.out.println("removeCoupling:"+src+":"+p1+":"+dest+":"+p2);
    Pair cs = new Pair(src,p1);
    Pair cd = new Pair(dest,p2);
    if(src.equals(myCoupled.getName())){
        removeExtPair(cs,cd);
//        showCoupling();
        }
    else {                                         // download coupling info to the src simulator
      if(internalModelTosim.get(src) instanceof CoupledSimulatorInterface){
         CoupledSimulatorInterface sim = (CoupledSimulatorInterface)internalModelTosim.get(src);
         sim.removePair(cs,cd);
//         sim.showCoupling();
      }
      else if(internalModelTosim.get(src) instanceof CoupledCoordinatorInterface){
         CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)internalModelTosim.get(src);
         sim.removePair(cs,cd);
//         sim.showCoupling();
      }
    }
}

public void removeModelCoupling(String modelName){
  couprel mc = myCoupled.getCouprel();
  Iterator it = mc.iterator();
  while (it.hasNext()){
    Pair pr = (Pair)it.next();
    Pair cs = (Pair)pr.getKey();
    Pair cd = (Pair)pr.getValue();
    String src =  (String)cs.getKey();
    String dst =  (String)cd.getKey();
    if(src.equals(modelName)||dst.equals(modelName)){
        ((digraph)myCoupled).removePair(cs,cd);    // update the coupling info of the Coupled model
        removeCoupling(src,(String)cs.getValue(),dst,(String)cd.getValue());
    }
  }
}

/*  // comment out by Xiaolin Hu
public void copyModelCoupling(couprel nc,String oldnm,IODevs newd){
   Iterator it = nc.iterator();
  while (it.hasNext()){
  Pair pr = (Pair)it.next();
    Pair cs = (Pair)pr.getKey();
    Pair cd = (Pair)pr.getValue();
    String src =  (String)cs.getKey();
    String dst =  (String)cd.getKey();
    myCoupled.addCoupling(src.equals(oldnm)?newd:myCoupled.withName(src),(String)cs.getValue(),
                dst.equals(oldnm)?newd:myCoupled.withName(dst),(String)cd.getValue());
  }
}
*/

//by saurabh
public couprel getModelCoupling(String modelName){

  couprel coup = new couprel();

  couprel mc = myCoupled.getCouprel();
  Iterator it = mc.iterator();
  while (it.hasNext()){
    Pair pr = (Pair)it.next();
    Pair cs = (Pair)pr.getKey();
    Pair cd = (Pair)pr.getValue();
    String src =  (String)cs.getKey();
    String dst =  (String)cd.getKey();
    if(src.equals(modelName)||dst.equals(modelName)){
      coup.add(cs, cd);
    }
  }
//  myCoupled.printCouprel(coup);
  return coup;
}

public couprel getExternalModelCouplings(digraph inModel){
  couprel coup = new couprel();

  //s.s("printing inside coordinator getExternal Modelcouplings:::::");
  coup = getModelCoupling(inModel.getName());

  //inModel.printComponents();
  //inModel.printCouprel(inModel.getCouprel());

  couprel updated = new couprel();

  couprel minus = inModel.getCouprel();
  Iterator it = minus.iterator();
  while (it.hasNext()){
    Pair pr = (Pair)it.next();
    Pair cs = (Pair)pr.getKey();
    Pair cd = (Pair)pr.getValue();
    String src =  (String)cs.getKey();
    String dst =  (String)cd.getKey();

    Iterator it2 = coup.iterator();
    while(it2.hasNext()){
      Pair pr2 = (Pair)it2.next();
      Pair cs2 = (Pair)pr2.getKey();
      Pair cd2 = (Pair)pr2.getValue();
      String src2 =  (String)cs2.getKey();
      String dst2 =  (String)cd2.getKey();
      //s.s("i searching<<<<<<<<<<<<< the coupling)"+src2+"))))))))))))"+dst2+"))))))))))))");

      if(!(src.equals(src2) && dst2.equals(dst))){
        //s.s("i do not found the coupling)))))))))))))))))))))))))");
        updated.add(cs2,cd2);
      }

    }
 }

/*
  s.s("the final coupplings are:::::::::::::::::::::::::::::::::");
  inModel.printCouprel(updated);
  s.s("and the model couplins are still");
  inModel.printCouprel(minus);
*/


  return updated;
}





public void removeModel(IODevs model){
      String modelName = model.getName();
      if(internalModelTosim.get(modelName) instanceof CoupledSimulatorInterface){
         CoupledSimulatorInterface sim = (CoupledSimulatorInterface)internalModelTosim.get(modelName);
         deletedSimulators.add(sim);
         internalModelTosim.remove(modelName);
      }
      else if(internalModelTosim.get(modelName) instanceof CoupledCoordinatorInterface){
         CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)internalModelTosim.get(modelName);
         deletedSimulators.add(sim);
         internalModelTosim.remove(modelName);
      }
}

public void addExtPair(Pair cs,Pair cd) {
extCoupInfo.add(cs,cd);
}

public void removeExtPair(Pair cs,Pair cd) {
extCoupInfo.remove(cs,cd);
}

public double getCurrentTime(){
return tN;
}

public void initialize(){
simulators.tellAll("initialize");
tL = 0;
updateChangedSimulators();
}

public void initialize(Double d){
initialize(d.doubleValue());
}

// initialized to the same time as the center coord,
// useful for real time and distributed simulation
public void initialize(double time){
//System.out.println(myCoupled.getName()+" Initialize !!!!!!!!!!!");
Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
Object [] args  = {new Double(time)};
simulators.tellAll("initialize",classes,args);
updateChangedSimulators();
}

public double getTN(){
return tN;
}

public double nextTN() {
//ensembleInterface result = new threadEnsembleSet();
ensembleInterface result = new ensembleSet();
Class [] classes  = {};
Object [] args  = {};
simulators.AskAll(result,"nextTNDouble",classes,args);
TreeSet t = new TreeSet(result);
Double d = (Double)t.first();   // get the smallest tN
return d.doubleValue();
}

public void computeInputOutput(double time) {
Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
Object [] args  = {new Double(time)};
simulators.tellAll("computeInputOutput",classes,args);
// send output to the corresponding model based on the coupling information
simulators.tellAll("sendMessages");
}


public void wrapDeltFunc(double time) {
  sendDownMessages();
  Class [] classes  = {ensembleBag.getTheClass("java.lang.Double")};
  Object [] args  = {new Double(time)};
  simulators.tellAll("DeltFunc",classes,args);
  input = new message();
  output = new message();
  updateChangedSimulators();
}

public void updateChangedSimulators(){  // for variable structure capability
  //check if there are added or removed simulators
  Iterator nsit = newSimulators.iterator();
  Iterator dsit = deletedSimulators.iterator();
  if(nsit.hasNext()||dsit.hasNext()){
      // need to update the simulators and download the internalModelTosim to simulators
      while (nsit.hasNext()) simulators.add(nsit.next());
      while (dsit.hasNext()) simulators.remove(dsit.next());
      //download the new ModtoSim info to all the simulators
      Class [] sclasses = {ensembleBag.getTheClass("GenCol.Function")};
      Object [] sargs  = {internalModelTosim};
      simulators.tellAll("setModToSim",sclasses,sargs);
  }
  // reset newSimulators and deletedSimulators to empty
  newSimulators = new ensembleSet();
  deletedSimulators = new ensembleSet();
}

public void sendDownMessages() {
  if(!input.isEmpty()){
    Relation r = convertInput(input);
    Iterator rit = r.iterator();
    while (rit.hasNext()){
       Pair p = (Pair)rit.next();
       Object ds = p.getKey();
       content co = (content)p.getValue();
       if(internalModelTosim.get(ds) instanceof CoupledSimulatorInterface){
           CoupledSimulatorInterface sim = (CoupledSimulatorInterface)internalModelTosim.get(ds);
           sim.putMessages(co);
       }
       else if(internalModelTosim.get(ds) instanceof CoupledCoordinatorInterface){
           CoupledCoordinatorInterface sim = (CoupledCoordinatorInterface)internalModelTosim.get(ds);
           sim.putMessages(co);
       }
    }
  }
}

public Relation convertInput(MessageInterface x) {
  Relation r = new Relation();
  message  msg = new message();
  if(x.isEmpty()) return r;
  ContentIteratorInterface cit = ((message)x).mIterator();
  while (cit.hasNext()){
     content co = (content)cit.next();
     HashSet s = extCoupInfo.translate(myCoupled.getName(), co.getPort().getName());
     Iterator it = s.iterator();
     while(it.hasNext()){
        Pair cp = (Pair) it.next();
        Object ds = cp.getKey();
        String por = (String)cp.getValue();
        Object tempval = co.getValue();
        content tempco = new content(por,(entity)tempval);
        r.put(ds,tempco);

        convertInputHook1(co, cp, tempco);
     }
  }
  return r;
}

public void simInject(double e,MessageInterface m){
double t = tL+e;
if (t <= nextTN()){
input = m;
wrapDeltFunc(t);
System.out.println("Time: " + t +" ,input injected:----------> " );
m.print();
showModelState();
}
else System.out.println("Time: " + tL+ " ,ERROR input rejected : elapsed time " + e +" is not in bounds.");
}

public String toString(){
return myCoupled.toString();
}

public void showModelState(){
simulators.tellAll("showModelState");
}

public void  simulate(double tTime)
{
  int i=1;
  tN = nextTN();
  while( (tN < DevsInterface.INFINITY) ) {
//    if(tN >tTime) break;  // Xiaolin Hu, Nov 27, 2005 -- for temporary testing purpose
//    Logging.log("ITERATION " + i + " ,time: " + tN, Logging.full);

    computeInputOutput(tN);
//    showOutput();
    wrapDeltFunc(tN);
    tL = tN;
    tN = nextTN();
 //   showModelState();
    i++;
  }
  System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN);
}

public void  simulate(int num_iter,double aa){
  int i=1;
  double compIOTime=0, TNTime=0, wrapDTime=0;

  tN = nextTN();
  double bb = System.currentTimeMillis();
  double eTime = bb-aa;
  System.out.println("after nextTN Time: " + eTime);
  aa=bb;
  while( (tN < DevsInterface.INFINITY) && (i<=num_iter) ) {
//    Logging.log("ITERATION " + i + " ,time: " + tN, Logging.full);
    //System.out.println(" ------------ ITERATION " + i +" tN "+tN);
    //System.out.println(myModel.getName()+"-------size-----"+imminents.size());

    computeInputOutput(tN);

    bb = System.currentTimeMillis();
    eTime = bb-aa;
    //System.out.println("after computeInputOutput Time: " + eTime);
    compIOTime += eTime;
    aa=bb;

    //showOutput();
    wrapDeltFunc(tN);

    bb = System.currentTimeMillis();
    eTime = bb-aa;
   // System.out.println("after wrapDeltFunc Time: " + eTime);
    wrapDTime += eTime;
    aa=bb;

    tL = tN;
    tN = nextTN();

    bb = System.currentTimeMillis();
    eTime = bb-aa;
   // System.out.println("after shortNextTN Time: " + eTime);
    TNTime += eTime;
    aa=bb;

    //showModelState();
    i++;
  }
System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN
                   + "__"+TNTime+ "__"+compIOTime+ "__"+wrapDTime);
}

public void  simulate(int num_iter){
  int i=1;
  tN = nextTN();
  while( (tN < DevsInterface.INFINITY) && (i<=num_iter) ) {
//    Logging.log("ITERATION " + i + " ,time: " + tN, Logging.full);
//    System.out.println(" ------------ ITERATION " + i +" tN "+tN);
    computeInputOutput(tN);
    //showOutput();
    wrapDeltFunc(tN);
    tL = tN;
    tN = nextTN();
    //showModelState();
    i++;
  }
//  System.out.println("Terminated Normally at ITERATION " + i + " ,time: " + tN);
}

//not yet adapted for coordinator
public MessageInterface makeMessage(){return new message();}

public void simInject(double e,String portName,entity value){
//  for use in usual devs
simInject(e,new port(portName),value);
}

    /**
     * Returns a list of the ports (along with their components) to which
     * the given port is a source.
     *
     * @param   portName    The source port name on the source component.
     * @return              The list of couplings to the source port's
     *                      desination ports.
     */
    public List getCouplingsToSourcePort(String portName)
    {
        return AtomicSimulatorUtil.getCouplingsToSourcePort(portName,
            myCoupled.getName(), coupInfo, extCoupInfo, modelToSim,
            internalModelTosim, null);
    }

    protected void convertInputHook1(content oldContent, Pair coupling, content newContent) {}

}


