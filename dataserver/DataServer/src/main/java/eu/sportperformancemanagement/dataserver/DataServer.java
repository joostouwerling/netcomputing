package eu.sportperformancemanagement.dataserver;

/**
 * This class acts as the starting point of the Data Server. 
 * It launches two threads:
 * 1) a LocationListener, listening for incoming location packets.
 * 2) a QueueListener, which listens on a RabbitMQ queue for incoming
 * 	  requests for locations for a certain match/player.
 * 
 * If either one of them fails to initialize (MySQL not available,
 * RabbitMQ not available, etc) the thread immediately returns 
 * and an error is logged, with details about the reason it did
 * not initialize.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class DataServer 
{
    public static void main( String[] args )
    {
    	(new Thread(new LocationListener())).start();
    	(new Thread(new QueueListener())).start();
    }
}
