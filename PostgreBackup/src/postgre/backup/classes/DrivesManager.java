package postgre.backup.classes;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
//import windows.utilities.PowerShell.OSDetector;

public class DrivesManager {
    
    private <T extends Enum<T> & HasNativeValue> T fromNative(Class<T> clazz, int value) {
        for (T c : clazz.getEnumConstants()) {
            if (c.getNativeValue() == value) {
                return c;
            }
        }
        return null;
    }

    public List<Drive> getDrives() {
        List<Drive> result = new ArrayList<>();
        ActiveXComponent axWMI = new ActiveXComponent("winmgmts://");
        try {
            Variant devices = axWMI.invoke("ExecQuery", 
            new Variant("Select DeviceID,DriveType,FileSystem from Win32_LogicalDisk"));
            EnumVariant deviceList = new EnumVariant(devices.toDispatch());
            while (deviceList.hasMoreElements()) {
                Dispatch item = deviceList.nextElement().toDispatch();
                String drive = Dispatch.call(item, "DeviceID").toString().toUpperCase();
                DriveTypeEnum driveType = fromNative(DriveTypeEnum.class, 
                Dispatch.call(item, "DriveType").getInt());
                String fileSystem = Dispatch.call(item, "FileSystem").toString();
                result.add(new Drive(drive, fileSystem, driveType));
            }
            return result;
        } finally {
            axWMI.safeRelease();
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