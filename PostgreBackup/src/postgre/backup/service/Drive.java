package postgre.backup.service;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.spi.FileSystemProvider;


/**
 * Classe que representa um drive, que pode ser uma Unidade USB, HD interno,
 * drive de rede, ou outra unidade mapeada.
 * 
 * @author Leandro Aparecido de Almeida
 */
public final class Drive {

    
    /**Tipo de sistema de arquivos (FAT, FAT-32, NTFS, etc.).*/
    private final String fileSystem;
    
    /**Tipo de drive.*/
    private final DriveTypeEnum driveType;
    
    /**Letra do drive (C:, D:, E:, etc.).*/
    private final String letter;
    
    /**Rótulo do drive.*/
    private String name;


    /**
     * Constructor da classe.
     * 
     * @param letter letra do drive (C:, D:, E:, etc.).
     * 
     * @param fileSystem tipo do sistema de arquivos (FAT, FAT-32, NTFS, etc.).
     * 
     * @param driveType tipo do drive.
     */
    public Drive(String letter, String fileSystem, DriveTypeEnum driveType) {
    
        this.letter = letter;
        this.fileSystem = fileSystem;
        this.driveType = driveType;
        
        try {
            
            FileSystemProvider fsProvider = FileSystems.getDefault().provider();
        
            FileStore fileStore = fsProvider.getFileStore(new File(letter).toPath());
            
            this.name = fileStore.name();
            
            if (this.name == null || this.name.equals("")) {
                this.name = "Drive";
            }
        
        } catch (Exception ex) {
            this.name = "Drive";
        }
        
    }

    
    /**
     * Obter o tipo de sistema de arquivos (FAT, FAT-32, NTFS, etc.).
     * 
     * @return tipo do sistema de arquivos.
     */
    public String getFileSystem() {
        return fileSystem;
    }

    
    /**
     * Obter o tipo de drive.
     * 
     * @return tipo de drive.
     */
    public DriveTypeEnum getDriveType() {
        return driveType;
    }

    
    /**
     * Obter a letra do drive (C:, D:, E:, etc.).
     * 
     * @return letra do drive.
     */
    public String getLetter() {
        return letter;
    }

    
    /**
     * Obter o rótulo do drive.
     * 
     * @return rótulo do drive.
     */
    public String getName() {
        return name;
    }
 
    
    /**
     * Comparar drives. O critério de comparação é pela letra do drive (C:, D:, 
     * E:, etc.).
     * 
     * @param obj outro drive a ser comparado.
     * 
     * @return Se true, é o mesmo drive. Se false, são drives diferentes.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Drive) {
                return this.letter.toUpperCase().equals(((Drive)obj).letter
                .toUpperCase());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    
    /**
     * Sobrescrito para retornar a letra do drive.
     * 
     * @return letra do drive.
     */
    @Override
    public String toString() {
        return name + " (" + letter + ")";
    }

    
}