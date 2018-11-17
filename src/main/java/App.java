import org.apache.commons.cli.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    private boolean verbose;
    private boolean createNewFolder;

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        final String OPTION_CONFIG = "config";
        final String OPTION_VERBOSE = "verbose";
        options.addOption(OPTION_CONFIG, "Config file location");
        options.addOption(OPTION_VERBOSE, false, "verbose logging");
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);

            App app = new App();
            if (!cmd.hasOption(OPTION_CONFIG)) {
                formatter.printHelp("ant", options);
                return;
            }
            boolean verbose = new Boolean(cmd.getOptionValue(OPTION_VERBOSE));
            verbose = true;
            String configFilePath = cmd.getOptionValue(OPTION_CONFIG);
            configFilePath = "/home/hungap/personal/photos/oneplus/foldersync.config";
            Properties prop = new Properties();
            java.io.File configFile = new java.io.File(configFilePath);
            if (!configFile.exists()) {
                throw new FileNotFoundException(configFilePath);
            }
            InputStream input = new FileInputStream(configFilePath);
            prop.load(input);
            app.setCreateNewFolder(Boolean.valueOf(Optional.ofNullable(prop.get("createNewFolder")).orElse("false").toString()));
            app.setVerbose(verbose);
            app.run(prop.get("sourceDirectory").toString(), prop.get("destinationDirectory").toString());
        } catch (ParseException e) {
            formatter.printHelp("ant", options);
        }
    }

    private void run(String sourceDir, String destDir) throws Exception {

        Folder sourceFolder = new FolderImpl(sourceDir);
        Folder destinationFolder = new FolderImpl(destDir);
        if (verbose) {
            System.out.println("Scanning files at " + sourceDir + "...");
        }
        Collection<File> sourceFiles = sourceFolder.getFiles();
        if (verbose) {
            System.out.println("Scanning files at " + destDir + "...");
        }
        Collection<File> destFiles = destinationFolder.getFiles();

        int initialSize = sourceFiles.size();
        if (verbose) {
            System.out.println("Number of files in source directory (" + sourceDir + "): " + initialSize);
            System.out.println("Number of files in destination directory(" + destDir + "): " + destFiles.size());
        }

        Iterator<File> itr = sourceFiles.iterator();

        while (itr.hasNext()) {
            File sourceFile = itr.next();
            for (File destFile : destFiles) {
                if (sourceFile.isSameName(destFile) && sourceFile.isSameLength(destFile)) {
                    itr.remove();
                    break;
                }
            }
        }

        if (verbose) {
            System.out.println("Number of files already synced: " + (initialSize - sourceFiles.size()));
        }

        if (sourceFiles.isEmpty()) {
            completed();
        }

        Folder newDestinationFolder = destinationFolder;

        if (createNewFolder) {
            String folderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            newDestinationFolder = destinationFolder.getChildFolder(folderName);
        }

        if (verbose) {
            System.out.println("Starts copying " + sourceFiles.size() + " to " + newDestinationFolder.getPath() + "...");
        }

        int count = 0;
        for (File sourceFile : sourceFiles) {
            newDestinationFolder.copyFile(sourceFile);
            count += 1;

            if (count % 100 == 0) {
                System.out.println("Copied " + count + " / " + sourceFiles.size() + " files.");
            }
        }
        System.out.println("Copied " + sourceFiles.size() + " / " + sourceFiles.size() + " files.");
        completed();
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setCreateNewFolder(boolean createNewFolder) {
        this.createNewFolder = createNewFolder;
    }

    private void completed() {
        System.out.println("Completed");
        System.exit(-1);
    }

}
