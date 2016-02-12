package org.rug.netcomputing.rmi.client;
import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.rug.netcomputing.rmi.base.Compute;
import org.rug.netcomputing.rmi.base.RmiStarter;
/**
 * RMI Client : get the RMI Compute service and send a task to compute PI to N
 * digits
 * START THE RMIREGISTRY.EXE from your JAVA Runtime/bin directory first
 * and make sure the remote object classes can be found in the classpath of the registry.
 * @author srasul
 */
public class LaunchCalcPItask extends RmiStarter {
	@Override
	public void start() {
		try {
			System.out.println("Started");
			Registry registry = LocateRegistry.getRegistry();
			System.out.println("Found registery");
			Compute compute = (Compute) registry.lookup(Compute.SERVICE_NAME);
			CalcPI task = new CalcPI(50);
			BigDecimal pi = compute.executeTask(task);
			System.out.println("computed pi: " + pi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new LaunchCalcPItask();
	}
}
