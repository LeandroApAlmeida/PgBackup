package postgre.backup.run;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import javax.swing.UIManager;
import postgre.backup.service.BackupMonitor;

/**
 * Classe que implementa o ponto de entrada do programa.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class Main {
    
    
    static {
    
        try {
            
            // Adiciona o diretório raiz do programa.
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
        
        // Adiciona a extensão do arquivo.
        System.setProperty("file_extension", ".pgback");
    
    }
    
    
    /**
     * Ponto de entrada do programa. Pode haver apenas uma instância em execução,
     * e este controle é feito aqui.
     * 
     * <br>
     * 
     * Quando o programa é iniciado, ele aciona o monitor de backup automático
     * e adiciona o ícone do aplicativo na barra de tarefas do Windows.
     * 
     * @param args o programa não trata parâmetros.
     */
    public static void main(String[] args) {
        
        try {
            
            // O controle de instância única se dá pela obtenção de acesso ao
            // arquivo pgbackup.lock, que está presente no diretório raiz do
            // programa. Caso tenha sucesso em obter o bloqueio do arquivo, o
            // programa é inicializado, caso contrário, não.
            
            final File file = new File(System.getProperty("root_dir") + "\\pgbackup.lock");
            
            final RandomAccessFile raFile = new RandomAccessFile(file, "rw");
            
            final FileLock fLock = raFile.getChannel().tryLock();
            
            if (fLock != null) {
                
                // Ao sair do programa, libera o bloqueio do arquivo e exclui
                // este arquivo.
                
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

                        // Define a aparência dos controles de interface gráfica
                        // do usuário de acordo com a aparência da plataforma.
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            
                        // Inicia o monitor de backup automático. 
                        BackupMonitor.getInstance().start(true);

                        // Adiciona o ícone do aplicativo na barra de tarefas
                        // do Windows.
                        ApplicationTrayIcon.addToSystemTray();

                    } catch (Exception ex) {

                        Application.showErrorDialog(
                            "Erro na inicialização!",
                            ex
                        );

                        System.exit(1);

                    }

                });
            
            } else {
            
                System.exit(1);
            
            }
        
        } catch (Exception ex) {
        
            Application.showErrorDialog(
                "Erro na inicialização!",
                ex
            );
            
            System.exit(1);
            
        }
        
    }
    
    
}