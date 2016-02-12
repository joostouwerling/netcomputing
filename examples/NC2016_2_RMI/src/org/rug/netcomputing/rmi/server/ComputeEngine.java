package org.rug.netcomputing.rmi.server;

import java.rmi.RemoteException;
import org.rug.netcomputing.rmi.base.Compute;
import org.rug.netcomputing.rmi.base.Task;

public class ComputeEngine implements Compute {

	@Override
	public <T> T executeTask(Task<T> t) throws RemoteException {
		System.out.println("got compute task: " + t);
		return t.execute();
	}
}