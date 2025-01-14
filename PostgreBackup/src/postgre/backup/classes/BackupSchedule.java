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

public class BackupSchedule {

    private static final BackupSchedule instance = new BackupSchedule();
    private final List<Time> backupTimes;
    private final File xmlFile;
    private boolean activated;
    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;    
    private boolean friday;
    private boolean saturday;    

    private BackupSchedule() {
        xmlFile = new File(System.getProperty("root_dir") + File.separator +
        "schedules.xml");
        backupTimes = new ArrayList<>();
        activated = false;
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
                        case "activated": {
                            activated = Boolean.valueOf(e0.getText());
                        } break;
                        case "week_days": {
                          List<Element> n1 = e0.getChildren();
                            for (Element e1 : n1) {
                                switch (e1.getName()) {
                                    case "sun": sunday = Boolean.valueOf(e1.getText()); break;
                                    case "mon": monday = Boolean.valueOf(e1.getText()); break;
                                    case "tue": tuesday = Boolean.valueOf(e1.getText()); break;
                                    case "wed": wednesday = Boolean.valueOf(e1.getText()); break;
                                    case "thu": thursday = Boolean.valueOf(e1.getText()); break;
                                    case "fri": friday = Boolean.valueOf(e1.getText()); break;
                                    case "sat": saturday = Boolean.valueOf(e1.getText()); break;
                                }
                            }
                        } break;
                        case "backup_times": {                            
                            List<Element> n1 = e0.getChildren();
                            for (Element e1 : n1) {
                                Time time = new Time(Long.valueOf(e1.getText()));
                                backupTimes.add(time);
                            }    
                        } break;
                    }
                }
            } else {
                throw new Exception("Arquivo não encontrado.");
            }
        } catch (Exception ex) {
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

    public List<Time> getBackupTimesList() {
        return backupTimes;
    }

    public boolean isSunday() {
        return sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isActivated() {
        return activated;
    }
    
    public void setSunday(boolean sun) {
        this.sunday = sun;
    }

    public void setMonday(boolean mon) {
        this.monday = mon;
    }

    public void setTuesday(boolean tue) {
        this.tuesday = tue;
    }

    public void setWednesday(boolean wed) {
        this.wednesday = wed;
    }

    public void setThursday(boolean thu) {
        this.thursday = thu;
    }

    public void setFriday(boolean fri) {
        this.friday = fri;
    }

    public void setSaturday(boolean sat) {
        this.saturday = sat;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public static BackupSchedule getInstance() {
        return instance;
    }

}