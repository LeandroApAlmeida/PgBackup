package postgre.backup.run;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Classe que implementa o tratador de eventos associado ao clique no 
 * {@link java.awt.TrayIcon} da aplicação. Ao clicar no ícone da aplicação na
 * área de noficações do Windows, por exemplo, será exibida a tela do monitor
 * de processos do Curumim Server.
 */
public class TrayIconMouseListener implements MouseListener {
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Application.showManualBackupUI();
        }
    }
    
    
    @Override 
    public void mousePressed(MouseEvent e) {}
    @Override 
    public void mouseReleased(MouseEvent e) {}
    @Override 
    public void mouseEntered(MouseEvent e) {}
    @Override 
    public void mouseExited(MouseEvent e) {}
    
    
}