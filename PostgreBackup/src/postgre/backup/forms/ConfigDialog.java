package postgre.backup.forms;

import dialogs.ErrorDialog;
import dialogs.FileChooserDialog;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import postgre.backup.classes.BackupSchedule;
import postgre.backup.classes.BackupMonitor;
import postgre.backup.classes.ServerSettings;
import postgre.backup.run.Application;


public class ConfigDialog extends javax.swing.JDialog {
    
    
    private class Comparator1 implements Comparator<Time> {
        @Override
        public int compare(Time o1, Time o2) {
            return o1.compareTo(o2);
        }        
    }
    
    
    private final ServerSettings serverSettings = ServerSettings.getInstance();
    
    private final BackupSchedule backupSchedule = BackupSchedule.getInstance();
    
    private final List<Time> schedulesList;
    
    private final Comparator1 comparator;
    
    
    protected ConfigDialog() {
        
        super(null, Dialog.ModalityType.TOOLKIT_MODAL);
        
        schedulesList = new ArrayList<>();
        comparator = new Comparator1();
        
        initComponents();
        
        setIconImage(Application.getDefaultIcon());         
        
        loadServerConfig();
    
    }
    
    
    private void openBackupApplication() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Programa do Microsoft Windows (.exe)",
            "exe"
        );
        
        FileChooserDialog fchooser = new FileChooserDialog(
            "Abrir o Programa de Backup",
            filter
        );
        
        int opc = fchooser.showOpenDialog(this);
        
        if (opc == FileChooserDialog.APPROVE_OPTION) {
            jtfBackupApplication.setText(fchooser.getSelectedFile().getAbsolutePath());
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    
    }
    
    
    private void openRestoreApplication() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Programa do Microsoft Windows (.exe)",
            "exe"
        );        
        
        FileChooserDialog fchooser = new FileChooserDialog(
            "Abrir o Programa de Restore",
            filter
        );
        
        int opc = fchooser.showOpenDialog(this);
        
        if (opc == FileChooserDialog.APPROVE_OPTION) {
            jtfRestoreApplication.setText(fchooser.getSelectedFile().getAbsolutePath());
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    
    }

    
    private void loadServerConfig() {
        
        jtfHost.setText(serverSettings.getHost());
        jspPort.setValue(serverSettings.getPort());
        jtfUserName.setText(serverSettings.getUserName());
        jpfPassword.setText(serverSettings.getPassword());
        jtfDatabase.setText(serverSettings.getDatabase());
        
        if (serverSettings.getBackupMode() == ServerSettings.EXTRACT_DATA_ONLY) {
            jrbBackupMode1.setSelected(true);
        } else {
            jrbBackupMode2.setSelected(true);
        }
        
        if (serverSettings.extractBlobs()) {
            jrbExtractBlobs.setSelected(true);
        } else {
            jrbNonExtractBlobs.setSelected(true);
        }
        
        if (serverSettings.getDriveType() == ServerSettings.NETWORK_DRIVE) {
            jrbNetworkDrive.setSelected(true);
            jtfNetworkDrive.setEnabled(true);
        } else {
            jrbRemovableDrive.setSelected(true);
            jtfNetworkDrive.setEnabled(false);
        }
        
        jtfNetworkDrive.setText(serverSettings.getNetworkDrive());
        
        jtfBackupApplication.setText(serverSettings.getBackupExecutable());
        
        jtfRestoreApplication.setText(serverSettings.getRestoreExecutable());
        
        jcbSun.setSelected(backupSchedule.isSunday());
        jcbMon.setSelected(backupSchedule.isMonday());
        jcbTue.setSelected(backupSchedule.isTuesday());
        jcbWed.setSelected(backupSchedule.isWednesday());
        jcbThu.setSelected(backupSchedule.isThursday());
        jcbFri.setSelected(backupSchedule.isFriday());
        jcbSat.setSelected(backupSchedule.isSaturday());
        
        jcbActivated.setSelected(backupSchedule.isActivated());
        
        schedulesList.addAll(backupSchedule.getBackupTimesList());
        
        loadSchedules();
        
        configAutomaticBackupControls();
        
    }

    
    private void loadSchedules() {
    
        ListModel model = new AbstractListModel() {
            @Override
            public int getSize() {
                return schedulesList.size();
            }
            @Override
            public Object getElementAt(int index) {
                return schedulesList.get(index).toLocalTime().format(DateTimeFormatter.ISO_TIME);
            }
        };
        
        jlTimes.setModel(model);
        
        if (!schedulesList.isEmpty()) {
            jlTimes.setSelectedIndex(0);
            jbRemoveTime.setEnabled(true);
        } else {
            jbRemoveTime.setEnabled(false);
        }
        
    }
  
    
    private void configAutomaticBackupControls() {
        
        boolean enabled = jcbActivated.isSelected();
        
        for (Component c : jpWeekDays.getComponents()) {
            c.setEnabled(enabled);
        }
        
        jftTime.setEnabled(enabled);
        jbAddTime.setEnabled(enabled);
        jbRemoveTime.setEnabled(enabled);
        jlTimes.setEnabled(enabled);
    
    }
    
    
    private void insertTime() {
        
        Time time = Time.valueOf(jftTime.getText());
        
        if (!schedulesList.contains(time)) {
        
            schedulesList.add(time);
            schedulesList.sort(comparator);
            
            loadSchedules();
            
            jftTime.setText("");
        
        }
        
    }
    
    
    private void removeTime() {
    
        schedulesList.remove(jlTimes.getSelectedIndex());
        
        loadSchedules();
    
    }
    
    
    private void save() {
    
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        BackupMonitor.getInstance().stop(true);        
        
        try {
        
            if ((jrbNetworkDrive.isSelected() && jtfNetworkDrive.getText().equals("")) ||
            jtfHost.getText().equals("") || jtfUserName.getText().equals("") || 
            jtfDatabase.getText().equals("")) {
                throw new Exception(
                    "Preencha todos os campos para prosseguir."
                );
            }
            
            if (jcbActivated.isSelected()) {
            
                if (!jcbSun.isSelected() && !jcbMon.isSelected() && !jcbTue
                .isSelected() && !jcbWed.isSelected() && !jcbThu.isSelected()
                && !jcbFri.isSelected() && !jcbSat.isSelected()) {
                    throw new Exception(
                        "Defina os dias da semana para o backup automático."
                    );
                }
                
                if (schedulesList.isEmpty()) {
                    throw new Exception(
                        "Defina os horários para o backup automático."
                    );
                }
                
            }
            
            serverSettings.setHost(jtfHost.getText());
            serverSettings.setPort((int) jspPort.getValue());
            serverSettings.setUserName(jtfUserName.getText());
            serverSettings.setPassword(new String(jpfPassword.getPassword()));
            serverSettings.setDatabase(jtfDatabase.getText());
            
            serverSettings.setBackupMode(jrbBackupMode1.isSelected() ? ServerSettings
            .EXTRACT_DATA_ONLY : ServerSettings.EXTRACT_STRUCTURE_AND_DATA);
            
            serverSettings.setExtractBlobs(jrbExtractBlobs.isSelected());
            
            serverSettings.setNetworkDrive(jrbNetworkDrive.isSelected() ? 
            jtfNetworkDrive.getText() : "");
            
            serverSettings.setBackupExecutable(jtfBackupApplication.getText());
            serverSettings.setRestoreExecutable(jtfRestoreApplication.getText());
            
            serverSettings.setDriveType(jrbNetworkDrive.isSelected() ? 
            ServerSettings.NETWORK_DRIVE : ServerSettings.REMOVABLE_DRIVE);
            
            serverSettings.saveXmlFile();
            
            backupSchedule.setSunday(jcbSun.isSelected());
            backupSchedule.setMonday(jcbMon.isSelected());
            backupSchedule.setTuesday(jcbTue.isSelected());
            backupSchedule.setWednesday(jcbWed.isSelected());
            backupSchedule.setThursday(jcbThu.isSelected());
            backupSchedule.setFriday(jcbFri.isSelected());
            backupSchedule.setSaturday(jcbSat.isSelected());
            backupSchedule.setActivated(jcbActivated.isSelected());
            
            backupSchedule.getBackupTimesList().clear();
            backupSchedule.getBackupTimesList().addAll(schedulesList);
            backupSchedule.saveXmlFile();
            
            if (backupSchedule.isActivated()) {
                BackupMonitor.getInstance().start(true);
            }
            
            Application.updateTryIcon();
            
            setVisible(false);
            
        } catch (Exception ex) {
            
            ErrorDialog.showException((Frame) this.getParent(), "Erro!", ex);
            
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btgBackupMode = new javax.swing.ButtonGroup();
        btgExtractBlobs = new javax.swing.ButtonGroup();
        btgDestDrive = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jrbBackupMode1 = new javax.swing.JRadioButton();
        jrbBackupMode2 = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jrbExtractBlobs = new javax.swing.JRadioButton();
        jrbNonExtractBlobs = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jtfNetworkDrive = new javax.swing.JTextField();
        jrbNetworkDrive = new javax.swing.JRadioButton();
        jrbRemovableDrive = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jpWeekDays = new javax.swing.JPanel();
        jcbSun = new javax.swing.JCheckBox();
        jcbMon = new javax.swing.JCheckBox();
        jcbTue = new javax.swing.JCheckBox();
        jcbWed = new javax.swing.JCheckBox();
        jcbThu = new javax.swing.JCheckBox();
        jcbFri = new javax.swing.JCheckBox();
        jcbSat = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jlTimes = new javax.swing.JList<>();
        jbAddTime = new javax.swing.JButton();
        jbRemoveTime = new javax.swing.JButton();
        jftTime = new javax.swing.JFormattedTextField();
        jcbActivated = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtfBackupApplication = new javax.swing.JTextField();
        jbOpenBackupApplication = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jtfRestoreApplication = new javax.swing.JTextField();
        jbOpenRestoreApplication = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jpfPassword = new javax.swing.JPasswordField();
        jLabel16 = new javax.swing.JLabel();
        jtfUserName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jtfHost = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jspPort = new javax.swing.JSpinner();
        jtfDatabase = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jbSave = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        jLabel6.setText("1");

        jLabel9.setText("3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CONFIGURAÇÕES DO SERVIÇO DE BACKUP");
        setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Modo de Backup:"));

        btgBackupMode.add(jrbBackupMode1);
        jrbBackupMode1.setSelected(true);
        jrbBackupMode1.setText("Dados Apenas");

        btgBackupMode.add(jrbBackupMode2);
        jrbBackupMode2.setText("Estrutura e Dados");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbBackupMode1)
                    .addComponent(jrbBackupMode2))
                .addContainerGap(275, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jrbBackupMode1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbBackupMode2)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Blobs:"));

        btgExtractBlobs.add(jrbExtractBlobs);
        jrbExtractBlobs.setSelected(true);
        jrbExtractBlobs.setText("Extrair Blobs");

        btgExtractBlobs.add(jrbNonExtractBlobs);
        jrbNonExtractBlobs.setText("Não Extrair Blobs");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbExtractBlobs)
                    .addComponent(jrbNonExtractBlobs))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jrbExtractBlobs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbNonExtractBlobs)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Destino do Backup:"));

        btgDestDrive.add(jrbNetworkDrive);
        jrbNetworkDrive.setSelected(true);
        jrbNetworkDrive.setText("Drive de rede");
        jrbNetworkDrive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbNetworkDriveActionPerformed(evt);
            }
        });

        btgDestDrive.add(jrbRemovableDrive);
        jrbRemovableDrive.setText("Drive Removível");
        jrbRemovableDrive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbRemovableDriveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jrbRemovableDrive)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jrbNetworkDrive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtfNetworkDrive, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbNetworkDrive)
                    .addComponent(jtfNetworkDrive, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrbRemovableDrive)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Parâmetros Gerais", jPanel3);

        jLabel7.setText("Horários:");

        jpWeekDays.setBorder(javax.swing.BorderFactory.createTitledBorder("Dias da Semana:"));

        jcbSun.setText("Domingo");

        jcbMon.setText("Segunda-Feira");

        jcbTue.setText("Terça-Feira");

        jcbWed.setText("Quarta-Feira");

        jcbThu.setText("Quinta-Feira");

        jcbFri.setText("Sexta-Feira");

        jcbSat.setText("Sábado");

        javax.swing.GroupLayout jpWeekDaysLayout = new javax.swing.GroupLayout(jpWeekDays);
        jpWeekDays.setLayout(jpWeekDaysLayout);
        jpWeekDaysLayout.setHorizontalGroup(
            jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpWeekDaysLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbSat)
                    .addGroup(jpWeekDaysLayout.createSequentialGroup()
                        .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbSun)
                            .addComponent(jcbWed))
                        .addGap(36, 36, 36)
                        .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbMon)
                            .addComponent(jcbThu))
                        .addGap(40, 40, 40)
                        .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbFri)
                            .addComponent(jcbTue))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jpWeekDaysLayout.setVerticalGroup(
            jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpWeekDaysLayout.createSequentialGroup()
                .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpWeekDaysLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jcbSun))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpWeekDaysLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbMon)
                            .addComponent(jcbTue))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpWeekDaysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbWed)
                    .addComponent(jcbThu)
                    .addComponent(jcbFri))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbSat)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jlTimes);

        jbAddTime.setText("+");
        jbAddTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddTimeActionPerformed(evt);
            }
        });

        jbRemoveTime.setText("-");
        jbRemoveTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveTimeActionPerformed(evt);
            }
        });

        try {
            jftTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jcbActivated.setText("Habilitar o backup automático do Banco de Dados (recomendável)");
        jcbActivated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbActivatedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jcbActivated)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jpWeekDays, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jftTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbAddTime, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jbRemoveTime, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbActivated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpWeekDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jftTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbAddTime, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbRemoveTime, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Backup Automático", jPanel7);

        jLabel2.setText("Programa para o Backup (pg_dump):");

        jbOpenBackupApplication.setText("...");
        jbOpenBackupApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenBackupApplicationActionPerformed(evt);
            }
        });

        jLabel3.setText("Programa para o Restore (pg_restore):");

        jbOpenRestoreApplication.setText("...");
        jbOpenRestoreApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenRestoreApplicationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(0, 217, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtfRestoreApplication)
                            .addComponent(jtfBackupApplication))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbOpenBackupApplication, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbOpenRestoreApplication, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfBackupApplication, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbOpenBackupApplication))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfRestoreApplication, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbOpenRestoreApplication))
                .addContainerGap(274, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ferramentas", jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Postgre Server:"));

        jLabel16.setText("Usuário:");

        jLabel1.setText("Host:");

        jLabel17.setText("Senha:");

        jLabel4.setText("Porta:");

        jspPort.setModel(new javax.swing.SpinnerNumberModel(0, 0, 65535, 1));

        jLabel5.setText("B. Dados:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpfPassword)
                    .addComponent(jtfUserName)
                    .addComponent(jtfHost)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jtfDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspPort, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jspPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jtfUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jpfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jbSave.setText("Gravar");
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jbCancel.setText("Cancelar");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbSave)
                    .addComponent(jbCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        save();
    }//GEN-LAST:event_jbSaveActionPerformed

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jbRemoveTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveTimeActionPerformed
        removeTime();
    }//GEN-LAST:event_jbRemoveTimeActionPerformed

    private void jbAddTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddTimeActionPerformed
        insertTime();
    }//GEN-LAST:event_jbAddTimeActionPerformed

    private void jcbActivatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbActivatedActionPerformed
        configAutomaticBackupControls();
    }//GEN-LAST:event_jcbActivatedActionPerformed

    private void jbOpenBackupApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenBackupApplicationActionPerformed
        openBackupApplication();
    }//GEN-LAST:event_jbOpenBackupApplicationActionPerformed

    private void jbOpenRestoreApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenRestoreApplicationActionPerformed
        openRestoreApplication();
    }//GEN-LAST:event_jbOpenRestoreApplicationActionPerformed

    private void jrbNetworkDriveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbNetworkDriveActionPerformed
        jtfNetworkDrive.setEnabled(true);
    }//GEN-LAST:event_jrbNetworkDriveActionPerformed

    private void jrbRemovableDriveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbRemovableDriveActionPerformed
        jtfNetworkDrive.setEnabled(false);
    }//GEN-LAST:event_jrbRemovableDriveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgBackupMode;
    private javax.swing.ButtonGroup btgDestDrive;
    private javax.swing.ButtonGroup btgExtractBlobs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbAddTime;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOpenBackupApplication;
    private javax.swing.JButton jbOpenRestoreApplication;
    private javax.swing.JButton jbRemoveTime;
    private javax.swing.JButton jbSave;
    private javax.swing.JCheckBox jcbActivated;
    private javax.swing.JCheckBox jcbFri;
    private javax.swing.JCheckBox jcbMon;
    private javax.swing.JCheckBox jcbSat;
    private javax.swing.JCheckBox jcbSun;
    private javax.swing.JCheckBox jcbThu;
    private javax.swing.JCheckBox jcbTue;
    private javax.swing.JCheckBox jcbWed;
    private javax.swing.JFormattedTextField jftTime;
    private javax.swing.JList<String> jlTimes;
    private javax.swing.JPanel jpWeekDays;
    private javax.swing.JPasswordField jpfPassword;
    private javax.swing.JRadioButton jrbBackupMode1;
    private javax.swing.JRadioButton jrbBackupMode2;
    private javax.swing.JRadioButton jrbExtractBlobs;
    private javax.swing.JRadioButton jrbNetworkDrive;
    private javax.swing.JRadioButton jrbNonExtractBlobs;
    private javax.swing.JRadioButton jrbRemovableDrive;
    private javax.swing.JSpinner jspPort;
    private javax.swing.JTextField jtfBackupApplication;
    private javax.swing.JTextField jtfDatabase;
    private javax.swing.JTextField jtfHost;
    private javax.swing.JTextField jtfNetworkDrive;
    private javax.swing.JTextField jtfRestoreApplication;
    private javax.swing.JTextField jtfUserName;
    // End of variables declaration//GEN-END:variables
}
