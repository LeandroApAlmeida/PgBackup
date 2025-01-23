package postgre.backup.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import postgre.backup.run.Application;

/**
 * Classe para gerenciamento dos processos de backup e restore do banco de
 * dados do PostgreSQL.
 * 
 * @author Leandro Aparecido de Almeida
 */
public final class BackupManager {
    
    
    /**Arquivo contendo o registro do último backup realizado do banco de dados.*/
    private static final File backupRegFile = new File(System.getProperty("root_dir") +
    File.separator + "backupdata.xml");

    
    /**
     * Constructor private para impedir a criação de uma instância da classe.
     */
    private BackupManager() {
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
     * <i>"--username=[usuário]":</i> usuário para acesso ao banco de dados
     * PostgreSQL.
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
     * @throws IOException erro na gravação do arquivo de backup.
     * @throws BackupException erro no processo de backup do banco de dados.
     */
    public static synchronized void doBackup(String outputDrive) throws IOException, 
    BackupException {
        
        ServerSettings serverSettings = ServerSettings.getInstance();
        
        File backupOutputFile = new File(outputDrive + File.separator + 
        serverSettings.getDatabase() + System.getProperty("file_extension"));
        
        if (backupOutputFile.exists()){
            backupOutputFile.delete();
        }         
        
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
        
        pgAdminParams.add("--file=" + backupOutputFile.getAbsolutePath());
        
        ProcessBuilder builder = new ProcessBuilder(pgAdminParams);
        
        builder.environment().put("PGPASSWORD", serverSettings.getPassword());
        
        builder.redirectErrorStream(true);
        
        // Notifica o usuário sobre o início do processo de backup.
        Application.displayTrayMessage(                    
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
            Application.displayTrayMessage(                    
                "ERRO NO PROCESSO DE BACKUP:",
                sb.toString(),
                Application.MESSAGE_ERROR
            );
            
            throw new BackupException(sb.toString());
            
        } else {
            
            //Grava o registro do backup no arquivo XML.
            
            Date date = new Date(System.currentTimeMillis());
            Document document = new Document();
            
            Element root = new Element("backup_data");
            
            Element e0 = new Element("last_backup");
            e0.setText(String.valueOf(date.getTime()));
            root.addContent(e0);
            
            Element e1 = new Element("output_file");
            e1.setText(backupOutputFile.getAbsolutePath());
            root.addContent(e1);
            document.setRootElement(root);
            
            if (backupRegFile.exists()) backupRegFile.delete();
            
            Format format = Format.getPrettyFormat();
            format.setEncoding("ISO-8859-1");
            
            XMLOutputter xout = new XMLOutputter(format);
            try (OutputStream out = new FileOutputStream(backupRegFile)) {
                xout.output(document, out);
            }
            
            // Notifica o usuário sobre o sucesso no processamento do backup.
            Application.displayTrayMessage(                    
                "BACKUP CONCLUÍDO!",
                "Backup do banco de dados \"" + serverSettings.getDatabase() + 
                "\" concluído com sucesso.",
                Application.MESSAGE_NONE
            );
            
        }
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
     * @throws IOException erro na gravação do arquivo de backup.
     * @throws RestoreException erro no processo de restore do banco de dados.
     */
    public static synchronized void doRestore(String inputFile) throws IOException,
    RestoreException{
        
        ServerSettings serverSettings = ServerSettings.getInstance();      
        
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
        Application.displayTrayMessage(                    
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
            Application.displayTrayMessage(                    
                "ERRO NO PROCESSO DE RESTORE:",
                sb.toString(),
                Application.MESSAGE_ERROR
            );
            
            throw new RestoreException(sb.toString());
        
        } else {
            
            // Notifica o usuário sobre o sucesso do processamento do restore.
            Application.displayTrayMessage(                    
                "RESTORE CONCLUÍDO!",
                "Restore do banco de dados \"" + serverSettings.getDatabase() + 
                "\" concluído com sucesso.",
                Application.MESSAGE_NONE
            );
            
        }
    }
    
    
    /**
     * Obter a data do último backup realizado do banco de dados.
     * 
     * @return data do último backup realizado.
     * @throws org.jdom2.JDOMException
     * @throws java.io.IOException
     */
    public static synchronized Date getLastBackupDate() throws JDOMException,
    IOException {
        
        Date date = null;
        
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(backupRegFile);
        
        Element root = (Element) document.getRootElement();
        
        List<Element> n0 = root.getChildren();
        
        for (Element e0 : n0) {
            if (e0.getName().equals("last_backup")) {
                date = new Date(Long.parseLong(e0.getText()));
                break;
            }
        }
        
        return date;
        
    }
    
    
}