package nl.rug.netcompuring.sportperformancemanagement;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MonitorLocationService extends Service {
    public MonitorLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
