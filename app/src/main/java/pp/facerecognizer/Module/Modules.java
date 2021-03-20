package pp.facerecognizer.Module;

public class Modules {
   private String ModuleName, ModuleID, DeviceToekn, Level, Leader;

    public Modules() {
    }

    public Modules(String moduleName, String moduleID, String deviceToekn, String level, String leader) {
        ModuleName = moduleName;
        ModuleID = moduleID;
        DeviceToekn = deviceToekn;
        Level = level;
        Leader = leader;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getDeviceToekn() {
        return DeviceToekn;
    }

    public void setDeviceToekn(String deviceToekn) {
        DeviceToekn = deviceToekn;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getLeader() {
        return Leader;
    }

    public void setLeader(String leader) {
        Leader = leader;
    }
}
