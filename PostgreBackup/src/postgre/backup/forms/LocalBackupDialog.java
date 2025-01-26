package postgre.backup.forms;

import dialogs.JOptionPaneEx;
import java.awt.Cursor;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import postgre.backup.service.BackupManager;
import postgre.backup.service.Drive;
import postgre.backup.service.DriveTypeEnum;
import postgre.backup.service.DrivesManager;
import postgre.backup.service.ServerSettings;
import postgre.backup.run.Application;

public class LocalBackupDialog extends javax.swing.JDialog implements Runnable {
    
    
    private class TimerTaskDevice extends TimerTask {
        @Override
        public void run() {
            listDrives();
        }        
    }

    
    private final ServerSettings serverSettings = new ServerSettings();
    
    private final DrivesManager drivesManager;
    
    private List<Drive> drives;
    
    private Timer timer;
    
    
    public LocalBackupDialog() {
    
        super(null, ModalityType.TOOLKIT_MODAL);
        
        drivesManager = new DrivesManager();
        timer = new Timer();
        
        initComponents();
        
        setIconImage(Application.getDefaultIcon());
        
        jlProgress.setVisible(false);
        
        listDrives();
        configControls();
        startTimer();
    
    }
    
    
    private void listDrives() {
        
        boolean updated = false;
        
        List<Drive> newList = drivesManager.getDrives(DriveTypeEnum.RemovableDisk);
        
        if (drives != null) {
        
            if (newList.size() != drives.size()) {
            
                updated = true;
            
            } else {
            
                for (Drive drive1 : newList) {
                    
                    boolean equals = false;
                    
                    for (Drive drive2 : drives) {
                        if (drive1.equals(drive2)) {
                            equals = true;
                            break;
                        }
                    }
                    
                    if (!equals) {
                        updated = true;
                        break;
                    }
                    
                }
                
                if (!updated) {
                    
                    for (Drive drive1 : drives) {
                        
                        boolean equals = false;
                        
                        for (Drive drive2 : newList) {
                            if (drive1.equals(drive2)) {
                                equals = true;
                                break;
                            }
                        }
                        
                        if (!equals) {
                            updated = true;
                            break;
                        }
                    
                    }
                    
                }
            }
        } else {
        
            updated = true;
        
        }
        
        if (updated) {
        
            Drive selectedDrive = null;
            
            if (jcbDrives.getSelectedIndex() >= 0 && drives != null) {
                selectedDrive = drives.get(jcbDrives.getSelectedIndex());
            }
            
            drives = newList;
            
            String[] listItems = new String[drives.size()];
            for (int i = 0; i < drives.size(); i++) {
                listItems[i] = drives.get(i).toString();
            }
            
            jcbDrives.setModel(new javax.swing.DefaultComboBoxModel<>(listItems));
            
            if (!drives.isEmpty()) {
                
                int index = 0;
                
                if (selectedDrive != null) {
                    for (int i = 0; i < drives.size(); i++) {
                        if (drives.get(i).equals(selectedDrive)) {
                            index = i;
                            break;
                        }
                    }
                }
                
                jcbDrives.setSelectedIndex(index);
                
            }
            
        }
        
    }
    
    
    private void configControls() {
        if (jcbDrives.getSelectedIndex() >= 0) {
            jbBackup.setEnabled(true);           
        } else {
            jbBackup.setEnabled(false); 
        }
    }
    
    
    @Override
    public void run() {
        
        jlProgress.setVisible(true);
        jcbDrives.setEnabled(false);

        stopTimer();

        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        jbBackup.setEnabled(false);
        jbCancel.setEnabled(false);

        Drive selectedDrive = drives.get(jcbDrives.getSelectedIndex());
        String drive = selectedDrive.getLetter();

        try {

            jtaLog.setText("Processando o Backup...");

            File backupFile = new BackupManager().doBackup(drive);

            StringBuilder sb = new StringBuilder();
            
            sb.append("Backup realizado com sucesso!");
            sb.append("\n\n");
            
            sb.append("Arquivo de Backup: ");
            sb.append(backupFile.getAbsoluteFile());
            sb.append("\n");
            
            sb.append("Modo de Backup: ");
            sb.append(serverSettings.getBackupMode() == ServerSettings
            .EXTRACT_DATA_ONLY ? "Dados Apenas" : "Estrutura e Dados");
            sb.append("\n");
            
            sb.append("Extrair Blobs: ");
            sb.append(serverSettings.extractBlobs() ? "sim" : "não");
            
            jtaLog.setText(sb.toString());
            
            //setVisible(false);
            
        } catch (Exception ex) {

            jbBackup.setEnabled(true);
            jbCancel.setEnabled(true);

            jtaLog.setText(
                "Erro ao fazer o Backup do Banco de Dados:\n\n" +
                ex.getMessage()
            );

            startTimer();

        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
        jlProgress.setVisible(false); 
        jcbDrives.setEnabled(true);
        jbBackup.setEnabled(true);
        jbCancel.setEnabled(true);
        
    }
    
    
    private void doBackup() {
        if (jcbDrives.getSelectedIndex() >= 0) {
            new Thread(this).start();
        } else {
            JOptionPaneEx.showMessageDialog(
                this,
                "Selecione o drive de destino do backup.",
                "Erro",
                JOptionPaneEx.ERROR_MESSAGE
            );
        }
    }
    
    
    private void abortBackup() {
        setVisible(false);
        stopTimer();
    }
    
    
    private void startTimer() {
        timer.schedule(new TimerTaskDevice(), 1000, 2000);
    }
    
    
    private void stopTimer() {
        timer.cancel();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jbCancel = new javax.swing.JButton();
        jbBackup = new javax.swing.JButton();
        jcbDrives = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jlProgress = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BACKUP LOCAL DO BANCO DE DADOS");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jtaLog.setEditable(false);
        jtaLog.setColumns(20);
        jtaLog.setLineWrap(true);
        jtaLog.setRows(5);
        jtaLog.setWrapStyleWord(true);
        jtaLog.setFocusable(false);
        jScrollPane1.setViewportView(jtaLog);

        jbCancel.setText("Fechar");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        jbBackup.setText("Fazer o Backup");
        jbBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBackupActionPerformed(evt);
            }
        });

        jcbDrives.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbDrives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDrivesActionPerformed(evt);
            }
        });

        jLabel1.setText("Drive de Destino:");

        jlProgress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/postgre/backup/forms/progress.gif"))); // NOI18N
        jlProgress.setText("jLabel3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                    .addComponent(jcbDrives, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jlProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jcbDrives, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbBackup)
                    .addComponent(jlProgress))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        abortBackup();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jbBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBackupActionPerformed
        doBackup();
    }//GEN-LAST:event_jbBackupActionPerformed

    private void jcbDrivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDrivesActionPerformed
        configControls();
    }//GEN-LAST:event_jcbDrivesActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        stopTimer();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbBackup;
    private javax.swing.JButton jbCancel;
    private javax.swing.JComboBox<String> jcbDrives;
    private javax.swing.JLabel jlProgress;
    private javax.swing.JTextArea jtaLog;
    // End of variables declaration//GEN-END:variables
}
