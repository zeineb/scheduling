package org.objectweb.proactive.core.component.controller;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.Type;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;

import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.type.ProActiveTypeFactory;

import java.io.Serializable;


/**
 * Base class for all component controllers.
 *
 * @author Matthieu Morel
 *
 */
public abstract class ProActiveController implements Interface, Serializable {
    private Component owner;
    private boolean isInternal = true;
    private InterfaceType interfaceType;
    private String controllerName;

    /**
     * Constructor for ProActiveController.
     * @param owner the component that wants this controller
     * @param controllerName the name of the controller (the list of controller names
     * is in the {@link Constants} class.
     */
    public ProActiveController(Component owner, String controllerName) {
        this.owner = owner;
        //this.interfaceType = interfaceType;
        this.controllerName = controllerName;
        try {
            if (controllerName.equals(Constants.CONTENT_CONTROLLER)) {
                interfaceType = ProActiveTypeFactory.instance().createFcItfType(Constants.CONTENT_CONTROLLER,
                        ProActiveContentController.class.getName(),
                        TypeFactory.SERVER, TypeFactory.MANDATORY,
                        TypeFactory.SINGLE);
            } else if (controllerName.equals(Constants.BINDING_CONTROLLER)) {
                interfaceType = ProActiveTypeFactory.instance().createFcItfType(Constants.BINDING_CONTROLLER,
                        ProActiveBindingController.class.getName(),
                        TypeFactory.SERVER, TypeFactory.MANDATORY,
                        TypeFactory.SINGLE);
            } else if (controllerName.equals(Constants.LIFECYCLE_CONTROLLER)) {
                interfaceType = ProActiveTypeFactory.instance().createFcItfType(Constants.LIFECYCLE_CONTROLLER,
                        ProActiveLifeCycleController.class.getName(),
                        TypeFactory.SERVER, TypeFactory.MANDATORY,
                        TypeFactory.SINGLE);
            } else if (controllerName.equals(
                        Constants.COMPONENT_PARAMETERS_CONTROLLER)) {
                interfaceType = ProActiveTypeFactory.instance().createFcItfType(Constants.COMPONENT_PARAMETERS_CONTROLLER,
                        ProActiveComponentParametersController.class.getName(),
                        TypeFactory.SERVER, TypeFactory.MANDATORY,
                        TypeFactory.SINGLE);
            } else {
                throw new ProActiveRuntimeException("unknown controller");
            }
        } catch (InstantiationException e) {
            throw new ProActiveRuntimeException("cannot create component or interface type",
                e);
        }
    }

    /**
     * see {@link org.objectweb.fractal.api.Interface#getFcItfOwner()}
     */
    public Component getFcItfOwner() {
        return owner;
    }

    /**
     * see {@link org.objectweb.fractal.api.Interface#isFcInternalItf()}
     */
    public boolean isFcInternalItf() {
        return isInternal;
    }

    /**
     * see {@link org.objectweb.fractal.api.Interface#getFcItfName()}
     */
    public String getFcItfName() {
        return controllerName;
    }

    /**
     * see {@link org.objectweb.fractal.api.Interface#getFcItfType()}
     */
    public Type getFcItfType() {
        return interfaceType;
    }

    /**
     * some control operations are to be performed while the component is stopped
     */
    protected void checkLifeCycleIsStopped() throws IllegalLifeCycleException {
        try {
            if (!((LifeCycleController) getFcItfOwner().getFcInterface(Constants.LIFECYCLE_CONTROLLER)).getFcState()
                      .equals(LifeCycleController.STOPPED)) {
                throw new IllegalLifeCycleException(
                    "this control operation should be performed while the component is stopped");
            }
        } catch (NoSuchInterfaceException nsie) {
            throw new ProActiveRuntimeException(
                "life cycle controller interface not found");
        }
    }

    //	// -------------------------------------------------------------------------
    //	// Implementation of the Name interface
    //	// -------------------------------------------------------------------------
    //
    //	public NamingContext getNamingContext() {
    //		return null;
    //	}
    //
    //	public byte[] encode() throws NamingException {
    //		throw new NamingException("Unsupported operation");
    //	}
}
