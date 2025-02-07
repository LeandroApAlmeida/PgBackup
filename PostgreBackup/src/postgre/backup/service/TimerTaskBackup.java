package postgre.backup.service;

import java.util.TimerTask;
import postgre.backup.run.Application;

/**
 * Classe responsável pela execução de um backup automático.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class TimerTaskBackup extends TimerTask {
    
    
    /**Parâmetros para configuração do serviço de backup.*/
    private final ServerSettings serverSettings = new ServerSettings();
    
    /**Instancia do monitor de backup automático.*/
    private final BackupMonitor backupMonitor = BackupMonitor.getInstance();
    
    
    /**
     * O método run é disparado assim que é atingido o horário de fazer o
     * backup, dentro de um timer na instância de BackupMonitor.
     */
    @Override
    public void run() {
        
        if (serverSettings.getDriveType() == ServerSettings.NETWORK_DRIVE) {
            
            // Neste caso, o backup será feito em um determinado drive de rede.
            
            backupMonitor.stop(true);
            
            try {
                new BackupManager().doBackup(serverSettings.getNetworkDrive());
            } catch (Exception ex) {
                Application.showNetworkErrorUI();
            }
            
            backupMonitor.start(true);
        
        } else {
            
            // Neste caso, o backup será feito em um drive USB local.
        
            backupMonitor.stop(true);
            
            Application.showLocalBackupUI();
            
            backupMonitor.start(true);
            
        }
        
        Application.updateSystemTrayIcon();
        
    }

    
}