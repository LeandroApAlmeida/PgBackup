package postgre.backup.forms;

import dialogs.JOptionPaneEx;
import java.awt.Cursor;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import postgre.backup.classes.BackupManager;
import postgre.backup.classes.Drive;
import postgre.backup.classes.DriveTypeEnum;
import postgre.backup.classes.DrivesManager;
import postgre.backup.run.Application;

public class ManualBackupDialog extends javax.swing.JDialog implements Runnable {
    
    private class TimerTaskDevice extends TimerTask {
        @Override
        public void run() {
            listDrives();
        }        
    }

    private final DrivesManager drivesManager;
    private List<Drive> drives;
    private Timer timer;
    
    public ManualBackupDialog() {
        super(null, ModalityType.TOOLKIT_MODAL);
        drivesManager = new DrivesManager();
        timer = new Timer();
        initComponents();
        setIconImage(Application.getDefaultIcon());        
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
            if (drives.size() > 0) {
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
        stopTimer();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jbBackup.setEnabled(false);
        jbCancel.setEnabled(false);
        Drive selectedDrive = drives.get(jcbDrives.getSelectedIndex());
        String drive = selectedDrive.getLetter();
        try {
            jtaLog.setText("Processando o Backup...");
            BackupManager.doBackup(drive);
            jtaLog.setText("Backup realizado com sucesso!");
            int opt = JOptionPaneEx.showConfirmDialog(this, "Deseja ejetar o dispositivo USB?",
            "Atenção!", JOptionPaneEx.YES_NO_OPTION, JOptionPaneEx.INFORMATION_MESSAGE);
            if (opt == JOptionPaneEx.YES_OPTION) {
                jtaLog.setText("Ejetando o dispositivo USB... Não remova antes de concluído.");
                selectedDrive.eject();
                jtaLog.setText("");
                JOptionPaneEx.showMessageDialog(
                    this,
                    "Agora você pode remover o dispositivo USB de backup.",
                    "Concluído com Sucesso!",
                    JOptionPaneEx.INFORMATION_MESSAGE
                );
            }            
            setVisible(false);
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
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BACKUP MANUAL DO BANCO DE DADOS");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jtaLog.setEditable(false);
        jtaLog.setColumns(20);
        jtaLog.setRows(5);
        jtaLog.setFocusable(false);
        jScrollPane1.setViewportView(jtaLog);

        jbCancel.setText("Cancelar");
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

        jLabel2.setText("Mensagem:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1))
                    .addComponent(jcbDrives, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 504, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jcbDrives, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancel)
                    .addComponent(jbBackup))
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbBackup;
    private javax.swing.JButton jbCancel;
    private javax.swing.JComboBox<String> jcbDrives;
    private javax.swing.JTextArea jtaLog;
    // End of variables declaration//GEN-END:variables
}
