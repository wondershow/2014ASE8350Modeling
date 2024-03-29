/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 3.0 Beta
 *  Date       : 02-22-03
 */

package simView;

import java.awt.Point;
import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;

/**
 * Defines methods that are common to both ViewableAtomic
 * and ViewableDigraph.
 */
public interface ViewableComponent
{
    /**
     * Returns the name of this devs component.
     */
    String getName();

    /**
     * Returns the graphical view that represents this component in a viewer.
     */
    ComponentView getView();

    /**
     * Returns how many outports this component has.
     */
    int getNumOutports();

    /**
     * Returns a list of the names of the outports of this component.
     */
    List getOutportNames();

    /**
     * Returns how many inports this component has.
     */
    int getNumInports();

    /**
     * Returns a list of the names of the inports of this component.
     */
    List getInportNames();

    /**
     * Returns a list of all of this component's test-inputs, for all ports.
     */
    List getTestInputs();

    /**
     * Returns the list of test inputs for this component's port of the
     * given name.
     *
     * @param   portName    The name of the port whose test inputs are
     *                      desired.
     */
    List getTestInputs(String portName);

    /**
     * Returns this component's associated simulator.
     */
    atomicSimulator getSimulator();

    /**
     * See ViewableComponentBase.preferredLocation member variable.
     */
    Point getPreferredLocation();

    /**
     * See ViewableComponentBase.preferredLocation member variable.
     */
    void setPreferredLocation(Point location);

    /**
     * See ViewableComponentBase.layoutName member variable.
     */
    String getLayoutName();

    /**
     * See ViewableComponentBase.hidden member variable.
     */
    boolean isHidden();

    /**
     * See ViewableComponentBase.hidden member variable.
     */
    void setHidden(boolean hidden);
}