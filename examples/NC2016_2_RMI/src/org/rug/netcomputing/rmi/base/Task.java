package org.rug.netcomputing.rmi.base;
/**
 * The Task interface has a type parameter, T, which represents the result type
 * of the task's computation. This is an example of generic types, implemented
 * by your own. Think of T as a placeholder that will be replaced by a concrete
 * type. RMI uses the Java object serialization mechanism to transport objects
 * by value between Java virtual machines. For an object to be considered
 * serializable, its class must implement the java.io.Serializable marker
 * interface. Therefore, classes that implement the Task interface must also
 * implement Serializable, as must the classes of objects used for task results.
 */
public interface Task<T> {
    T execute();
}