package postgre.backup.run;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseListener;
import java.util.Date;
import javax.swing.ImageIcon;
import postgre.backup.service.BackupMonitor;
import postgre.backup.service.BackupSchedule;
import postgre.backup.service.LastBackupInfo;
import postgre.backup.service.ServerSettings;
import postgre.backup.utils.StrUtils;

/**
 * Classe que representa o ícone da aplicação na barra de tarefas.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class ApplicationTrayIcon {
    
    
    /**Mensagem sem ícone.*/
    public static final int MESSAGE_NONE = 1;
    
    /**Mensagem de informação.*/
    public static final int MESSAGE_INFO = 2;
    
    /**Mensagem de alerta.*/
    public static final int MESSAGE_WARNING = 3;
    
    /**Mensagem de erro.*/
    public static final int MESSAGE_ERROR = 4;
    
    
    /**Ícone na barra de tarefas.*/
    private static TrayIcon trayIcon;
    
    
    /**
     * Exibir o balão de mensagem na barra de tarefas.
     * 
     * @param title título da mensagem.
     * 
     * @param text texto da mensagem.
     * 
     * @param type tipo da mensagem.
     */
    public static void displayMessage(String title, String text, int type) {
    
        TrayIcon.MessageType messageType = TrayIcon.MessageType.NONE;
        
        switch (type) {
            
            case MESSAGE_NONE -> messageType = TrayIcon.MessageType.NONE; 
            
            case MESSAGE_INFO ->  messageType = TrayIcon.MessageType.INFO;
            
            case MESSAGE_WARNING -> messageType = TrayIcon.MessageType.WARNING;
            
            case MESSAGE_ERROR -> messageType = TrayIcon.MessageType.ERROR;
        
        }
        
        trayIcon.displayMessage(title, text, messageType);
    
    }

    
    /**
     * Adiciona o ícone da aplicação à barra de tarefas do sistema operacional.
     */
    public static void addToSystemTray() {
    
        try {
            
            SystemTray systemTray = SystemTray.getSystemTray();
            
            TrayIcon[] trayIcons = systemTray.getTrayIcons();
            
            for (TrayIcon ti : trayIcons) {
                systemTray.remove(ti);
            }
            
            trayIcon = null;
            
            System.gc();
            
            PopupMenu popupMenu = new TrayIconPopupMenu();
            
            Image image = new ImageIcon(Application.class.getResource("postgres.png")).getImage();
            
            String toolTipText = getToolTipText();

            trayIcon = new TrayIcon(image, toolTipText, popupMenu);
            
            MouseListener mouseListener = new TrayIconMouseListener();
            
            trayIcon.addMouseListener(mouseListener);        
            
            systemTray.add(trayIcon);
        
        } catch (Exception ex) {  

            System.exit(1);
            
        }
        
    }
    
    
    /**
     * Obter os parâmetros sobre o serviço de backup para exibição.
     * 
     * @return parâmetros sobre o serviço de backup para exibição.
     */
    private static String getToolTipText() {
        
        ServerSettings serverSettings = new ServerSettings();

        LastBackupInfo lastBackupInfo = new LastBackupInfo();

        String toolTipText;

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

            toolTipText = sb.toString();

        } else {

            toolTipText = "Gerenciador de Backup do Banco de Dados";

        }

        return toolTipText;
        
    }
    
    
    /**
     * Atualiza os dados do ícone do aplicativo na barra de tarefas do Windows.
     */
    public static void updateToolTipText() {
        
        if (trayIcon != null) {
        
            String toolTipText = getToolTipText();
            
            trayIcon.setToolTip(toolTipText);
        
        }
    
    }
    
    
}
