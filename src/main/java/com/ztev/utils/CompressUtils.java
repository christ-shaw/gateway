package com.ztev.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ${xiaozb} on 2017/9/6.
 *
 * @copyright by ztev
 */
public class CompressUtils {
    public static void createZip(Path directoryPath, Path zipPath) throws IOException {
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        ZipArchiveOutputStream tOut = null;

        try {
            fOut = new FileOutputStream(zipPath.toFile());
            bOut = new BufferedOutputStream(fOut);
            tOut = new ZipArchiveOutputStream(bOut);
            addFileToZip(tOut, directoryPath, "");
        } finally {
            tOut.finish();
            IOUtils.closeQuietly(tOut);
            IOUtils.closeQuietly(bOut);
            IOUtils.closeQuietly(fOut);
        }


    }


    public static void addFileToZip(ZipArchiveOutputStream outputStream, Path directoryPath, String base) throws IOException {
        File f = directoryPath.toFile();
        // create zip entry
        String entryName = base + f.getName();
        outputStream.putArchiveEntry(new ZipArchiveEntry(f, entryName));

        if (f.isFile()) {
            FileReader fileReader = null;

            try {
                fileReader = new FileReader(f);
                IOUtils.copy(fileReader, outputStream);
            } finally {
                outputStream.closeArchiveEntry();
                IOUtils.closeQuietly(fileReader);

            }
        } else {
            outputStream.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileToZip(outputStream, Paths.get(child.getAbsolutePath()), entryName + "/");
                }
            }
        }
    }
}