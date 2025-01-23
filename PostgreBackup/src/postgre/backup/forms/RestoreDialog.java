package postgre.backup.forms;

import dialogs.FileChooserDialog;
import java.awt.Cursor;
import javax.swing.filechooser.FileNameExtensionFilter;
import postgre.backup.classes.BackupManager;
import postgre.backup.classes.RestoreException;
import postgre.backup.run.Application;

public class RestoreDialog extends javax.swing.JDialog implements Runnable {

    
    public RestoreDialog() {
        
        super(null, ModalityType.TOOLKIT_MODAL);
        
        initComponents();
        
        setIconImage(Application.getDefaultIcon());
        
        jlProgress.setVisible(false);
    
    }
  
    
    private void doRestore() {
        new Thread(this).start();
    }
    
    
    private void openOutputBackupFile() {
    
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Arquivo de Backup do PostgreSQL (.pgback)",
            "pgback"
        );
        
        FileChooserDialog fchooser = new FileChooserDialog(
            "Abrir Arquivo de Backup",
            filter
        );
        
        int opc = fchooser.showOpenDialog(this);
        
        if (opc == FileChooserDialog.APPROVE_OPTION) {
            jtfOutputBackupFile.setText(fchooser.getSelectedFile().getAbsolutePath());
        }
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    
    }
    
    
    @Override
    public void run() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        jbRestore.setEnabled(false);
        jbOpenOutputBackupFile.setEnabled(false);
        jtfOutputBackupFile.setEnabled(false);
        jbClose.setEnabled(false);
        jlProgress.setVisible(true);        
        
        try {
        
            if (!jtfOutputBackupFile.getText().equals("")) {
            
                jtaLog.setText("Processando o Restore...");
                
                BackupManager.doRestore(jtfOutputBackupFile.getText());
                
                StringBuilder sb = new StringBuilder();
                
                sb.append("Restore realizado com sucesso!");
                jtaLog.setText(sb.toString());
            
            } else {
                
                throw new RestoreException(
                    "Arquivo de backup não definido."
                );
            
            }
        
        } catch (Exception ex) {
        
            jtaLog.setText(
                "Erro ao Restaurar o Banco de Dados:\n\n" +
                ex.getMessage()
            );
            
        }
        
        jlProgress.setVisible(false);
        jbOpenOutputBackupFile.setEnabled(true);
        jtfOutputBackupFile.setEnabled(true);
        jbRestore.setEnabled(true);
        jbClose.setEnabled(true);
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbRestore = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jtfOutputBackupFile = new javax.swing.JTextField();
        jbOpenOutputBackupFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jlProgress = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RESTAURAR O BACKUP DO BANCO DE DADOS");
        setResizable(false);

        jbRestore.setText("Restaurar o Backup");
        jbRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRestoreActionPerformed(evt);
            }
        });

        jbClose.setText("Fechar");
        jbClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCloseActionPerformed(evt);
            }
        });

        jtaLog.setEditable(false);
        jtaLog.setColumns(20);
        jtaLog.setLineWrap(true);
        jtaLog.setRows(5);
        jtaLog.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jtaLog);

        jtfOutputBackupFile.setEditable(false);
        jtfOutputBackupFile.setBackground(new java.awt.Color(255, 255, 255));

        jbOpenOutputBackupFile.setText("...");
        jbOpenOutputBackupFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenOutputBackupFileActionPerformed(evt);
            }
        });

        jLabel1.setText("Arquivo  de Backup:");

        jlProgress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/postgre/backup/forms/progress.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jtfOutputBackupFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbOpenOutputBackupFile, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 358, Short.MAX_VALUE)
                        .addComponent(jlProgress)
                        .addGap(14, 14, 14))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfOutputBackupFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbOpenOutputBackupFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbRestore)
                    .addComponent(jbClose))
                .addGap(11, 11, 11))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlProgress)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRestoreActionPerformed
        doRestore();
    }//GEN-LAST:event_jbRestoreActionPerformed

    private void jbCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jbCloseActionPerformed

    private void jbOpenOutputBackupFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenOutputBackupFileActionPerformed
        openOutputBackupFile();
    }//GEN-LAST:event_jbOpenOutputBackupFileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbOpenOutputBackupFile;
    private javax.swing.JButton jbRestore;
    private javax.swing.JLabel jlProgress;
    private javax.swing.JTextArea jtaLog;
    private javax.swing.JTextField jtfOutputBackupFile;
    // End of variables declaration//GEN-END:variables
}
