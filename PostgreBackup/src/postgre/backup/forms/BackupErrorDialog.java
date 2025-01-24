package postgre.backup.forms;

import dialogs.JOptionPaneEx;
import java.util.Date;
import java.util.Timer;
import postgre.backup.classes.TimerTaskBackup;
import postgre.backup.run.Application;

public class BackupErrorDialog extends javax.swing.JDialog {

    
    public BackupErrorDialog() {
    
        super(null, ModalityType.TOOLKIT_MODAL);
        
        initComponents();
        
        setIconImage(Application.getDefaultIcon());
    
    }
    
    
    private void showManualBackupDialog() {
        WindowManager.showManualBackupDialogUnchecked();
    }
    
    
    private void startNetworkDriveBackup() {
        Timer backupTimer = new Timer();
        backupTimer.schedule(new TimerTaskBackup(), new Date());
        setVisible(false);
    }
    
    
    private void abortBackup() {
    
        int opt = JOptionPaneEx.showConfirmDialog(
            this,
            "Cancelar o backup do Banco de Dados?",
            "Atenção!",
            JOptionPaneEx.INFORMATION_MESSAGE,
            JOptionPaneEx.YES_NO_OPTION
        );
        
        if (opt == JOptionPaneEx.YES_OPTION) {
            setVisible(false);
        }
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jbAbort = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("ERRO NO BACKUP DO BANCO DE DADOS");
        setResizable(false);

        jScrollPane2.setBorder(null);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Backup do Banco de Dados agendado para este horário, porém não foi possível localizar o drive de rede de destino. Por favor, verifique se há conexão com a internet e se há defeitos em cabos e interfaces de comunicação. Após corrigir o problema, clique no botão Tentar Novamente para concluir o processo.\n\nÉ altamente recomendável que se conclua o backup do banco de dados, portanto, se não conseguir estabelecer uma conexão com a rede, conecte um dispositivo removível de memória como pendrive ou cartão de memória em uma porta USB do computador e clique no botão Drive Removível para proceder a um backup manual.\n\nEm caso de dúvidas e caso o problema persista, contacte o administrador do sistema para a resolução.");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jTextArea1.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea1);

        jbAbort.setText("Abortar o Backup");
        jbAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAbortActionPerformed(evt);
            }
        });

        jButton1.setText("Drive Removível");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Tentar Novamente");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbAbort, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAbort)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAbortActionPerformed
        abortBackup();
    }//GEN-LAST:event_jbAbortActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showManualBackupDialog();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        startNetworkDriveBackup();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbAbort;
    // End of variables declaration//GEN-END:variables
}
