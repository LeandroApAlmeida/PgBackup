package postgre.backup.run;

import dialogs.ErrorDialog;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Date;
import javax.swing.ImageIcon;
import postgre.backup.service.BackupMonitor;
import postgre.backup.service.BackupSchedule;
import postgre.backup.service.LastBackupInfo;
import postgre.backup.service.ServerSettings;
import postgre.backup.service.StrUtils;
import postgre.backup.forms.WindowManager;

/**
 * Classe que representa 
 * 
 * @author Leandro Aparecido de Almeida
 */
public class Application {

    
    public static final int MESSAGE_NONE = 1;
    
    public static final int MESSAGE_INFO = 2;
    
    public static final int MESSAGE_WARNING = 3;
    
    public static final int MESSAGE_ERROR = 4;
    
    
    private static TrayIcon trayIcon;
    
    
    public static void displayTrayMessage(String title, String text, int type) {
    
        MessageType messageType = MessageType.NONE;
        
        switch (type) {
            case MESSAGE_NONE: messageType = MessageType.NONE; break;
            case MESSAGE_INFO: messageType = MessageType.INFO; break;
            case MESSAGE_WARNING: messageType = MessageType.WARNING; break;
            case MESSAGE_ERROR: messageType = MessageType.ERROR; break;
        }
        
        trayIcon.displayMessage(title, text, messageType);
    
    }
    
    
    public static void showMessageError(String title, Throwable ex) {
        ErrorDialog.showException(null, title, ex);
    }
    
    
    public static void showNetworkErrorUI() {
    
        displayTrayMessage(
            "Erro no acesso à unidade de rede", 
            "O backup do Banco de Dados não pode ser concluído.",
            MESSAGE_ERROR
        );
        
        WindowManager.showBackupErrorDialog();
    
    }
    
    
    public static void showLocalBackupUI() {
        WindowManager.showLocalBackupDialog();
    }

    
    public static void showContextBackupUI() {
        if (new ServerSettings().getDriveType() == ServerSettings.REMOVABLE_DRIVE) {
            WindowManager.showLocalBackupDialog();
        } else {
            WindowManager.showNetworkBackupDialog();
        }
    }
    
    
    public static Image getDefaultIcon() {
    
        URL url = Application.class.getResource("/postgre/backup/forms/postgresql32.png");
        
        Image image = new ImageIcon(url).getImage();
        
        return image;
    
    }

    
    public static void updateTryIcon() {
    
        try {
        
            SystemTray systemTray = SystemTray.getSystemTray();
            
            TrayIcon[] tryIcons = systemTray.getTrayIcons();
            
            for (TrayIcon ti : tryIcons) {
                systemTray.remove(ti);
            }
            
            PopupMenu popupMenu = new TrayIconPopupMenu();
            
            Image image = new ImageIcon(Application.class.getResource("postgres.png")).getImage();
            
            ServerSettings serverSettings = new ServerSettings();
            
            LastBackupInfo lastBackupInfo = new LastBackupInfo();
            
            String tipText;
            
            if (serverSettings.getDatabase() != null) {
            
                BackupSchedule backupSchedule = new BackupSchedule();
                
                StringBuilder sb = new StringBuilder();
                
                sb.append("Conexão: ");
                sb.append(serverSettings.getHost());
                sb.append(":");
                sb.append(serverSettings.getPort());
                sb.append("\n");
                
                sb.append("Banco de dados: ");
                sb.append(serverSettings.getDatabase());
                sb.append("\n");
                
                
                sb.append("Último Backup: ");
                Date lastBackupdate = lastBackupInfo.getDate();
                if (lastBackupdate != null) {
                    sb.append(StrUtils.formatDate1(lastBackupdate));
                } else {
                    sb.append("[pendente]");
                }
                sb.append("\n");
                
                sb.append("Próximo Backup: ");
                
                if (backupSchedule.isAutomatic()) {
                    
                    Date date = BackupMonitor.getInstance().getNextBackupTime();
                    
                    if (date != null) {
                        sb.append(StrUtils.formatDate1(date));
                    } else {
                        sb.append("[Pendente]");
                    }
                    
                } else {
                    
                    sb.append("[backup manual]");
                    
                }
                
                tipText = sb.toString();
            
            } else {
            
                tipText = "Gerenciador de Backup do Banco de Dados";
            
            }
            
            trayIcon = null;
            
            System.gc();
            
            trayIcon = new TrayIcon(image, tipText, popupMenu);
            
            MouseListener mouseListener = new TrayIconMouseListener();
            
            trayIcon.addMouseListener(mouseListener);        
            
            systemTray.add(trayIcon);
        
        } catch (Exception ex) {  
        
            showMessageError("Erro!", ex);
            System.exit(1);
            
        }
        
    }
    
    
}