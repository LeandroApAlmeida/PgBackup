package postgre.backup.run;

import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 * Classe que implementa o tratador de eventos associado ao clique no 
 * {@link java.awt.TrayIcon} da aplicação. Ao clicar no ícone da aplicação na
 * área de noficações do Windows, por exemplo, será exibida a tela do monitor
 * de processos do Curumim Server.
 */
public class TrayIconMouseListener implements MouseListener {
    
    private final PopupMenu popupMenu;

    public TrayIconMouseListener(PopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Frame frame = new Frame("");
            frame.setVisible(true);
            frame.add(popupMenu);
            frame.setType(Window.Type.UTILITY);
            popupMenu.show(frame, e.getXOnScreen(), e.getYOnScreen());
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