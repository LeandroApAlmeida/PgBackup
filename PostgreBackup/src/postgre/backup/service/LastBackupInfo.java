package postgre.backup.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Informações do último backup realizado. O formato do arquivo XML é o seguinte:
 * 
 * <br><br>
 * 
 * <pre>
 * &lt;last_backup&gt;
 *      &lt;date&gt;[1]&lt;/date&gt;
 *      &lt;file&gt;[2]&lt;/file&gt;
 * &lt;/last_backup&gt;
 * </pre>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * [1]: Data do último backup.<br>
 * [2]: Arquivo gerado pelo backup.<br>
 * 
 * @author Leandro Aparecido de Almeida
 */
public class LastBackupInfo {
    
    
    /**Arquivo XML contendo o registro do último backup realizado do banco de dados.*/
    private final File xmlFile;
    
    /**Arquivo gerado no último backup.*/
    private File file;
    
    /**Data do último backup.*/
    private Date date;

    
    /**
     * Constructor da classe. Ao instanciar a classe, carrega os dados do último
     * backup do arquivo XML.
     */
    public LastBackupInfo() {
        
        xmlFile = new File(
            System.getProperty("root_dir")
            .concat(File.separator)
            .concat("last-backup.xml")
        );
        
        loadXmlFile();
        
    }
    
    
    /**
     * Carregar os dados do arquivo XML.
     */
    private void loadXmlFile() {
        
        try {
            
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(xmlFile);

            Element root = (Element) document.getRootElement();

            List<Element> n0 = root.getChildren();

            for (Element e0 : n0) {
                switch (e0.getName()) {
                    case "date": date = new Date(Long.parseLong(e0.getText())); break;
                    case "file": file = new File(e0.getText()); break;
                }
            }
            
        } catch (Exception ex) {
            file = null;
            date = null;
        }
        
    }  
    
    
    /**
     * Salvar os dados no arquivo XML.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void saveXmlFile() throws FileNotFoundException, IOException {
        
        Document document = new Document();

        Element root = new Element("last_backup");

        Element e0 = new Element("date");
        e0.setText(String.valueOf(date.getTime()));
        root.addContent(e0);

        Element e1 = new Element("file");
        e1.setText(file.getAbsolutePath());
        root.addContent(e1);
        document.setRootElement(root);

        if (xmlFile.exists()) xmlFile.delete();

        Format format = Format.getPrettyFormat();
        format.setEncoding("ISO-8859-1");

        XMLOutputter xout = new XMLOutputter(format);
        try (OutputStream out = new FileOutputStream(xmlFile)) {
            xout.output(document, out);
        }
        
    }


    /**
     * Obter a data do último backup.
     * 
     * @return data do último backup.
     */
    public Date getDate() {
        return date;
    }

    
    /**
     * Obter o arquivo gerado no último backup.
     * 
     * @return arquivo gerado no último backup.
     */
    public File getFile() {
        return file;
    }

    
    /**
     * Definir a data do último backup.
     * 
     * @param date data do último backup.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    
    /**
     * Definir o arquivo gerado no último backup.
     * 
     * @param file arquivo gerado no último backup.
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    
}