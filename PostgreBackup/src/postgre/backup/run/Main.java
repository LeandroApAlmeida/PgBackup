package postgre.backup.run;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import javax.swing.UIManager;
import postgre.backup.service.BackupMonitor;
import static postgre.backup.run.Application.showMessageError;

/**
 *
 * @author leandro
 */
public class Main {
    
    
    static {
    
        try {
            System.setProperty(
                "root_dir",
                new File(
                    Application.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                ).getParent()
            );
        } catch (Exception ex) {
        }
        
        System.setProperty("file_extension", ".pgback");
    
    }
    
    
    public static void main(String[] args) {
        
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
                                file.delete();
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
    
    
}