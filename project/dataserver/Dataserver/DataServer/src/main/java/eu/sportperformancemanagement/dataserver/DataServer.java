package eu.sportperformancemanagement.dataserver;


public class DataServer 
{
    public static void main( String[] args )
    {
    	(new Thread(new LocationListener())).start();
    	/*LocationDAO locdao = new LocationDAO();
    	Location[] locs = locdao.findLocations(1, 1);
    	for (Location loc : locs)
    		System.out.println(loc.getLatitude() + " " + loc.getLongitude());*/
    }
}
