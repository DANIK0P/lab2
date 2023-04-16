package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Ls {


    @Option(name = "-l", usage = "Enable long format listing")
    private boolean isLongFormat;

    @Option(name = "-h", usage = "Enable human-readable format listing")
    private boolean isHumanReadable;

    @Option(name = "-r", usage = "Reverse the order of the output listing")
    private boolean isReverse;

    @Option(name = "-o", usage = "file name to output")
    private String outputFile;

    @Argument(usage = "Directory or file")
    private File directoryOrFile;


    public void execute(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Usage: [-l] [-h] [-r] [-o output.file] directory_or_file");
            parser.printUsage(System.err);
            return;
        }


        List<String> fileNames;
        if (directoryOrFile.isDirectory()) {
            fileNames = Arrays.asList(directoryOrFile.list());
            if (isReverse) Collections.reverse(fileNames);
        } else {
            fileNames = Collections.singletonList(directoryOrFile.getName());
        }

        StringBuilder outputBuilder = new StringBuilder();
        for (String fileName : fileNames) {
            if (isLongFormat)
                outputBuilder.append(longFormat(fileName));
            else if (isHumanReadable)
                outputBuilder.append(humanReadable(fileName));
            else
                outputBuilder.append(String.format("%s\n", fileName));
        }


        if (outputFile != null) {
            FileWriter writer = new FileWriter(outputFile);
            writer.write(outputBuilder.toString());
            writer.close();
        } else {
            System.out.print(outputBuilder);
        }
    }


    public String longFormat(String fileName) {
        if (!directoryOrFile.exists()) return fileName + " The file doesn't exist";
        File file;
        if (directoryOrFile.isDirectory()) {
            file = new File(directoryOrFile, fileName);
        } else {
            file = directoryOrFile;
        }
        String permissions = String.format("%3s", Integer.toBinaryString(file.canExecute() ? 1 : 0)
                + Integer.toBinaryString(file.canRead() ? 1 : 0)
                + Integer.toBinaryString(file.canWrite() ? 1 : 0)).replace(' ', '0');
        String lastModified = getLastModifiedString(file);
        String size = getSizeString(file);
        return isReverse ? String.format("%s %s %s %s\n", size, lastModified, permissions, fileName)
                : String.format("%s %s %s %s\n", fileName, permissions, lastModified, size);
    }


    public String humanReadable(String fileName) {
        if (!directoryOrFile.exists()) return fileName + " The file doesn't exist";
        File file;
        if (directoryOrFile.isDirectory()) {
            file = new File(directoryOrFile, fileName);
        } else {
            file = directoryOrFile;
        }
        String size = getHumanReadableSizeString(file);
        String permissions = getPermissionsString(file);
        return isReverse ? String.format("%s %s %s\n", size, permissions, fileName)
                : String.format("%s %s %s\n", fileName, permissions, size);
    }


    public String getPermissionsString(File file) {
        return (file.canRead() ? "r" : "-") +
                (file.canWrite() ? "w" : "-") +
                (file.canExecute() ? "x" : "-");
    }


    public String getLastModifiedString(File file) {
        if (!file.exists()) return "The file doesn't exist";
        long lastModified = file.lastModified();
        return String.format("%tF %<tT", lastModified);
    }


    public String getSizeString(File file) {
        return String.format("%d", file.length());
    }


    public String getHumanReadableSizeString(File file) {
        long size = file.length();
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int index = 0;
        while (size >= 1024 && index < units.length - 1) {
            size /= 1024;
            index++;
        }
        return String.format("%d %s", size, units[index]);
    }
}