package postgre.backup.service;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.spi.FileSystemProvider;

public final class Drive {

    
    private final String fileSystem;
    
    private final DriveTypeEnum driveType;
    
    private final String letter;
    
    private String name;

    
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

    
    public String getFileSystem() {
        return fileSystem;
    }

    
    public DriveTypeEnum getDriveType() {
        return driveType;
    }

    
    public String getLetter() {
        return letter;
    }

    
    public String getName() {
        return name;
    }
 
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Drive) {
            return this.letter.toUpperCase().equals(((Drive)obj).letter
            .toUpperCase());
        } else {
            return false;
        }
    }

    
    @Override
    public String toString() {
        return name + " (" + letter + ")";
    }

    
}