package postgre.backup.classes;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * Classe responsável pelo monitoramento do backup automático do banco de dados.
 * Ela é reponsável por acionar o backup sempre nas datas e horários definidos
 * para que eles aconteçam (o controle é por dias da semana).
 * @author Leandro Aparecido de Almeida
 */
public class BackupMonitor {
    
    /**Instância única da classe.*/
    private static final BackupMonitor instance = new BackupMonitor();
    /**Parâmetros para a realização do backup automático.*/
    private final BackupSchedule backupSchedule = BackupSchedule.getInstance();
    /**Controle de execução do backup no horário programado.*/
    private Timer backupTimer;
    /**Controle para recalculo periódico do tempo para o próximo backup.*/
    private Timer monitorTimer;
    private Date nextBackupTime = null;

    /**Constructor private.*/
    private BackupMonitor() {
    }

    /**
     * Calcular o tempo restante para o próximo backup automático do banco de
     * dados PostgreSQL e start as threads para o monitoramento.
     * @param startMonitor status de start o controlar de recalculo
     * do tempo para o próximo backup automático.
     */
    public synchronized void start(boolean startMonitor) {      
        if (backupSchedule.isActivated()) {
            stop(false);
            backupTimer = new Timer();
            List<Integer> weekDays = new ArrayList<>();
            if (backupSchedule.isSunday()) weekDays.add(1);
            if (backupSchedule.isMonday()) weekDays.add(2);
            if (backupSchedule.isTuesday()) weekDays.add(3);
            if (backupSchedule.isWednesday()) weekDays.add(4);
            if (backupSchedule.isThursday()) weekDays.add(5);
            if (backupSchedule.isFriday()) weekDays.add(6);
            if (backupSchedule.isSaturday()) weekDays.add(7);
            List<Time> times = backupSchedule.getBackupTimesList();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); 
            Time currentTime = new Time(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
            );
            Time nextSchedule = currentTime;
            boolean backupToday = false;
            if (weekDays.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                for (Time time : times) {
                    if (currentTime.getTime() <= time.getTime()) {
                        //O backup será nesta mesma data, no primeiro horário
                        //da lista, após o horário atual do sistema.
                        nextSchedule = time;
                        backupToday = true;
                        break;
                    }
                }
            }          
            if (!backupToday) {
                //O backup será realizado na próxima data definida, no primeiro
                //horário da lista.
                for (int i = 1; i <= 7; i++) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(calendar.getTime());
                    c.add(Calendar.DAY_OF_MONTH, i);
                    if (weekDays.contains(c.get(Calendar.DAY_OF_WEEK))) {
                        calendar.add(Calendar.DAY_OF_MONTH, i);
                        break;
                    }
                }
                nextSchedule = times.get(0);
            }
            calendar.set(Calendar.HOUR_OF_DAY, nextSchedule.getHours());
            calendar.set(Calendar.MINUTE, nextSchedule.getMinutes());
            calendar.set(Calendar.SECOND, nextSchedule.getSeconds());
            nextBackupTime = new Date(calendar.getTimeInMillis());
            //Calcula o tempo até o backup e inicia a thread.
            long schedule = calendar.getTimeInMillis() - System.currentTimeMillis();
            backupTimer.schedule(new TimerTaskBackup(), schedule);
            //O controlador ativará o recalculo a cada 10 minutos.
            if (startMonitor) {
                monitorTimer = new Timer();
                monitorTimer.schedule(
                    new TimerTaskMonitor(),
                    new Date(),
                    10*(60000)
                );
            }
        } else {
            nextBackupTime = null;
            stop(true);
        }
    }
    
    /**
     * Parar as threads do monitor de backup.
     * @param stopMonitor status de stop a thread do controlador de
     * recalculo do tempo para o próximo backup automático.
     */
    public synchronized void stop(boolean stopMonitor) {
        //Para a thread do controlador.
        if (stopMonitor) {
            if (monitorTimer != null) {
                monitorTimer.cancel();
                monitorTimer = null;
            }
        }
        //Para a thread do monitor.
        if (backupTimer != null) {
            backupTimer.cancel();
            backupTimer = null;
        }
    }

    public Date getNextBackupTime() {
        return nextBackupTime;
    }
    
    /**Obter a instância única da classe.*/
    public static BackupMonitor getInstance() {
        return instance;
    }
    
}