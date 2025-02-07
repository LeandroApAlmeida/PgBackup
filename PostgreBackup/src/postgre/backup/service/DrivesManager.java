package postgre.backup.service;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para o gerenciamento de drives do sistema de arquivos.
 * 
 * @author Leandro Aparecido de Almeida
 */
public class DrivesManager {
    
    
    private <T extends Enum<T> & HasNativeValue> T fromNative(Class<T> _class, int value) {
        
        for (T c : _class.getEnumConstants()) {
            if (c.getNativeValue() == value) {
                return c;
            }
        }
        
        return null;
        
    }

    
    /**
     * Obter a lista com todos os drives do sistema. Nesta lista, serão retornados
     * todos os tipos de drives montados, seja USB, de rede, local, ou outro.
     * 
     * <br><br>
     * 
     * No windows, a busca pelos drives será via WMI (Windows Media Interface),
     * com acessado nativo via biblioteca JACOB (Java-Com Bridge).
     * 
     * @return lista com todos os drives do sistema. 
     */
    public List<Drive> getDrives() {
        
        ComThread.InitSTA();

        List<Drive> result = new ArrayList<>();
        
        ActiveXComponent activex = new ActiveXComponent("winmgmts://");
        
        try {
            
            // Seleciona todos os discos lógicos.
            String query1 = "SELECT * FROM Win32_LogicalDisk";
            
            Variant logicalDisks = activex.invoke("ExecQuery", new Variant(query1));
            
            EnumVariant logicalDisksList = new EnumVariant(logicalDisks.toDispatch());
            
            while (logicalDisksList.hasMoreElements()) {
                
                Dispatch logicalDisk = logicalDisksList.nextElement().toDispatch();

                String drive = Dispatch.call(logicalDisk, "DeviceID").toString().toUpperCase();
                
                DriveTypeEnum driveType = fromNative(DriveTypeEnum.class, Dispatch.call(logicalDisk, "DriveType").getInt());
                
                String fileSystem = Dispatch.call(logicalDisk, "FileSystem").toString();
                
                // Obtém todas as partições associadas ao disco lógico.
                StringBuilder query2 = new StringBuilder();
                query2.append("ASSOCIATORS OF {Win32_LogicalDisk.DeviceID='");
                query2.append(drive);
                query2.append("'} WHERE AssocClass=Win32_LogicalDiskToPartition");
                
                Variant partitions = activex.invoke("ExecQuery", new Variant(query2.toString()));
                
                EnumVariant partitionsList = new EnumVariant(partitions.toDispatch());
                
                while (partitionsList.hasMoreElements()) {
                    
                    Dispatch partition = partitionsList.nextElement().toDispatch();
                    
                    // Associa a partição ao disco físico.
                    StringBuilder query3 = new StringBuilder();
                    query3.append("ASSOCIATORS OF {Win32_DiskPartition.DeviceID='");
                    query3.append(Dispatch.call(partition, "DeviceID").toString());
                    query3.append("'} WHERE AssocClass=Win32_DiskDriveToDiskPartition");

                    Variant disks = activex.invoke("ExecQuery", new Variant(query3.toString()));

                    EnumVariant disksList = new EnumVariant(disks.toDispatch());

                    while (disksList.hasMoreElements()) {

                        Dispatch disk = disksList.nextElement().toDispatch();
                        
                        String interfaceType = Dispatch.call(disk, "InterfaceType").toString().toUpperCase();
                        
                        // Se o disco estiver conectado via USB, define o tipo
                        // de drive como RemovableDisk.
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
    
    
    /**
     * Obter a lista de drives pelo tipo (Removível, Rede, etc.).
     * 
     * @param driveType tipo de drive.
     * 
     * @return lista de drives pelo tipo.
     */
    public List<Drive> getDrives(DriveTypeEnum driveType) {
        
        List<Drive> drives = getDrives();
        List<Drive> selectedDrives = new ArrayList<>();
        
        for (Drive drive : drives) {
        
            switch (driveType) {
                
                case Unknown: {
                    if (drive.getDriveType() == DriveTypeEnum.Unknown) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case NoRootDirectory: {
                    if (drive.getDriveType() == DriveTypeEnum.NoRootDirectory) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case RemovableDisk: {
                    if (drive.getDriveType() == DriveTypeEnum.RemovableDisk) {
                        selectedDrives.add(drive);
                    }
                } break;
            
                case LocalDisk: {
                    if (drive.getDriveType() == DriveTypeEnum.LocalDisk) {
                        selectedDrives.add(drive);
                    }
                } break;
               
                case NetworkDrive: {
                    if (drive.getDriveType() == DriveTypeEnum.NetworkDrive) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case CompactDisc: {
                    if (drive.getDriveType() == DriveTypeEnum.CompactDisc) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case RAMDisk: {
                    if (drive.getDriveType() == DriveTypeEnum.RAMDisk) {
                        selectedDrives.add(drive);
                    }
                } break;
                
                case All: {
                    if (drive.getDriveType() == DriveTypeEnum.RemovableDisk ||
                    drive.getDriveType() == DriveTypeEnum.NetworkDrive) {
                        selectedDrives.add(drive);
                    }
                } break;
            
            }
        
        }
        
        return selectedDrives;
    
    }

    
}