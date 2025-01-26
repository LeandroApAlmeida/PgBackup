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
 *
 * @author leandro
 */
public class LastBackupInfo {
    
    
    /**Arquivo contendo o registro do último backup realizado do banco de dados.*/
    private final File xmlFile;
    
    private File outputFile;
    
    private Date date;

    
    public LastBackupInfo() {
        
        xmlFile = new File(
            System.getProperty("root_dir")
            .concat(File.separator)
            .concat("last-backup.xml")
        );
        
        loadXmlFile();
        
    }
    
    
    private void loadXmlFile() {
        
        try {
            
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(xmlFile);

            Element root = (Element) document.getRootElement();

            List<Element> n0 = root.getChildren();

            for (Element e0 : n0) {
                if (e0.getName().equals("date")) {
                    date = new Date(Long.parseLong(e0.getText()));
                } else if (e0.getName().equals("file")) {
                    outputFile = new File(e0.getText());
                }
            }
            
        } catch (Exception ex) {
            outputFile = null;
            date = null;
        }
        
    }  
    
    
    public void saveXmlFile() throws FileNotFoundException, IOException {
        
        Document document = new Document();

        Element root = new Element("last_backup");

        Element e0 = new Element("date");
        e0.setText(String.valueOf(date.getTime()));
        root.addContent(e0);

        Element e1 = new Element("file");
        e1.setText(outputFile.getAbsolutePath());
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

    
    public Date getDate() {
        return date;
    }

    
    public File getOutputFile() {
        return outputFile;
    }

    
    public void setDate(Date date) {
        this.date = date;
    }

    
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }
    
    
}