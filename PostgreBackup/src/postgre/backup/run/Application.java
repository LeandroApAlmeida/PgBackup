package postgre.backup.run;

import dialogs.ErrorDialog;
import java.awt.Dialog;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import postgre.backup.forms.AboutDialog;
import postgre.backup.forms.ManualBackupDialog;
import postgre.backup.forms.NetworkBackupErrorDialog;
import postgre.backup.forms.RestoreDialog;
import postgre.backup.forms.SettingsDialog;

/**
 * Classe para acesso às funcionalidades do serviço de backup.
 * 
 * @author Leandro Aparecido de Almeida
 */
public final class Application {
    
    
    private static Dialog dialog;
    
    
    /**
     * Exibir a janela para a configuração do serviço de backup.
     */
    public synchronized static void showSettingsDialog() {
        
        if (dialog == null) {
        
            dialog = new SettingsDialog() ;
            
            dialog.setVisible(true);
            
            dialog = null;
            
            System.gc();
        
        }
    
    }
    
    
    /**
     * Exibir a janela para a restauração do banco de dados.
     */
    public synchronized static void showRestoreDialog() {
        
        if (dialog == null) {
        
            dialog = new RestoreDialog();
            
            dialog.setVisible(true);
            
            dialog = null;
            
            System.gc();
        
        }
    
    }
    
    
    /**
     * Exibir a janela de erro no backup em drive de rede.
     */
    public synchronized static void showBackupErrorDialog() {
        
        ApplicationTrayIcon.displayMessage(
            "Erro no acesso à unidade de rede",
            "O backup do Banco de Dados não pode ser concluído.",
            ApplicationTrayIcon.MESSAGE_ERROR
        );
        
        if (dialog != null) dialog.setVisible(false);
        
        dialog = null;
        
        System.gc();
        
        dialog = new NetworkBackupErrorDialog();
        
        dialog.setVisible(true);
        
        dialog = null;
        
        System.gc();
        
    }
    
    
    /**
     * Exibir a janela de backup manual.
     */
    public synchronized static void showManualBackupDialog() {
        
        if (dialog == null) {
        
            dialog = new ManualBackupDialog();
            
            dialog.setVisible(true);
            
            dialog = null;
            
            System.gc();
        
        }
    
    }
    
    
    /**
     * Exibir a janela de backup manual em drive local, removendo a janela atual
     * em exibição.
     */
    public synchronized static void showManualBackupDialog2() {
        
        if (dialog != null) dialog.setVisible(false);
        
        dialog = null;
        
        System.gc();
        
        dialog = new ManualBackupDialog();
        
        dialog.setVisible(true);
        
        dialog = null;
        
        System.gc();
    
    }
    
    
    /**
     * Exibir a janela de créditos da versão.
     */
    public synchronized static void showAboutDialog() { 
        
        if (dialog == null) {
        
            dialog = new AboutDialog();
            
            dialog.setVisible(true);
            
            dialog = null;
            
            System.gc();
        
        }
    
    }
    
    
    /**
     * Exibir uma mensagem de erro padrão.
     * 
     * @param title título da mensagem.
     * 
     * @param ex erro notificado na mensagem.
     */
    public synchronized static void showErrorDialog(String title, Throwable ex) {
    
        ErrorDialog.showException(null, title, ex);
    
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
    
    
}