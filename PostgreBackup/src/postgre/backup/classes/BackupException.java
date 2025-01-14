package postgre.backup.classes;

/**
 * Exceção associada a erro no processo de backup do banco de dados PostgreSQL.
 * @author Leandro Aparecido de Almeida
 */
public class BackupException extends Exception {
    /**
     * Constructor que recebe a mensagem do erro como parâmetro.
     * @param message mensagem do erro.
     */
    public BackupException(String message) {
        super(message);
    }
}