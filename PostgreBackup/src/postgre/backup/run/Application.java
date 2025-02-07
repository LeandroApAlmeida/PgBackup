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
 * Classe para acesso às funcionalidades do serviço de backup.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class Application {

    
    public static final int MESSAGE_NONE = 1;
    
    public static final int MESSAGE_INFO = 2;
    
    public static final int MESSAGE_WARNING = 3;
    
    public static final int MESSAGE_ERROR = 4;
    
    
    /**Ícone na barra de tarefas do Windows.*/
    private static TrayIcon trayIcon;
    
    
    /**
     * Exibir um balão de mensagem na barra de tarefas.
     * 
     * @param title título da mensagem.
     * 
     * @param text texto da mensagem.
     * 
     * @param type tipo da mensagem.
     */
    public static void displayMessage(String title, String text, int type) {
    
        MessageType messageType = MessageType.NONE;
        
        switch (type) {
            case MESSAGE_NONE: messageType = MessageType.NONE; break;
            case MESSAGE_INFO: messageType = MessageType.INFO; break;
            case MESSAGE_WARNING: messageType = MessageType.WARNING; break;
            case MESSAGE_ERROR: messageType = MessageType.ERROR; break;
        }
        
        trayIcon.displayMessage(title, text, messageType);
    
    }
    
    
    /**
     * Exibir uma mensagem de erro padrão.
     * 
     * @param title título da mensagem.
     * 
     * @param ex erro notificado na mensagem.
     */
    public static void showMessageError(String title, Throwable ex) {
        ErrorDialog.showException(null, title, ex);
    }
    
    
    /**
     * Exibe uma caixa de mensagem informando que houve erro no backup em
     * drive remoto.
     */
    public static void showNetworkErrorUI() {
    
        displayMessage(
            "Erro no acesso à unidade de rede", 
            "O backup do Banco de Dados não pode ser concluído.",
            MESSAGE_ERROR
        );
        
        WindowManager.showBackupErrorDialog();
    
    }
    
    
    /**
     * Exibir caixa de diálogo para backup em drive local.
     */
    public static void showLocalBackupUI() {
        WindowManager.showLocalBackupDialog();
    }

    
    /**
     * Exibir caixa de diálogo para backup manual do banco de dados.
     */
    public static void showManualBackupUI() {
        WindowManager.showManualBackupDialog();
    }
    
    
    /**
     * Obter o ícone padrão do aplicativo.
     * 
     * @return ícone padrão do aplicativo.
     */
    public static Image getDefaultIcon() {
        URL url = Application.class.getResource("/postgre/backup/forms/postgresql32.png");
        Image image = new ImageIcon(url).getImage();
        return image;
    }

    
    /**
     * Adiciona o ícone do aplicativo na barra de tarefas do Windows.
     */
    public static void addIconToSystemTray() {
    
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
        
            showMessageError("Erro!", ex);
            System.exit(1);
            
        }
        
    }
    
    
    /**
     * Atualiza os dados do ícone do aplicativo na barra de tarefas do Windows.
     */
    public static void updateSystemTrayIcon() {
        if (trayIcon != null) {
            String toolTipText = getToolTipText();
            trayIcon.setToolTip(toolTipText);
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
    
    
}