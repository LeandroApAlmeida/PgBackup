package postgre.backup.run;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Classe que implementa o tratador de eventos associado ao clique no 
 * ícone da aplicação.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class TrayIconMouseListener implements MouseListener {
    
    
    /**
     * Evento disparado ao clicar no ícone da aplicação. Neste caso, exibe a 
     * interface gráfica para backup manual do banco de dados, em drive de rede
     * ou drive removível.
     * 
     * @param e dados do evento.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Application.showContextBackupUI();
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