package postgre.backup.classes;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.spi.FileSystemProvider;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jacob.com.ComThread;

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
    
    
    public void eject() throws Exception {

        ComThread.InitSTA();
        
        try {
        
            ActiveXComponent wmi = new ActiveXComponent("winmgmts:\\\\.\\root\\cimv2");
            
            Dispatch classInstance = wmi.getObject();
            
            String query = "SELECT * FROM Win32_LogicalDisk WHERE DeviceID='" + letter + "'";
            
            Variant logicalDisks = Dispatch.call(
                classInstance,
                "ExecQuery",
                new Variant(query)
            );

            Dispatch diskSet = logicalDisks.toDispatch();
            
            Variant diskVariant = Dispatch.get(diskSet, "Count");
            
            int diskCount = diskVariant.getInt();

            for (int i = 0; i < diskCount; i++) {
            
                Dispatch disk = Dispatch.call(diskSet, "ItemIndex", i).toDispatch();
                
                String volumeName = Dispatch.get(disk, "VolumeName").toString();
                
                remove(volumeName);
            
            }
            
        } finally {
            ComThread.Release();
        }
        
    }

    public static void remove(String volumeName) {
        
        ComThread.InitSTA();
        
        try {
        
            ActiveXComponent wmi = new ActiveXComponent("winmgmts:\\\\.\\root\\cimv2");
            
            Dispatch classInstance = wmi.getObject();
            
            String query = "SELECT * FROM Win32_Volume WHERE Name LIKE '%" + volumeName + "%'";
            
            Variant volumes = Dispatch.call(
                classInstance,
                "ExecQuery",
                new Variant(query)
            );

            Dispatch volumeSet = volumes.toDispatch();
            
            Variant volumeVariant = Dispatch.get(volumeSet, "Count");
            
            int volumeCount = volumeVariant.getInt();

            for (int i = 0; i < volumeCount; i++) {
                Dispatch volume = Dispatch.call(volumeSet, "ItemIndex", i).toDispatch();
                Dispatch.call(volume, "Dismount", new Variant(true), new Variant(false));
                Dispatch.call(volume, "SetVolumeDirty");
            }

            query = "SELECT * FROM Win32_DiskDrive WHERE DeviceID LIKE '%" + volumeName + "%'";
            
            Variant drives = Dispatch.call(
                classInstance,
                "ExecQuery",
                new Variant(query)
            );

            Dispatch driveSet = drives.toDispatch();
            Variant driveVariant = Dispatch.get(driveSet, "Count");
            
            int driveCount = driveVariant.getInt();

            for (int i = 0; i < driveCount; i++) {
                Dispatch drive = Dispatch.call(driveSet, "ItemIndex", i).toDispatch();
                Dispatch.call(drive, "Disable");
            }
            
        } finally {
            ComThread.Release();
        }
        
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