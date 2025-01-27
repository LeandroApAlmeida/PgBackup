package postgre.backup.service;

/**
 * Tipo de drive. Os tipos são:
 * 
 * <br><br>
 * 
 * <ul>
 * 
 * <li><b>Unknown:</b> disco desconhecido.</li>
 * 
 * <li><b>NoRootDirectory:</b> qualquer drive que não tem o Windows instalado.</li>
 * 
 * <li><b>RemovableDisk</b> disco removível.</li>
 * 
 * <li><b>LocalDisk</b> disco local.</li>
 * 
 * <li><b>NetworkDrive</b> drive de rede.</li>
 * 
 * <li><b>CompactDisc</b> disco CD.</li>
 * 
 * <li><b>RAMDisk</b> disco em memória RAM.</li>
 *
 * </ul>
 * 
 * @author Leandro Aparecido de Almeida
 */
public enum DriveTypeEnum implements HasNativeValue {
    
    
    /**Drive desconhecido.*/
    Unknown(0),
    
    /**Qualquer drive que não tenha o Windows instalado.*/
    NoRootDirectory(1),
    
    /**Disco removível.*/
    RemovableDisk(2),
    
    /**Disco local.*/
    LocalDisk(3),
    
    /**Drive de rede.*/
    NetworkDrive(4),
    
    /**Disco CD.*/
    CompactDisc(5),
    
    /**Disco em memória RAM.*/
    RAMDisk(6);

    
    /**Tipo do disco.*/
    public final int nativeValue;

    
    DriveTypeEnum(int nativeValue) {
        this.nativeValue = nativeValue;
    }

    
    @Override
    public int getNativeValue() {
        return nativeValue;
    }
    
    
}
