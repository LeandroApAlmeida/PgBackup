package postgre.backup.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import postgre.backup.run.Application;

/**
 * Classe para gerenciamento dos processos de backup e restore do banco de
 * dados do PostgreSQL.
 * 
 * @author Leandro Aparecido de Almeida
 */
public final class BackupManager {
    
    
    /**
     * Obter o path do próximo arquivo de backup. Será mantido um número definido
     * de arquivos de backup no diretório ~/PG_BACKUP/[nome do banco de dados], 
     * conforme definido pelo administrador. Alcançado este número de arquivos,
     * o mais antigo será excluído, para a gravação do novo backup.
     * 
     * @param outputDrive drive de destino do backup.
     * 
     * @param serverSettings configurações do serviço de backup.
     * 
     * @return path do novo arquivo a ser criado.
     * 
     * @throws IOException
     */
    private File getNextBackupFile(String outputDrive, ServerSettings serverSettings) 
    throws IOException {
        
        ArrayList<File> backupFiles = new ArrayList<>(serverSettings.getNumberOfFiles());
        
        File backupFolder = new File(
            outputDrive
            .concat(File.separator)
            .concat("PG_BACKUP")
            .concat(File.separator)
            .concat(serverSettings.getDatabase())
        );
        
        if (backupFolder.exists()) {
            // Obtém todos os arquivos no diretório de backup.
            for (File file : backupFolder.listFiles()) {
                if (file.isFile()) {
                    backupFiles.add(file);
                }
            }
        } else {
            backupFolder.mkdirs();
        }
        
        if (backupFiles.size() >= serverSettings.getNumberOfFiles()) {
        
            // Exclui todos os arquivos mais antigos que ultrapassem a contagem
            // de (serverSettings.getNumberOfFiles() - 1). 
            // 
            // Exemplo: 
            // 
            // Se serverSettings.getNumberOfFiles() é igual a 5, e o número de
            // arquivos de backup no diretório é 7, então exclui os 3 arquivos
            // mais antigos, de modo a ficar somente 4 arquivos no diretório. Ao
            // gravar o próximo backup, voltam a ficar 5 arquivos novamente, 
            // conforme definido pelo administrador em serverSettings.getNumberOfFiles().
            
            Collections.sort(backupFiles, (File f1, File f2) -> {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                } else {
                    return 0;
                }
            });
            
            int numFilesToDelete = backupFiles.size() - (serverSettings.getNumberOfFiles() - 1);

            for (int i = 0; i < numFilesToDelete; i++) {
                // Exclui o(s) arquivo(s) mais antigo(s).
                backupFiles.get(i).delete();
            }
        
        }
        
        // Constrói o path do arquivo com base no diretório de backup no drive
        // de destino e na data atual do sistema.
        
        LocalDateTime now = LocalDateTime.now();

        String fileName = serverSettings.getDatabase()
        .concat("_")
        .concat(String.valueOf(now.getYear()))
        .concat(String.format("%02d", now.getMonthValue()))
        .concat(String.format("%02d", now.getDayOfMonth()))
        .concat(String.format("%02d", now.getHour()))
        .concat(String.format("%02d", now.getMinute()))
        .concat(String.format("%02d", now.getSecond()))
        .concat(String.format("%03d", now.getNano() / 1000000));
        
        File backupFile = new File(
            backupFolder.getAbsolutePath()
            .concat(File.separator)
            .concat(fileName)
            .concat(System.getProperty("file_extension"))
        );
        
