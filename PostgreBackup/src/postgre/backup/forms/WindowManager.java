package postgre.backup.forms;

import java.awt.Dialog;

/**
 * Classe para o gerenciamento de janelas do aplicativo. Somente uma janela pode
 * estar visível em determinado momento.
 * 
 * @author Leandro Aparecido de Almeida
 */
public final class WindowManager {
    
    
    /**Diálogo em exibição.*/
    private static Dialog dialog;
    
    
    private WindowManager() {    
    }

    
    /**
     * Exibir a janela para a configuração do serviço de backup.
     */
    public synchronized static void showSettingsDialog() {
        if (dialog == null) {
            dialog = new SettingsDialog();
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
     * Exibir a janela para o backup manual em drive de rede.
     */
    public synchronized static void showNetworkBackupDialog() {
        if (dialog == null) {
            dialog = new NetworkBackupDialog();
            dialog.setVisible(true);
            dialog = null;
            System.gc();
        }
    }
    
    
    /**
     * Exibir a janela de erro no backup em drive de rede.
     */
    public synchronized static void showBackupErrorDialog() {
        if (dialog != null) dialog.setVisible(false);
        dialog = null;
        System.gc();
        dialog = new NetworkBackupErrorDialog();
        dialog.setVisible(true);
        dialog = null;
        System.gc();
    }
    
    
    /**
     * Exibir a janela de backup manual em drive local.
     */
    public synchronized static void showLocalBackupDialog() {
        if (dialog == null) {
            dialog = new LocalBackupDialog();
            dialog.setVisible(true);
            dialog = null;
            System.gc();
        }
    }
    
    
    /**
     * Exibir a janela de backup manual em drive local, removendo a janela atual
     * em exibição.
     */
    public synchronized static void showLocalBackupDialog2() {
        if (dialog != null) dialog.setVisible(false);
        dialog = null;
        System.gc();
        dialog = new LocalBackupDialog();
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

    
}