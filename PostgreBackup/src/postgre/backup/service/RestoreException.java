
package postgre.backup.service;

/**
 * Exceção associada a erro no processo de restore do banco de dados PostgreSQL.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class RestoreException extends Exception {    
    
    public RestoreException(String message) {
        super(message);
    }    

}