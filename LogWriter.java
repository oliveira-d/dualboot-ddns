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

public class LogWriter {
        // Check Log file
        public static Logger configureLogWriter(String projectName) {
            String[] logParentFolderNames = {"/var/log","/var/lib"};
            String logFileName = projectName + ".log";

            Path[] logParentFoldersPath = new Path[logParentFolderNames.length];
            for (int i=0; i<logParentFolderNames.length; i++) {
                logParentFoldersPath[i] = Paths.get(logParentFolderNames[i]);
            }

            String logParentFolderName = ".";
            for (int i=0; i<logParentFolderNames.length; i++){
                if (Files.exists(logParentFoldersPath[i])){
                    logParentFolderName = logParentFolderNames[i];
                    break;
                }
            }

            String logFolderName = logParentFolderName + "/" + projectName;
            Path logFolderPath = Paths.get(logFolderName);
            if (!Files.exists(logFolderPath)) {
                if (Main.hasWritePrivileges(logParentFolderName)){
                    try{
                        Files.createDirectories(logFolderPath);
                        //logFileName = logFolderName+"/"+logFileName;
                    } catch (IOException e) {
                        System.err.println("Failed to create log folder at "+logFolderName+". Proceding with logging deactivated.");
                    }
                } else {
                    System.err.println("No write privileges at "+logParentFolderName);
                    System.err.println("Try running the program as root to write the log directory structure.");
                }
            }

            Logger logger = Logger.getLogger("dydns");
            boolean log;
            try {
                // Create a file handler that writes log messages to a file
                FileHandler fileHandler = new FileHandler(logFolderName+"/"+logFileName,true);
                fileHandler.setFormatter(new SimpleFormatter());

                // Add the file handler to the logger
                logger.addHandler(fileHandler);
                log = true;
            } catch (IOException e) {
                e.printStackTrace();
                log=false;
            }
            return logger;
        }

}