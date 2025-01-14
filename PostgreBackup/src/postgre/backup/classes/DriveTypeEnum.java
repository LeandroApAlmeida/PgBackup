package postgre.backup.classes;

public enum DriveTypeEnum implements HasNativeValue {
    
    Unknown(0),
    NoRootDirectory(1),
    RemovableDisk(2),
    LocalDisk(3),
    NetworkDrive(4),
    CompactDisc(5),
    RAMDisk(6);

    public final int nativeValue;

    DriveTypeEnum(int nativeValue) {
        this.nativeValue = nativeValue;
    }

    @Override
    public int getNativeValue() {
        return nativeValue;
    }
}
