package postgre.backup.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * Classe com os parâmetros para agendamento do backup. O formato do arquivo XML
 * que contém os parâmetros é o seguinte:
 * 
 * <br><br>
 * 
 * <pre>
 * &lt;schedule&gt;
 *      
 *      &lt;activated&gt;[1]&lt;/activated&gt;
 *      
 *      &lt;week_days&gt;
 *          &lt;sun&gt;[2]&lt;/sun&gt;
 *          &lt;mon&gt;[3]&lt;/mon&gt;
 *          &lt;tue&gt;[4]&lt;/tue&gt;
 *          &lt;wed&gt;[5]&lt;/wed&gt;
 *          &lt;thu&gt;[6]&lt;/thu&gt;
 *          &lt;fri&gt;[7]&lt;/fri&gt;
 *          &lt;sat&gt;[8]&lt;/sat&gt;
 *      &lt;/week_days&gt;
 *      
 *      &lt;backup_times&gt;
 *          &lt;time&gt;[9]&lt;/time&gt;
 *          &lt;time&gt;[10]&lt;/time&gt;
 *      &lt;/backup_times&gt;
 * 
 * &lt;/schedule&gt;
 * </pre>
 * 
 * Onde:
 * 
 * <br><br>
 * 
 * [1]: Status de backup automático ativado.<br>
 * [2]: Status de backup aos domingos.<br>
 * [3]: Status de backup às segundas-feiras.<br>
 * [4]: Status de backup às terças-feiras.<br>
 * [5]: Status de backup às quartas-feiras.<br>
 * [6]: Status de backup às quintas-feiras.<br>
 * [7]: Status de backup às sextas-feiras.<br>
 * [8]: Status de backup aos sábados.<br>
 * [9]: Horário de backup 1.<br>
 * [10]: Horário de backup 2.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class BackupSchedule {


    /**Horários de backup.*/
    private final List<Time> backupTimes;
    
    /**Path do arquivo XML que contém os parâmetros para agendamento do backup.*/
    private final File xmlFile;
    
    /**Status de backup automático ativado.*/
    private boolean activated;
    
    /**Status de backup automático no domingo.*/
    private boolean sunday;
    
    /**Status de backup automático na segunda-feira.*/
    private boolean monday;
    
    /**Status de backup automático na terça-feira.*/
    private boolean tuesday;
    
    /**Status de backup automático na quarta-feira.*/
    private boolean wednesday;
    
    /**Status de backup automático na quinta-feira.*/
    private boolean thursday;    
    
    /**Status de backup automático na sexta-feira.*/
    private boolean friday;
    
    /**Status de backup automático no sábado.*/
    private boolean saturday;    

    
    public BackupSchedule() {
        
        xmlFile = new File(System.getProperty("root_dir") + File.separator +
        "schedules.xml");
        
        backupTimes = new ArrayList<>();
        
        activated = false;
        
        loadXmlFile();        
    
    }
    
    
    /**
     * Carrega o arquivo XML que contém os parâmetros para o agendamento de
     * backup.
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
                        
                        // Obtém o status de backup automático ativado.
                        case "activated": {
                            
                            activated = Boolean.parseBoolean(e0.getText());
                            
                        } break;
                       
                        // Obtém o status de backup automático por dia da semana.
                        case "week_days": {
                            
                            List<Element> n1 = e0.getChildren();
                            
                            for (Element e1 : n1) {
                                switch (e1.getName()) {
                                    
                                    case "sun": sunday = Boolean.parseBoolean(e1.getText()); break;
                                    
                                    case "mon": monday = Boolean.parseBoolean(e1.getText()); break;
                                    
                                    case "tue": tuesday = Boolean.parseBoolean(e1.getText()); break;
                                    
                                    case "wed": wednesday = Boolean.parseBoolean(e1.getText()); break;
                                    
                                    case "thu": thursday = Boolean.parseBoolean(e1.getText()); break;
                                    
                                    case "fri": friday = Boolean.parseBoolean(e1.getText()); break;
                                    
                                    case "sat": saturday = Boolean.parseBoolean(e1.getText()); break;
                                
                                }
                            }
                            
                        } break;
                        
                        // Obtém os horários de backup automático em cada dia
                        // da semana que foi definido.
                        case "backup_times": {
                            
                            List<Element> n1 = e0.getChildren();
                            
                            for (Element e1 : n1) {
                                Time time = new Time(Long.parseLong(e1.getText()));
                                backupTimes.add(time);
                            }
                            
                        } break;
                        
                    }
                    
                }
                
            } else {
                
                throw new Exception("Arquivo não encontrado.");
                
            }
            
        } catch (Exception ex) {
            
            // Em caso de erro, define os valores padrão.
            
            activated = false;
            sunday = false;
            monday = false;
            tuesday = false;
            wednesday = false;
            thursday = false;
            friday = false;
            saturday = false;
            
        }
        
    }
    
    
    /**
     * Salvar os parâmetros para o agendamento de backup no arquivo XML.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveXmlFile() throws FileNotFoundException, IOException {
        
        Document doc = new Document();
        
        Element root = new Element("schedule");
        
        Element e0 = new Element("activated");
        
        e0.setText(String.valueOf(activated));
        root.addContent(e0);        
        
        Element e1 = new Element("week_days");
        
        Element e1_1 = new Element("sun");
        e1_1.setText(String.valueOf(sunday));
        e1.addContent(e1_1);
        
        Element e1_2 = new Element("mon");
        e1_2.setText(String.valueOf(monday));
        e1.addContent(e1_2);
        
        Element e1_3 = new Element("tue");
        e1_3.setText(String.valueOf(tuesday));
        e1.addContent(e1_3);
        
        Element e1_4 = new Element("wed");
        e1_4.setText(String.valueOf(wednesday));
        e1.addContent(e1_4);
        
        Element e1_5 = new Element("thu");
        e1_5.setText(String.valueOf(thursday));
        e1.addContent(e1_5);
        
        Element e1_6 = new Element("fri");
        e1_6.setText(String.valueOf(friday));
        e1.addContent(e1_6);
        
        Element e1_7 = new Element("sat");
        e1_7.setText(String.valueOf(saturday));
        e1.addContent(e1_7);
        
        root.addContent(e1);
        
        Element e2 = new Element("backup_times");
        
        for (Time time : backupTimes) {
            Element e2_1 = new Element("time");
            e2_1.setText(String.valueOf(time.getTime()));
            e2.addContent(e2_1);
        }
        
        root.addContent(e2);
        
        doc.setRootElement(root);
        
        xmlFile.delete();
        
        Format format = Format.getPrettyFormat();
        format.setEncoding("ISO-8859-1");
        
        XMLOutputter xout = new XMLOutputter(format);
        
        try (OutputStream out = new FileOutputStream(xmlFile)) {
            xout.output(doc, out);
        }
        
    }

    
    /**
     * Obter a lista com os horários para backup.
     * @return lista com os horários para backup.
     */
    public List<Time> getBackupTimesList() {
        return backupTimes;
    }

    
    /**
     * Obter o status de backup aos domingos.
     * @return status de backup aos domingos.
     */
    public boolean isSunday() {
        return sunday;
    }

    
    /**
     * Obter o status de backup às segundas-feiras.
     * @return status de backup às segundas-feiras.
     */
    public boolean isMonday() {
        return monday;
    }

    
    /**
     * Obter o status de backup às terças-feiras.
     * @return status de backup às terças-feiras.
     */
    public boolean isTuesday() {
        return tuesday;
    }

    
    /**
     * Obter o status de backup às quartas-feiras.
     * @return status de backup às quartas-feiras.
     */
    public boolean isWednesday() {
        return wednesday;
    }

    
    /**
     * Obter o status de backup às quintas-feiras.
     * @return status de backup às quintas-feiras.
     */
    public boolean isThursday() {
        return thursday;
    }

    
    /**
     * Obter o status de backup às sextas-feiras.
     * @return status de backup às sextas-feiras.
     */
    public boolean isFriday() {
        return friday;
    }

    
    /**
     * Obter o status de backup aos sábados.
     * @return status de backup aos sábados.
     */
    public boolean isSaturday() {
        return saturday;
    }

    
    /**
     * Obter o status de backup automático ativado.
     * @return status de backup automático ativado.
     */
    public boolean isActivated() {
        return activated;
    }
    
    
    /**
     * Definir o status de backup automático aos domingos.
     * @param sun status de backup automático aos domingos. 
     */
    public void setSunday(boolean sun) {
        this.sunday = sun;
    }

    
    /**
     * Definir o status de backup automático às segundas-feiras.
     * @param mon status de backup automático às segundas-feiras. 
     */
    public void setMonday(boolean mon) {
        this.monday = mon;
    }

    
    /**
     * Definir o status de backup automático às terças-feiras.
     * @param tue status de backup automático às terças-feiras. 
     */
    public void setTuesday(boolean tue) {
        this.tuesday = tue;
    }

    
    /**
     * Definir o status de backup automático às quartas-feiras.
     * @param wed status de backup automático às quartas-feiras. 
     */
    public void setWednesday(boolean wed) {
        this.wednesday = wed;
    }

    
    /**
     * Definir o status de backup automático às quintas-feiras.
     * @param thu status de backup automático às quintas-feiras. 
     */
    public void setThursday(boolean thu) {
        this.thursday = thu;
    }

    
    /**
     * Definir o status de backup automático às sextas-feiras.
     * @param fri status de backup automático às sextas-feiras. 
     */
    public void setFriday(boolean fri) {
        this.friday = fri;
    }


    /**
     * Definir o status de backup automático aos sábados.
     * @param sat status de backup automático aos sábados. 
     */
    public void setSaturday(boolean sat) {
        this.saturday = sat;
    }

    
    /**
     * Definir o status de backup automático ativado.
     * @param activated status de backup automático ativado.
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    
}