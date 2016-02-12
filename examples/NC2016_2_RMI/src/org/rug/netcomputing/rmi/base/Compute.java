package org.rug.netcomputing.rmi.base;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * The Compute interface's executeTask method returns the result of the
 * execution of the Task instance passed to it. The executeTask method has its
 * own type parameter, T, that associates its own return type with the result
 * type of the passed Task instance. Different kinds of tasks can be run by a
 * Compute object as long as they are implementations of the Task type. The
 * classes that implement this interface can contain any data needed for the
 * computation of the task and any other methods needed for the computation.
 */
public interface Compute extends Remote {
    
    public static final String SERVICE_NAME = "ComputeEngine";
    
    <T> T executeTask(Task<T> t) throws RemoteException;
}