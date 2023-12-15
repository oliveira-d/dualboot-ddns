import java.io.IOException;

//user for checking wether configuration files and paths exist
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// used to check write privileges
import java.nio.file.FileSystems;
//import java.nio.file.Files;
import java.nio.file.LinkOption;
//import java.nio.file.Path;

// Log
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//import java.io.IOException;

public class Main {

    public static final String projectName = "dydns";
    public static LogManager logger = new LogManager(projectName,true);
    public static LogManager ipMonitor = new LogManager("ipMonitor",true);
    
    public static void main(String[] args) {
        // Check configuration file
        String[] configFolderNames = {"/usr/local/etc","/etc","."};
        String configFileName = projectName + ".conf";
        //String[] configurationParameters = {"username","password","hostname","domain"};

        Path[] configFolderPaths = new Path[configFolderNames.length];
        for (int i=0; i<configFolderNames.length; i++) {
            configFolderPaths[i] = Paths.get(configFolderNames[i]);
        }

        String configFolderName = "";
        for (int i=0; i<configFolderNames.length; i++) {
            if (Files.exists(configFolderPaths[i]) && Files.isDirectory(configFolderPaths[i])) {
                configFolderName = configFolderNames[i];
                break;
            }
        }

        String configFile = configFolderName + "/" + configFileName;
        Path configFilePath = Paths.get(configFile);
        
        ConfigHandler configHandler = new ConfigHandler(configFile);
        if (!Files.exists(configFilePath)) {
            System.err.println("Configuration file not found in "+configFile);
            if (!hasWritePrivileges(configFolderName)){
                System.err.println("No write privileges to "+configFolderName);
                System.err.println("Try running the program as root to write the configuration file.");
                System.exit(0);
            } else {
                System.out.println("Configuring "+projectName+"...");
                configHandler.createConfig();
            }
            System.out.println("Configuration file created at "+configFile);
            //System.out.println("Run the program again to update you DDNS records.");
        }
        configHandler.readConfig();
        String provider = configHandler.getProperty("provider");
        //String currentIP = DyDNS.getCurrentIP();
        
        if (provider.equals(ConfigHandler.DDNSProviders[0])) {
            String username = configHandler.getProperty("username");
            String password = configHandler.getProperty("password");
            String hostname = configHandler.getProperty("hostname");
            String domain = configHandler.getProperty("domain");
            //procedes to update DDNS
            //System.out.println(username+" "+password+" "+hostname+" "+domain);
            try {
                String currentIP = DyDNS.getCurrentIP();
                if (!(ipMonitor.getLastLine().contains(currentIP))) {
                    DyDNS.NoIpUpdate(username, password, hostname, domain);
                    //DyDNS.DuckDNSUpdate("token-here","hostname-here");
                    ipMonitor.info("Retrieved IP: "+currentIP);
                }
            } catch (IOException e) {
                logger.info("Failed to get current IP address.");
            }
        }

        if (provider.equals(ConfigHandler.DDNSProviders[1])) {
            String username = configHandler.getProperty("username");
            String password = configHandler.getProperty("password");
            String hostname = configHandler.getProperty("hostname");
            String domain = configHandler.getProperty("domain");
            //procedes to update DDNS
            //System.out.println(username+" "+password+" "+hostname+" "+domain);
            try {
                String currentIP = DyDNS.getCurrentIP();
                if (!(ipMonitor.getLastLine().contains(currentIP))) {
                    DyDNS.DynDNSUpdate(username, password, hostname, domain);
                    //DyDNS.DuckDNSUpdate("token-here","hostname-here");
                    ipMonitor.info("Retrieved IP: "+currentIP);
                }
            } catch (IOException e) {
                logger.info("Failed to get current IP address.");
            }
        }

        if (provider.equals(ConfigHandler.DDNSProviders[2])) {
            String token = configHandler.getProperty("token");
            String hostname = configHandler.getProperty("hostname");
            try {
                String currentIP = DyDNS.getCurrentIP();
                if (!(ipMonitor.getLastLine().contains(currentIP))) {
                    DyDNS.DuckDNSUpdate(hostname, token);
                    ipMonitor.info("Retrieved IP: "+currentIP);
                } else {
                    logger.info("No change in IP address.");
                }
            } catch (IOException e) {
                logger.info("Failed to get current IP address.");
            }
        }        
    }

    public static boolean hasWritePrivileges(String configFolder) {
    
        Path path = FileSystems.getDefault().getPath(configFolder);

        if (Files.isWritable(path)) {
            return true;
        } else {
            return false;
        }
    }
}
