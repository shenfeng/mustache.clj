package me.shenfeng.mustache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * list resources available from the classpath
 */
public class ResourceList {
    public static List<String> getResources(String folder, List<String> extensions)
            throws IOException {
        List<String> files = new ArrayList<String>(32);
        String[] paths = System.getProperty("java.class.path", ".").split(":");
        for (String path : paths) {
            File file = new File(path);
            if (file.isDirectory()) {
                files.addAll(getFromDir(file, folder, extensions));
            } else if (file.exists()) {
                files.addAll(getFromJar(file, folder, extensions));
            }
        }
        return files;
    }

    private static boolean accpet(String path, String folder, List<String> extensions)
            throws IOException {
        boolean b = false;
        for (String e : extensions) {
            if (path.endsWith(e)) {
                b = true;
                break;
            }
        }
        return b && path.contains(folder);
    }

    public static List<String> getFromJar(File file, String folder, List<String> extensions)
            throws IOException {
        ArrayList<String> files = new ArrayList<String>();
        ZipFile zf = new ZipFile(file);
        Enumeration<?> e = zf.entries();
        while (e.hasMoreElements()) {
            String fileName = ((ZipEntry) e.nextElement()).getName();
            if (accpet(fileName, folder, extensions)) {
                files.add(fileName);
            }
        }
        zf.close();
        return files;
    }

    public static List<String> getFromDir(File dir, String folder, List<String> extensions)
            throws IOException {
        List<String> files = new ArrayList<String>(16);
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                files.addAll(getFromDir(f, folder, extensions));
            } else {
                String path = f.getAbsolutePath();
                if (accpet(path, folder, extensions))
                    files.add(path);
            }
        }
        return files;
    }
}