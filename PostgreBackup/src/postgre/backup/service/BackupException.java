package postgre.backup.service;

/**
 * Exceção associada a erro no processo de backup do banco de dados PostgreSQL.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class BackupException extends Exception {
    
    public BackupException(String message) {
        super(message);
    }
   
}