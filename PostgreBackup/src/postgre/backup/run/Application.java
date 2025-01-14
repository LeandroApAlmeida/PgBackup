package postgre.backup.run;

import dialogs.ErrorDialog;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import postgre.backup.classes.BackupMonitor;
import postgre.backup.classes.BackupSchedule;
import postgre.backup.classes.ServerSettings;
import postgre.backup.classes.StrUtils;
import postgre.backup.forms.WindowsManager;

public class Application {

    public static final int MESSAGE_NONE = 1;
    public static final int MESSAGE_INFO = 2;
    public static final int MESSAGE_WARNING = 3;
    public static final int MESSAGE_ERROR = 4;
    private static TrayIcon trayIcon;
    
    static {
        try {System.setProperty("root_dir", new File(Application.class
        .getProtectionDomain().getCodeSource().getLocation().toURI())
        .getParent());} catch (Exception ex) {}
        System.setProperty("file_extension", ".pgback");
    }
    
    public static void main(String[] args)  {
        try {
            final File file = new File(System.getProperty("root_dir") + "\\pgbackup.lock");
            final RandomAccessFile raFile = new RandomAccessFile(file, "rw");
            final FileLock fLock = raFile.getChannel().tryLock();
            if (fLock != null) {
                Runtime.getRuntime().addShutdownHook(
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                fLock.release();
                                raFile.close();
                            } catch (IOException ex) {                            
                            }
                        }
                    }
                ); 
                java.awt.EventQueue.invokeLater(() -> {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        BackupMonitor.getInstance().start(true);
                        Application.updateTryIcon();
                    } catch (Exception ex) {
                        showMessageError("Erro na inicialização!", ex);
                        System.exit(1);
                    }
                });
            } else {
                System.exit(1);
            }
        } catch (Exception ex) {
            showMessageError("Erro na inicialização!", ex);
            System.exit(1);
        }
    }
    
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
        displayTrayMessage("Erro no acesso à unidade de rede", "O backup do Banco de Dados não pode ser concluído.",
        MESSAGE_ERROR);
        WindowsManager.showBackupErrorDialog();
    }
    
    public static void showManualBackupUI() {
        displayTrayMessage("Backup do Banco de Dados", 
        "Conecte um dispositivo removível de memória a uma porta USB para fazer o Backup.",
        MESSAGE_WARNING);
        WindowsManager.showManualBackupDialog();
    }

    public static void showContextBackupUI() {
        if (ServerSettings.getInstance().getDriveType() == ServerSettings.REMOVABLE_DRIVE) {
            WindowsManager.showManualBackupDialog();
        } else {
            WindowsManager.showNetworkBackupDialog();
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
            ServerSettings serverSettings = ServerSettings.getInstance();
            String tipText;
            if (serverSettings.getDatabase() != null) {
                BackupSchedule backupSchedule = BackupSchedule.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append("Postgre Server: ");
                sb.append(serverSettings.getHost());
                sb.append(":");
                sb.append(serverSettings.getPort());
                sb.append("\n");
                sb.append("Database: ");
                sb.append(serverSettings.getDatabase());
                sb.append("\n");
                sb.append("Backup Automático: ");
                sb.append(backupSchedule.isActivated() ? "true" : "false");
                sb.append("\n");
                sb.append("Próximo Backup: ");
                Date date = BackupMonitor.getInstance().getNextBackupTime();
                if (date != null) {
                    sb.append(StrUtils.formatDate1(date));
                } else {
                    sb.append("[Pendente]");
                }
                tipText = sb.toString();
            } else {
                tipText = "Gerenciador de Backup do Banco de Dados";
            }
            trayIcon = null;
            System.gc();
            trayIcon = new TrayIcon(image, tipText, popupMenu);
            //MouseListener mouseListener = new TrayIconMouseListener(popupMenu);
            //trayIcon.addMouseListener(mouseListener);        
            systemTray.add(trayIcon);
        } catch (Exception ex) {  
            showMessageError("Erro!", ex);
            System.exit(1);
        }
    }
    
}
