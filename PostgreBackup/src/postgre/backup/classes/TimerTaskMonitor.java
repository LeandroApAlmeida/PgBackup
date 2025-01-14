package postgre.backup.classes;

import java.util.TimerTask;

public class TimerTaskMonitor extends TimerTask {

    @Override
    public void run() {
        BackupMonitor.getInstance().start(false);
    }
    
}
