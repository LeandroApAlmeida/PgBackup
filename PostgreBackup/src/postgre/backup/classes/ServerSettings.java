package postgre.backup.classes;

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
 * PostgreSQL.
 * @author Leandro Aparecido de Almeida
 */
public class ServerSettings {

    public static final int REMOVABLE_DRIVE = 1;
    public static final int NETWORK_DRIVE = 2;
    /**Opção extrair apenas os dados das tabelas do banco de dados.*/
    public static final int EXTRACT_DATA_ONLY = 1;
    /**Opção estrair os dados e a estrutura do banco de dados.*/
    public static final int EXTRACT_STRUCTURE_AND_DATA = 2;
    
    /**Instância única da classe.*/
    private static final ServerSettings instance = new ServerSettings();
    /**Arquivo XML aonde os arquivos serão gravados e lidos.*/
    private final File xmlFile;
    /**Modoo de backup do banco de dados.*/
    private int backupMode;
    /**Status de extrair blobs (objetos grandes) no bckup.*/
    private boolean extractBlobs;
    /**Arquivo de destino do backup (local ou em rede).*/
    private String networkDrive; 
    /**Número IP do Postgre Server.*/
    private String host;
    /**Nome de usuário para acesso ao Postgre Server.*/
    private String userName;
    /**Senha  para acesso ao Postgre Server.*/
    private String password;
    /**Nome do banco de dados PostgreSQL sob controle de backup.*/
    private String database;
    /**Programa que faz o backup do banco de dados (pg_dump.exe)*/
    private String backupExecutable;
    /**Programa que faz o restore do banco de dados (pg_restore.exe)*/
    private String restoreExecutable;
    /**Porta TCP do Postgre Server.*/
    private int port;    
    private int driveType;
    
    private ServerSettings() {
        xmlFile = new File(System.getProperty("root_dir") + File.separator + 
        "config.xml");        
        loadXmlFile();
    }
    
    private void loadXmlFile() {
        try {
            if (xmlFile.exists()) {
                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(xmlFile);
                Element root = (Element) document.getRootElement();
                List<Element> n0 = root.getChildren();
                for (Element e0 : n0) {
                    switch (e0.getName()) {
                        case "host": host = e0.getText(); break;
                        case "port": port = Integer.valueOf(e0.getText()); break;
                        case "user_name": userName = e0.getText(); break;
                        case "password": password = e0.getText(); break;
                        case "database": database = e0.getText(); break;
                        case "backup_executable": backupExecutable = e0.getText(); break;
                        case "restore_executable": restoreExecutable = e0.getText(); break;
                        case "backup_mode": backupMode = Integer.valueOf(e0
                        .getText()); break;
                        case "extract_blobs": extractBlobs = Boolean.valueOf(e0
                        .getText()); break;
                        case "network_drive": networkDrive = e0.getText(); break;
                        case "drive_type": driveType = Integer.valueOf(e0.getText()); break;
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
        }
    }
    
    public void saveXmlFile() throws FileNotFoundException, IOException {        
        Document doc = new Document();
        Element root = new Element("config");
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
        doc.setRootElement(root);
        if (xmlFile.exists()) xmlFile.delete();
        Format format = Format.getPrettyFormat();
        format.setEncoding("ISO-8859-1");
        XMLOutputter xout = new XMLOutputter(format);
        try (OutputStream out = new FileOutputStream(xmlFile)) {
            xout.output(doc, out);
        }    
    }
    
    public int getBackupMode() {
        return backupMode;
    }

    public boolean extractBlobs() {
        return extractBlobs;
    }

    public String getNetworkDrive() {
        return networkDrive;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
    
    public String getBackupExecutable() {
        return backupExecutable;
    }

    public String getRestoreExecutable() {
        return restoreExecutable;
    }
    
    public int getDriveType() {
        return driveType;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    
    public void setBackupMode(int backupMode) {
        this.backupMode = backupMode;
    }

    public void setExtractBlobs(boolean extractBlobs) {
        this.extractBlobs = extractBlobs;
    }

    public void setNetworkDrive(String networkDrive) {
        this.networkDrive = networkDrive;
    }

    public void setBackupExecutable(String backupExecutable) {
        this.backupExecutable = backupExecutable;
    }

    public void setRestoreExecutable(String restoreExecutable) {
        this.restoreExecutable = restoreExecutable;
    }
    
    public void setDriveType(int driveType) {
        this.driveType = driveType;
    }

    public static ServerSettings getInstance() {
        return instance;
    }
    
}