        return backupFile;
        
    }
    
    
    /**
     * Fazer o backup do banco de dados PostgreSQL. O backup é realizado
     * passando-se os seguintes parâmetros para o pg_dump:
     * 
     * <br><br>
     * 
     * <i>"--host=[host]":</i> endereço IP do host do Postgre Server.
     * 
     * <br><br>
     * 
     * <i>"--port=[porta]":</i> porta TCP do serviço do Postgre Server.
     * 
     * <br><br>
     * 
     * <i>"--username=[usuário]":</i> usuário para acesso ao banco de dados.
     * 
     * <br><br>
     * 
     * <i>"--dbname=[nome banco de dados]":</i> nome do banco de dados que se
     * fará o backup.
     * 
     * <br><br>
     * 
     * <i>"--format=c":</i> formato do arquivo de backup.
     * 
     * <br><br>
     * 
     * <i>"--data-only":</i> utilizar esta diretiva se no backup se deseja
     * extrair somente os dados contidos nas tabelas do banco de dados, sem
     * extrair a estrutura para a reconstrução do mesmo.
     * 
     * <br><br>
     * 
     * <i>"--file=[nome do arquivo de backup]":</i> caminho do arquivo de backup
     * do banco de dados.
     * 
     * <br><br>
     * 
     * Exemplo: 
     * 
     * <br><br>
     * 
     * pg_dump.exe "--host=127.0.0.1" "--port=5432" "--username=postgres"
     * "--dbname=db_teste" "--format=c" "--data-only" "--file=D:\db_teste.pgback"
     * 
     * <br><br>
     * 
     * <i><b>Obs.:</b> o nome do arquivo de backup deve ser passado como último 
     * parâmetro.</i>
     * 
     * @param outputDrive Drive de destino do Backup.
     * 
     * @return Arquivo de backup criado.
     * 
     * @throws IOException
     * 
     * @throws BackupException erro no processo de backup do banco de dados.
     */
    public synchronized File doBackup(String outputDrive) throws IOException, 
    BackupException {
        
        ServerSettings serverSettings = new ServerSettings();
        
        File backupFile = getNextBackupFile(outputDrive, serverSettings);
        
        // Lista de parâmetros para o pg_dump.exe.
        List<String> pgAdminParams = new ArrayList<>();
        
        pgAdminParams.add(serverSettings.getBackupExecutable());
        pgAdminParams.add("--host=" + serverSettings.getHost());
        pgAdminParams.add("--port=" + String.valueOf(serverSettings.getPort()));
        pgAdminParams.add("--username=" + serverSettings.getUserName());
        pgAdminParams.add("--dbname=" + serverSettings.getDatabase());
        pgAdminParams.add("--format=c");
        
        if (serverSettings.getBackupMode() == ServerSettings.EXTRACT_DATA_ONLY) {
            pgAdminParams.add("--data-only");
        }
        
        pgAdminParams.add("--file=" + backupFile.getAbsolutePath());
        
        ProcessBuilder builder = new ProcessBuilder(pgAdminParams);
        
        builder.environment().put("PGPASSWORD", serverSettings.getPassword());
        
        builder.redirectErrorStream(true);
        
        // Notifica o usuário sobre o início do processo de backup.
        Application.displayMessage(                    
            "NOTIFICAÇÃO DE PROCESSO:",
            "Realizando o \"Backup\" do banco de dados \"" + 
            serverSettings.getDatabase() + "\"",
            Application.MESSAGE_NONE
        );
        
        StringBuilder sb = new StringBuilder();
        
        // Inicia o processo de backup.
        Process process = builder.start();
        
        // Captura qualquer mensagem de erro que pode ter ocorrido com o 
        // processamento do backup.
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process
        .getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        
        process.destroyForcibly();
        
        if (!sb.toString().equals("")) {
            
            //Notifica o usuário sobre erro ocorrido no processo de backup.
            Application.displayMessage(                    
                "ERRO NO PROCESSO DE BACKUP:",
                sb.toString(),
                Application.MESSAGE_ERROR
            );
            
            throw new BackupException(sb.toString());
            
        } else {
            
            //Grava o registro do backup no arquivo XML.
            
            Date date = new Date(System.currentTimeMillis());
            
            LastBackupInfo lastBackupInfo = new LastBackupInfo();
            
            lastBackupInfo.setDate(date);
            
            lastBackupInfo.setFile(backupFile);
            
            lastBackupInfo.saveXmlFile();
            
            // Notifica o usuário sobre o sucesso no processamento do backup.
            Application.displayMessage(                    
                "BACKUP CONCLUÍDO!",
                "Backup do banco de dados \"" + serverSettings.getDatabase() + 
                "\" concluído com sucesso.",
                Application.MESSAGE_NONE
            );
            
        }
        
        return backupFile;
        
    } 
    
    
    /**
     * Fazer o restore do banco de dados PostgreSQL. O restore é realizado
     * passando-se os seguintes parâmetros para o pg_restore:
     * 
     * <br><br>
     * 
     * <i>"--host=[host]":</i> endereço IP do host do Postgre Server.
     * 
     * <br><br>
     * 
     * <i>"--port=[porta]":</i> porta TCP do serviço do Postgre Server.
     * 
     * <br><br>
     * 
     * <i>"--username=[usuário]":</i> usuário para acesso ao banco de dados
     * PostgreSQL.
     * 
     * <br><br>
     * 
     * <i>"--dbname=[nome banco de dados]":</i> nome do banco de dados que se
     * fará o restore.
     * 
     * <br><br>
     * 
     * <i>"[nome do arquivo de backup]":</i> caminho do arquivo de backup
     * do banco de dados.
     * 
     * <br><br>
     * 
     * Exemplo: 
     * 
     * <br><br>
     * 
     * pg_restore.exe "--host=127.0.0.1" "--port=5432" "--username=postgres"
     * "--dbname=db_teste" "D:\db_teste.pgback"
     * 
     * <br><br>
     * 
     * <i><b>Obs.:</b> o nome do arquivo de backup deve ser passado como último 
     * parâmetro.</i>
     * 
     * @param inputFile arquivo de backup do banco de dados.
     * 
     * @throws IOException
     * 
     * @throws RestoreException erro no processo de restore do banco de dados.
     */
    public synchronized void doRestore(String inputFile) throws IOException,
    RestoreException{
        
        ServerSettings serverSettings = new ServerSettings();      
        
        // Lista de parâmetros para o pg_restore.exe.
        List<String> pgAdminParams = new ArrayList<>();
        
        pgAdminParams.add(serverSettings.getRestoreExecutable());
        pgAdminParams.add("--host=" + serverSettings.getHost());
        pgAdminParams.add("--port=" + String.valueOf(serverSettings.getPort()));
        pgAdminParams.add("--username=" + serverSettings.getUserName());
        pgAdminParams.add("--dbname=" + serverSettings.getDatabase());
        pgAdminParams.add(inputFile);
        
        ProcessBuilder builder = new ProcessBuilder(pgAdminParams);
        
        builder.environment().put("PGPASSWORD", serverSettings.getPassword());
        
        builder.redirectErrorStream(true);
        
        // Notifica o usuário sobre o início do processo de restore.
        Application.displayMessage(                    
            "NOTIFICAÇÃO DE PROCESSO:",
            "Realizando a \"Restauração\" do banco de dados \"" + 
            serverSettings.getDatabase() + "\"",
            Application.MESSAGE_NONE
        );
        
        // Inicia o processo de restore.
        Process process = builder.start();
        
        StringBuilder sb = new StringBuilder();
        
        // Captura qualquer mensagem de erro que pode ter ocorrido com o 
        // processamento do restore.
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process
        .getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
        }
        
        process.destroyForcibly();
        
        if (!sb.toString().equals("")) {
            
            //Notifica o usuário sobre erro ocorrido no processo de restore.
            Application.displayMessage(                    
                "ERRO NO PROCESSO DE RESTORE:",
                sb.toString(),
                Application.MESSAGE_ERROR
            );
            
            throw new RestoreException(sb.toString());
        
        } else {
            
            // Notifica o usuário sobre o sucesso do processamento do restore.
            Application.displayMessage(                    
                "RESTORE CONCLUÍDO!",
                "Restore do banco de dados \"" + serverSettings.getDatabase() + 
                "\" concluído com sucesso.",
                Application.MESSAGE_NONE
            );
            
        }
    }
    
    
}