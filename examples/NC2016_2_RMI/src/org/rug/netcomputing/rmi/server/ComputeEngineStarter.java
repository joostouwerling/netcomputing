package org.rug.netcomputing.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.rug.netcomputing.rmi.base.Compute;
import org.rug.netcomputing.rmi.base.RmiStarter;

/**
 * start the server component. this exposes the an implementation of the Compute
 * interface as a service over RMI An RMI server program needs to create the
 * initial remote objects and export them to the RMI runtime, which makes them
 * available to receive incoming remote invocations.
 * 
 * @author srasul
 * 
 */
public class ComputeEngineStarter extends RmiStarter {
	@Override
	public void start() {
		try {
			Compute engine = new ComputeEngine();
			System.out.println("Engine made");
			Compute engineStub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
			System.out.println("Object exported");
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(Compute.SERVICE_NAME, engineStub);
			System.out.println("Service registered");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ComputeEngineStarter();
	}
}