package eu.sportperformancemanagement.dataserver;

public class DataServer 
{
    public static void main( String[] args )
    {
    	(new Thread(new LocationListener())).start();
    	(new Thread(new QueueListener())).start();
    }
}
