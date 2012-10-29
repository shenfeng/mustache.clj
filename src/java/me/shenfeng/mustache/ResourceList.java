package me.shenfeng.mustache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * list resources available from the classpath
 */
public class ResourceList {
    public static List<String> getResources(Pattern pattern)
            throws IOException {
        List<String> retval = new ArrayList<String>();
        String[] paths = System.getProperty("java.class.path", ".")
                .split(":");
        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, pattern));
            } else if (file.exists()) {
                retval.addAll(getResourcesFromJarFile(file, pattern));
            }
        }
        return retval;
    }

    static List<String> getResourcesFromJarFile(File file, Pattern pattern)
            throws ZipException, IOException {
        ArrayList<String> retval = new ArrayList<String>();
        ZipFile zf = new ZipFile(file);
        Enumeration<?> e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            String fileName = ze.getName();
            boolean accept = pattern.matcher(fileName).matches();
            if (accept) {
                retval.add(fileName);
            }
        }
        zf.close();
        return retval;
    }

    static List<String> getResourcesFromDirectory(File directory,
            Pattern pattern) throws IOException {
        ArrayList<String> retval = new ArrayList<String>();
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                String fileName = file.getCanonicalPath();
                boolean accept = pattern.matcher(fileName).matches();
                if (accept) {
                    retval.add(fileName);
                }
            }
        }
        return retval;
    }
}