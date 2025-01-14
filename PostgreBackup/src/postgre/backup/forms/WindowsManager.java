package postgre.backup.forms;

public class WindowsManager {
    
    private static boolean windowVisible;

    private WindowsManager() {
    }

    public static void showConfigDialog() {
        if (!windowVisible) {
            windowVisible = true;
            new ConfigDialog().setVisible(true);
            windowVisible = false;
        }
    }
    
    public static void showRestoreDialog() {
        if (!windowVisible) {
            windowVisible = true;
            new RestoreDialog().setVisible(true);
            windowVisible = false;
        }
    }
    
    public static void showNetworkBackupDialog() {
        if (!windowVisible) {
            windowVisible = true;
            new NetworkBackupDialog().setVisible(true);
            windowVisible = false;
        }
    }
    
    public static void showAboutDialog() { 
        if (!windowVisible) {
            windowVisible = true;
            new AboutDialog().setVisible(true);
            windowVisible = false;
        }
    }
    
    public static void showBackupErrorDialog() {
        if (!windowVisible) {
            windowVisible = true;
            new BackupErrorDialog().setVisible(true);
            windowVisible = false;
        }
    }
    
    public static void showManualBackupDialog() {
        if (!windowVisible) {
            windowVisible = true;
            new ManualBackupDialog().setVisible(true);
            windowVisible = false;
        }
    }

}