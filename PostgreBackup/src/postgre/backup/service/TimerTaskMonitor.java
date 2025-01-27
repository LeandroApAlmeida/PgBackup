package postgre.backup.service;

import java.util.TimerTask;

/**
 * Classe para recalculo da data para a realização do próximo backup.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class TimerTaskMonitor extends TimerTask {

    
    /**
     * O método run é disparado a cada 10 minutos, forçando o recalculo da data
     * para a realização do próximo backup.
     */
    @Override
    public void run() {
        BackupMonitor.getInstance().start(false);
    }
    
    
}