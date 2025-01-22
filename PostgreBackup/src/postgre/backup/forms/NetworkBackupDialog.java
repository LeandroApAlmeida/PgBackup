package postgre.backup.forms;

import java.awt.Cursor;
import postgre.backup.classes.BackupManager;
import postgre.backup.classes.ServerSettings;
import postgre.backup.run.Application;

public class NetworkBackupDialog extends javax.swing.JDialog implements Runnable {

    
    private final ServerSettings serverSettings = ServerSettings.getInstance();
    
    
    public NetworkBackupDialog() {
        
        super(null, ModalityType.TOOLKIT_MODAL);
        
        initComponents();
        
        setIconImage(Application.getDefaultIcon());
        
        jlProgress.setVisible(false);
    
    }
    
    
    private void doBackup() {        
        new Thread(this).start();
    }

    
    @Override
    public void run() {
        
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        jbBackup.setEnabled(false);
        jbClose.setEnabled(false);
        jlProgress.setVisible(true);        
        
        try {
            
            jtaLog.setText("Processando o Backup...");
            
            BackupManager.doBackup(serverSettings.getNetworkDrive());
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("Backup realizado com sucesso!");
            sb.append("\n");
            
            sb.append("Arquivo de Backup: ");
            sb.append(serverSettings.getNetworkDrive());
            sb.append("\n");
            
            sb.append("Modo de Backup: ");
            sb.append(serverSettings.getBackupMode() == ServerSettings
            .EXTRACT_DATA_ONLY ? "Dados Apenas" : "Estrutura e Dados");
            sb.append("\n");
            
            sb.append("Extrair Blobs: ");
            sb.append(String.valueOf(serverSettings.extractBlobs()));
            
            jtaLog.setText(sb.toString());
            
        } catch (Exception ex) {
            
            jtaLog.setText(
                "Erro ao fazer o Backup do Banco de Dados:\n\n" +
                ex.getMessage()
            );
        
        }
        
        jlProgress.setVisible(false);
        jbBackup.setEnabled(true);
        jbClose.setEnabled(true);
        
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbBackup = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jlProgress = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BACKUP EM DRIVE REMOTO DO BANCO DE DADOS");
        setResizable(false);

        jbBackup.setText("Fazer o Backup");
        jbBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBackupActionPerformed(evt);
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

        jlProgress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/postgre/backup/forms/progress.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 414, Short.MAX_VALUE)
                        .addComponent(jlProgress))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbBackup)
                            .addComponent(jbClose))
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jlProgress)
                        .addContainerGap())))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBackupActionPerformed
        doBackup();
    }//GEN-LAST:event_jbBackupActionPerformed

    private void jbCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCloseActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jbCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbBackup;
    private javax.swing.JButton jbClose;
    private javax.swing.JLabel jlProgress;
    private javax.swing.JTextArea jtaLog;
    // End of variables declaration//GEN-END:variables
}
