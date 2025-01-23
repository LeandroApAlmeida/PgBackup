package postgre.backup.classes;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DrivesManager {
    
    
    private <T extends Enum<T> & HasNativeValue> T fromNative(Class<T> _class, int value) {
        
        for (T c : _class.getEnumConstants()) {
            if (c.getNativeValue() == value) {
                return c;
            }
        }
        
        return null;
        
    }

    
    public List<Drive> getDrives() {
        
        ComThread.InitSTA();
        
        List<Drive> result = new ArrayList<>();
        
        ActiveXComponent activex = new ActiveXComponent("winmgmts://");
        
        try {
            
            String query1 = "SELECT * FROM Win32_LogicalDisk";
            
            Variant logicalDisks = activex.invoke("ExecQuery", new Variant(query1));
            
            EnumVariant logicalDisksList = new EnumVariant(logicalDisks.toDispatch());
            
            while (logicalDisksList.hasMoreElements()) {
                
                Dispatch logicalDisk = logicalDisksList.nextElement().toDispatch();

                String drive = Dispatch.call(logicalDisk, "DeviceID").toString().toUpperCase();
                
                DriveTypeEnum driveType = fromNative(DriveTypeEnum.class, Dispatch.call(logicalDisk, "DriveType").getInt());
                
                String fileSystem = Dispatch.call(logicalDisk, "FileSystem").toString();
                
                StringBuilder query2 = new StringBuilder();
                query2.append("ASSOCIATORS OF {Win32_LogicalDisk.DeviceID='");
                query2.append(drive);
                query2.append("'} WHERE AssocClass=Win32_LogicalDiskToPartition");
                
                Variant partitions = activex.invoke("ExecQuery", new Variant(query2.toString()));
                
                EnumVariant partitionsList = new EnumVariant(partitions.toDispatch());
                
                while (partitionsList.hasMoreElements()) {
                    
                    Dispatch partition = partitionsList.nextElement().toDispatch();
                    
                    StringBuilder query3 = new StringBuilder();
                    query3.append("ASSOCIATORS OF {Win32_DiskPartition.DeviceID='");
                    query3.append(Dispatch.call(partition, "DeviceID").toString());
                    query3.append("'} WHERE AssocClass=Win32_DiskDriveToDiskPartition");

                    Variant disks = activex.invoke("ExecQuery", new Variant(query3.toString()));

                    EnumVariant disksList = new EnumVariant(disks.toDispatch());

                    while (disksList.hasMoreElements()) {

                        Dispatch disk = disksList.nextElement().toDispatch();
                        
                        String interfaceType = Dispatch.call(disk, "InterfaceType").toString().toUpperCase();
                        
                        if (interfaceType.equals("USB")) {
                            driveType = DriveTypeEnum.RemovableDisk;
                            break;
                        }

                    }
                    
                }
                
                result.add(new Drive(drive, fileSystem, driveType));
            
            }
            
            return result;
        
        } finally {
        
            activex.safeRelease();
            
            ComThread.Release();
        
        }
        
    }

    
    public Drive getDrive(File file) {
    
        Drive driveRef = null;
        
        List<Drive> drives = getDrives();
        
        for (Drive drive : drives) {
            if (file.getAbsolutePath().startsWith(drive.getLetter())) {
                driveRef = drive;
                break;
            }
        }
        
        return driveRef;
    
    }
    
    
    public List<Drive> getDrives(DriveTypeEnum driveType) {
        
        List<Drive> drives = getDrives();
        List<Drive> selectedDrives = new ArrayList<>();
        
        for (Drive drive : drives) {
        
            switch (driveType) {
            
                case LocalDisk: {
                    if (drive.getDriveType() == DriveTypeEnum.LocalDisk) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case RemovableDisk: {
                    if (drive.getDriveType() == DriveTypeEnum.RemovableDisk) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case NetworkDrive: {
                    if (drive.getDriveType() == DriveTypeEnum.NetworkDrive) {
                        selectedDrives.add(drive);
                    }
                } break;
            
            }
        
        }
        
        return selectedDrives;
    
    }

    
}