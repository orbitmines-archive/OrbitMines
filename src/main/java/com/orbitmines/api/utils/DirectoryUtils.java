package com.orbitmines.api.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import java.io.File;

public class DirectoryUtils {

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            assert files != null;

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
}
