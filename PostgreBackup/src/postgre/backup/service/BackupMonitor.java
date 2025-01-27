package postgre.backup.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * Classe responsável pelo backup automático do banco de dados. Ela é responsável
 * por acionar o mecanismo do agendador para executar nos dias e horários definidos.
 * 
 * @author Leandro Aparecido de Almeida
 */
public final class BackupMonitor {
    
    
    /**Instância única da classe.*/
    private static final BackupMonitor instance = new BackupMonitor();
    
    /**Controle de execução do backup no horário programado.*/
    private Timer backupTimer;
    
    /**Controle para recalculo periódico do tempo para o próximo backup.*/
    private Timer monitorTimer;
    
    /**Data do próximo backup.*/
    private Date nextBackupTime = null;

    
    /**Constructor private.*/
    private BackupMonitor() {
    }

    
    /**
     * Calcular o tempo restante para o próximo backup automático do banco de
     * dados PostgreSQL e iniciar as threads para o monitoramento.
     * 
     * @param startMonitor status de ativação do controlador de recalculo do tempo
     * para o próximo backup automático.
     */
    public synchronized void start(boolean startMonitor) {
        
        BackupSchedule backupSchedule = new BackupSchedule();

        if (backupSchedule.isAutomatic()) {

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

            Time nextTime = currentTime;

            boolean backupToday = false;

            if (weekDays.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                for (Time time : times) {
                    if (currentTime.getTime() <= time.getTime()) {
                        //O backup será nesta mesma data, no primeiro horário
                        //da lista, após o horário atual do sistema.
                        nextTime = time;
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
                nextTime = times.get(0);
            }

            calendar.set(Calendar.HOUR_OF_DAY, nextTime.getHours());
            calendar.set(Calendar.MINUTE, nextTime.getMinutes());
            calendar.set(Calendar.SECOND, nextTime.getSeconds());

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
     * 
     * @param stopMonitor status de desativação do thread do controlador de
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

    
    /**
     * Obter a data do próximo backup automático.
     * @return data do próximo backup automático.
     */
    public Date getNextBackupTime() {
        return nextBackupTime;
    }
    
    
    /**Obter a instância única da classe
     * @return instancia da classe.
     */
    public static BackupMonitor getInstance() {
        return instance;
    }
    
    
}