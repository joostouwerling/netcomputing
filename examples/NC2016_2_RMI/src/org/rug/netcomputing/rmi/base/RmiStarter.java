package org.rug.netcomputing.rmi.base;
/**
 * class to do some common things for client & server to get RMI working
 * @author srasul
 */
public abstract class RmiStarter {
    public RmiStarter() {
        System.setProperty("java.security.policy", PolicyFileLocator.getLocationOfPolicyFile());
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        start();
    }
    /**
     * extend this class and do RMI handling here
     */
    public abstract void start();
}
