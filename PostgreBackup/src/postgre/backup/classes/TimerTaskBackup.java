package postgre.backup.classes;

import java.util.TimerTask;
import postgre.backup.run.Application;

public class TimerTaskBackup extends TimerTask {
    
    
    private final ServerSettings serverSettings = ServerSettings.getInstance();
    
    private final BackupMonitor backupMonitor = BackupMonitor.getInstance();
    
    
    @Override
    public void run() {
        
        if (serverSettings.getDriveType() == ServerSettings.NETWORK_DRIVE) {
            
            backupMonitor.stop(true);
            
            try {
                BackupManager.doBackup(serverSettings.getNetworkDrive());
            } catch (Exception ex) {
                Application.showNetworkErrorUI();
            }
            
            backupMonitor.start(true);
        
        } else {
        
            backupMonitor.stop(true);
            
            Application.showManualBackupUI();
            
            backupMonitor.start(true);
            
        }
        
        Application.updateTryIcon();
        
    }

    
}