package postgre.backup.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Parâmetros gerais para configuração do processo de backup do banco de dados
 * PostgreSQL. O formato do arquivo XML é o seguinte:
 * 
 * <br><br>
 * 
 * <pre>
 * &lt;settings&gt;
 *      &lt;host&gt;[1]&lt;/host&gt;
 *      &lt;port&gt;[2]&lt;/port&gt;
 *      &lt;user_name&gt;[3]&lt;/user_name&gt;
 *      &lt;password&gt;[4]&lt;/password&gt;
 *      &lt;database&gt;[5]&lt;/database&gt;
 *      &lt;backup_executable&gt;[6]&lt;/backup_executable&gt;
 *      &lt;restore_executable&gt;[7]&lt;/restore_executable&gt; 
 *      &lt;backup_mode&gt;[8]&lt;/backup_mode&gt;
 *      &lt;extract_blobs&gt;[9]&lt;/extract_blobs&gt;
 *      &lt;network_drive/&gt;[10]
 *      &lt;drive_type&gt;[11]&lt;/drive_type&gt;
 *      &lt;number_of_files&gt;[12]&lt;/number_of_files&gt;
 * &lt;/settings&gt;
 * </pre>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * [1]: Endereço do host do servidor PostgreSQL.<br>
 * [2]: Porta do servidor PostgreSQL.<br>
 * [3]: Nome de usuário para acesso ao servidor PostgreSQL.<br>
 * [4]: Senha para acesso ao servidor PostgreSQL.<br>
 * [5]: Nome do banco de dados sob controle de backup.<br>
 * [6]: Path do programa de backup do PostgreSQL (pg_dump.exe).<br>
 * [7]: Path do programa de restauração do backup do PostgreSQL (pg_restore.exe).<br>
 * [8]: Modo de backup (Dados apenas/Estrutura e dados).<br>
 * [9]: Status de extrair blobs.<br>
 * [10]: Drive de rede definido para backup.<br>
 * [11]: Tipo de drive do backup (Drive de Rede/Drive Removível).<br>
 * [12]: Número de arquivos a serem mantidos no diretório de backup.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class ServerSettings {

    
    /**Constante Backup em Drive Removível.*/
    public static final int REMOVABLE_DRIVE = 1;
    
    /**Constante Backup em Drive de Rede.*/
    public static final int NETWORK_DRIVE = 2;
    
    /**Constante extrair apenas os dados do banco de dados.*/
    public static final int EXTRACT_DATA_ONLY = 1;
    
    /**Constante extrair dados e a estrutura do banco de dados.*/
    public static final int EXTRACT_STRUCTURE_AND_DATA = 2;
    
    
    /**Arquivo XML aonde os arquivos serão gravados e lidos.*/
    private final File xmlFile;
    
    /**Modo de backup do banco de dados.*/
    private int backupMode;
    
    /**Tipo de drive para o backup (Drive Removível/Drive de Rede).*/
    private int driveType;
    
    /**Status de extrair blobs (objetos grandes) no backup.*/
    private boolean extractBlobs;
    
    /**Arquivo de destino do backup (local ou em rede).*/
    private String networkDrive; 
    
    /**Número IP do Postgre Server.*/
    private String host;
    
    /**Porta TCP do Postgre Server.*/
    private int port;
    
    /**Nome de usuário para acesso ao Postgre Server.*/
    private String userName;
    
    /**Senha para acesso ao Postgre Server.*/
    private String password;
    
    /**Nome do banco de dados sob controle de backup.*/
    private String database;
    
    /**Programa que faz o backup do banco de dados (pg_dump.exe)*/
    private String backupExecutable;
    
    /**Programa que faz o restore do banco de dados (pg_restore.exe)*/
    private String restoreExecutable;
    
    /**Número de arquivos a serem mantidos no diretório de backup.*/
    private int numberOfFiles = 1;
    
    
    /**
     * Constructor padrão. Ao instanciar, carrega os parâmetros para o serviço
     * a partir do arquivo XML "settings.xml" no diretório raiz do programa.
     */
    public ServerSettings() {
        
        xmlFile = new File(
            System.getProperty("root_dir")
            .concat(File.separator)
            .concat("settings.xml")
        );        
        
        loadXmlFile();
    
    }
    
    
    /**
     * Carrega o arquivo XML que contém os parâmetros para a configuração do
     * serviço de backup.
     */
    private void loadXmlFile() {
        
        try {
            
            if (xmlFile.exists()) {
                
                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(xmlFile);
                
                Element root = (Element) document.getRootElement();
                
                List<Element> n0 = root.getChildren();
                
                for (Element e0 : n0) {
                
                    switch (e0.getName()) {
                        
                        case "host" -> host = e0.getText();
                        
                        case "port" -> port = Integer.parseInt(e0.getText());
                        
                        case "user_name" -> userName = e0.getText();
                        
                        case "password" -> password = e0.getText();
                        
                        case "database" -> database = e0.getText();
                        
                        case "backup_executable" -> backupExecutable = e0.getText();
                        
                        case "restore_executable" -> restoreExecutable = e0.getText();
                        
                        case "backup_mode" -> backupMode = Integer.parseInt(e0.getText());
                        
                        case "extract_blobs" -> extractBlobs = Boolean.parseBoolean(e0.getText());
                        
                        case "network_drive" -> networkDrive = e0.getText();
                        
                        case "drive_type" -> driveType = Integer.parseInt(e0.getText());
                        
                        case "number_of_files" -> numberOfFiles = Integer.parseInt(e0.getText());
                    
                    }
                
                }
                
            } else {
                
                throw new Exception("Arquivo não encontrado.");
                
            }
            
        } catch (Exception ex) {
            
            host = "localhost";
            port = 5432;
            userName = "postgres";
            password = "";
            database = "";
            backupExecutable = "";
            restoreExecutable = "";
            backupMode = EXTRACT_STRUCTURE_AND_DATA;
            extractBlobs = true;
            networkDrive = "";
            numberOfFiles = 1;
            
        }
        
    }
    
    
    /**
     * Salvar os parâmetros para a configuração do serviço de backup no arquivo
     * XML.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void saveXmlFile() throws FileNotFoundException, IOException {        
        
        Document doc = new Document();
        
        Element root = new Element("settings");
        
        Element e0 = new Element("host");
        e0.setText(host);
        root.addContent(e0);
        
        Element e1 = new Element("port");
        e1.setText(String.valueOf(port));
        root.addContent(e1);
        
        Element e2 = new Element("user_name");
        e2.setText(userName);
        root.addContent(e2);
        
        Element e3 = new Element("password");
        e3.setText(password);
        root.addContent(e3);
        
        Element e4 = new Element("database");
        e4.setText(database);
        root.addContent(e4);
        
        Element e5 = new Element("backup_executable");
        e5.setText(backupExecutable);
        root.addContent(e5);
        
        Element e6 = new Element("restore_executable");
        e6.setText(restoreExecutable);
        root.addContent(e6);
        
        Element e7 = new Element("backup_mode");        
        e7.setText(String.valueOf(backupMode));
        root.addContent(e7);
        
        Element e8 = new Element("extract_blobs");
        e8.setText(String.valueOf(extractBlobs));
        root.addContent(e8);
        
        Element e9 = new Element("network_drive");
        e9.setText(networkDrive);
        root.addContent(e9);
        
        Element e10 = new Element("drive_type");
        e10.setText(String.valueOf(driveType));
        root.addContent(e10);
        
        Element e11 = new Element("number_of_files");
        e11.setText(String.valueOf(numberOfFiles));
        root.addContent(e11);
        
        doc.setRootElement(root);
        
        if (xmlFile.exists()) xmlFile.delete();
        
        Format format = Format.getPrettyFormat();
        format.setEncoding("ISO-8859-1");
        
        XMLOutputter xout = new XMLOutputter(format);
        
        try (OutputStream out = new FileOutputStream(xmlFile)) {
            xout.output(doc, out);
        }
        
    }
    
    
    /**
     * Obter o modo de backup (Dados apenas/Estrutura e dados).
     * 
     * @return modo de backup.
     */
    public int getBackupMode() {
    
        return backupMode;
    
    }

    
    /**
     * Obter o status de extrair blobs.
     * 
     * @return status de extrair blobs.
     */
    public boolean extractBlobs() {
    
        return extractBlobs;
    
    }

    
    /**
     * Obter o drive de rede definido para backup.
     * 
     * @return drive de rede definido para backup. 
     */
    public String getNetworkDrive() {
     
        return networkDrive;
    
    }

    
    /**
     * Obter o endereço do host do servidor PostgreSQL.
     * 
     * @return endereço do host do servidor PostgreSQL.
     */
    public String getHost() {
        
        return host;
    
    }

    
    /**
     * Obter a porta do servidor PostgreSQL.
     * 
     * @return porta do servidor PostgreSQL. 
     */
    public int getPort() {
        
        return port;
    
    }

    
    /**
     * Obter o nome de usuário para acesso ao servidor PostgreSQL.
     * 
     * @return nome de usuário para acesso ao servidor PostgreSQL.
     */
    public String getUserName() {
        
        return userName;
    
    }


    /**
     * Obter a senha para acesso ao servidor PostgreSQL.
     * 
     * @return senha para acesso ao servidor PostgreSQL. 
     */
    public String getPassword() {
        
        return password;
    
    }


    /**
     * Obter o nome do banco de dados sob controle de backup.
     * 
     * @return nome do banco de dados sob controle de backup. 
     */
    public String getDatabase() {
        
        return database;
    
    }
    
    
    /**
     * Obter o path do programa de backup do PostgreSQL (pg_dump.exe).
     * 
     * @return path do programa de backup do PostgreSQL.
     */
    public String getBackupExecutable() {
        
        return backupExecutable;
    
    }

    
    /**
     * Obter o path do programa de restauração do backup do PostgreSQL (pg_restore.exe).
     * 
     * @return path do programa de restauração do backup do PostgreSQL.
     */
    public String getRestoreExecutable() {
        
        return restoreExecutable;
    
    }
    
    
    /**
     * Obter o tipo de drive do backup (Drive de Rede/Drive Removível).
     * 
     * @return tipo de drive do backup.
     */
    public int getDriveType() {
        
        return driveType;
    
    }

    
    /**
     * Obter o número de arquivos a serem mantidos no diretório de backup.
     * 
     * @return número de arquivos a serem mantidos no diretório de backup.
     */
    public int getNumberOfFiles() {
        
        return numberOfFiles;
    
    }
    
    
    /**
     * Definir o endereço do host do servidor PostgreSQL.
     * 
     * @param host endereço do host do servidor PostgreSQL.
     */
    public void setHost(String host) {
        
        this.host = host;
    
    }

    
    /**
     * Definir a porta do servidor PostgreSQL.
     * 
     * @param port porta do servidor PostgreSQL.
     */
    public void setPort(int port) {
    
        this.port = port;
    
    }

    
    /**
     * Definir o nome de usuário para acesso ao servidor PostgreSQL.
     * 
     * @param userName nome de usuário para acesso ao servidor PostgreSQL.
     */
    public void setUserName(String userName) {
        
        this.userName = userName;
    
    }

    
    /**
     * Definir a senha para acesso ao servidor PostgreSQL.
     * 
     * @param password senha para acesso ao servidor PostgreSQL.
     */
    public void setPassword(String password) {
        
        this.password = password;
    
    }

    
    /**
     * Definir o nome do banco de dados sob controle de backup.
     * 
     * @param database nome do banco de dados sob controle de backup.
     */
    public void setDatabase(String database) {
        
        this.database = database;
    
    }
    
    
    /**
     * Definir o modo de backup (Dados apenas/Estrutura e dados).
     * 
     * @param backupMode modo de backup.
     */
    public void setBackupMode(int backupMode) {
        
        this.backupMode = backupMode;
    
    }

  
    /**
     * Definir o status de extrair blobs.
     * 
     * @param extractBlobs status de extrair blobs.
     */
    public void setExtractBlobs(boolean extractBlobs) {
        
        this.extractBlobs = extractBlobs;
    
    }

    
    /**
     * Definir o drive de rede definido para backup.
     * 
     * @param networkDrive drive de rede definido para backup.
     */
    public void setNetworkDrive(String networkDrive) {
        
        this.networkDrive = networkDrive;
    
    }


    /**
     * Definir o path do programa de backup do PostgreSQL (pg_dump.exe).
     * 
     * @param backupExecutable path do programa de backup do PostgreSQL. 
     */
    public void setBackupExecutable(String backupExecutable) {
       
        this.backupExecutable = backupExecutable;
    
    }

    
    /**
     * Definir o path do programa de restauração do backup do PostgreSQL (pg_restore.exe).
     * 
     * @param restoreExecutable path do programa de restauração do backup do PostgreSQL.
     */
    public void setRestoreExecutable(String restoreExecutable) {
        
        this.restoreExecutable = restoreExecutable;
    
    }
    
    
    /**
     * Definir o tipo de drive do backup (Drive de Rede/Drive Removível).
     * 
     * @param driveType tipo de drive de backup.
     */
    public void setDriveType(int driveType) {
        
        this.driveType = driveType;
    
    }


    /**
     * Definir o número de arquivos a serem mantidos no diretório de backup.
     * 
     * @param numOfFiles número de arquivos a serem mantidos no diretório de backup.
     */
    public void setNumberOfFiles(int numOfFiles) {
    
        this.numberOfFiles = numOfFiles;
    
    }

    
}