package postgre.backup.forms;

import java.awt.Dialog;

public class WindowManager {
    
    
    private static boolean windowVisible;
    
    private static Dialog owner;

    
    private WindowManager() {
    }

    
    public static void showConfigDialog() {
        if (!windowVisible) {
            windowVisible = true;
            owner = new ConfigDialog();
            owner.setVisible(true);
            windowVisible = false;
        }
    }
    
    
    public static void showRestoreDialog() {
        if (!windowVisible) {
            windowVisible = true;
            owner = new RestoreDialog();
            owner.setVisible(true);
            windowVisible = false;
        }
    }
    
    
    public static void showNetworkBackupDialog() {
        if (!windowVisible) {
            windowVisible = true;
            owner = new NetworkBackupDialog();
            owner.setVisible(true);
            windowVisible = false;
        }
    }
    
    
    public static void showAboutDialog() { 
        if (!windowVisible) {
            windowVisible = true;
            owner = new AboutDialog();
            owner.setVisible(true);
            windowVisible = false;
        }
    }
    
    
    public static void showBackupErrorDialog() {
        if (owner != null) owner.setVisible(false);
        windowVisible = true;
        owner = new BackupErrorDialog();
        owner.setVisible(true);
        windowVisible = false;
    }
    
    
    public static void showManualBackupDialog() {
        if (!windowVisible) {
            windowVisible = true;
            owner = new ManualBackupDialog();
            owner.setVisible(true);
            windowVisible = false;
        }
    }
    
    
    public static void showManualBackupDialogUnchecked() {
        if (owner != null) owner.setVisible(false);
        windowVisible = true;
        owner = new ManualBackupDialog();
        owner.setVisible(true);
        windowVisible = false;
    }
    
    
    public static Dialog getOwner() {
        return owner;
    }

    
}