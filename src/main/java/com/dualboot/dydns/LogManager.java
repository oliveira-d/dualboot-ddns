package com.dualboot.dydns;

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

//list 
import java.util.List;

public class LogManager {
        //public static String logFolderName;
        private Logger logger;
        private String logFileFull;
        // 
        LogManager(String logName,boolean appendMode) {
            configureLogManager(logName,appendMode);
        }
        // Check Log file
        private void configureLogManager(String logName,boolean appendMode) {
            String[] logParentFolderNames = {"/var/log","/var/lib"};
            String logFileName = logName + ".log";

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

            String logFolderName = logParentFolderName + "/" + Main.projectName;
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

            Logger logger = Logger.getLogger(logName);
            boolean log;
            try {
                // Create a file handler that writes log messages to a file
                this.logFileFull = logFolderName+"/"+logFileName;
                FileHandler fileHandler = new FileHandler(logFolderName+"/"+logFileName,appendMode);
                fileHandler.setFormatter(new SimpleFormatter());

                // Add the file handler to the logger
                logger.addHandler(fileHandler);
                log = true;
            } catch (IOException e) {
                e.printStackTrace();
                log=false;
            }
            this.logger = logger;
        }

        public void info(String message) {
            logger.info(message);
        }

        public void warning(String message) {
            logger.warning(message);
        }

        public String getLastLine() {
            Path logFilePath = Path.of(this.logFileFull);
            String readLine = "";
            try {
                // Read all lines from the log file
                List<String> logLines = Files.readAllLines(logFilePath);
                // Check if the file has at least two lines
                if (logLines.size() > 0) {
                    // Access the second line (index 1)
                    readLine = logLines.get(logLines.size()-1);
                    // You can parse and analyze the second line as needed
                } else {
                    System.out.println("The log file is empty. No data to read from.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readLine;
        }
}

