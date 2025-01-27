package postgre.backup.run;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import postgre.backup.service.ServerSettings;
import postgre.backup.forms.WindowManager;

/**
 * Popup menu associado ao {@link java.awt.TrayIcon} da aplicação.
 * 
 * <br>
 * 
 * O objetivo desta subclasse é o de criar os itens do popup menu e associar os 
 * eventos aos mesmos antes de adicioná-lo ao <b>TrayIcon</b>.
 */
public class TrayIconPopupMenu extends java.awt.PopupMenu {

    
    /**
     * Constructor padrão. Cria os itens do popup menu da aplicação.
     */
    public TrayIconPopupMenu() {
        createPopupMenu();
    }
    
    
    /**
     * Cria os itens do popup menu.
     */
    private void createPopupMenu() {
        addDoBackupMenuItem();
        addSeparator();
        addDoRestoreMenuItem();
        addSeparator();
        addAboutMenuItem();
        addSeparator();
        addConfigMenuItem();
        addSeparator();
        addExitMenuItem();
    }
    
    
    /**
     * Adicionar o item de menu "Configurar...".
     */
    private void addConfigMenuItem() {
        
        MenuItem menuItem = new MenuItem("Configurar...");
        
        menuItem.addActionListener((ActionEvent e) -> {
            WindowManager.showConfigDialog();
        });
        
        this.add(menuItem);
    
    }
    
    
    /**
     * Adicionar o item de menu "Fazer o Backup".
     */
    private void addDoBackupMenuItem() {
        
        MenuItem menuItem = new MenuItem("Fazer o Backup");
        
        menuItem.addActionListener((ActionEvent e) -> {
            Application.showContextBackupUI();
        });
        
        this.add(menuItem);
    
    }
    
    
    /**
     * Adicionar o item de menu "Restaurar o Backup".
     */
    private void addDoRestoreMenuItem() {
    
        MenuItem menuItem = new MenuItem("Restaurar o Backup");
        
        menuItem.addActionListener((ActionEvent e) -> {
            WindowManager.showRestoreDialog();
        });
        
        this.add(menuItem);
    
    }
    
    
    /**
     * Adicionar o item de menu "Sobre a Versão".
     */
    private void addAboutMenuItem() {
        
        MenuItem menuItem = new MenuItem("Sobre a Versão");
        
        menuItem.addActionListener((ActionEvent e) -> {
            WindowManager.showAboutDialog();
        });
        
        this.add(menuItem);
    
    }
    
    
    /**
     * Adicionar o item de menu "Sair".
     */
    private void addExitMenuItem() {
        
        MenuItem menuItem = new MenuItem("Sair");
        
        menuItem.addActionListener((ActionEvent e) -> {
        
            int opt = JOptionPane.showConfirmDialog(
                null,
                "Sair do gerenciador de backup de \"" + 
                new ServerSettings().getDatabase() + "\"?",
                "Atenção!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (opt == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            
        });
        
        this.add(menuItem);
    
    }
   
    
}