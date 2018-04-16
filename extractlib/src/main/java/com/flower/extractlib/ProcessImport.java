package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/26.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessImport {

    private File copyFile;

    public ProcessImport() {
    }

    public void readTxt(File tempFile) throws IOException {
        if (!Config.needImportSettingsName.contains(tempFile.getName())
                && !Config.needImportResourceName.contains(tempFile.getName()) ) {
            return;
        }
        Config.curfileHasImportSettings = false;
        Config.curfileHasImportResource = false;
        System.out.println("#" + tempFile.getName() + "\n");
        BufferedWriter writer = getReplaceFileWriter(tempFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(tempFile), Config.charset));
        String tempString = "";
        while ((tempString = reader.readLine()) != null) {
            if (tempString.startsWith("import ")) {
                if(!Config.curfileHasImportSettings && Config.needImportSettingsName.contains(tempFile.getName())){
                    writer.write(Config.importSettingString + "\n");
                    Config.curfileHasImportSettings = true;
                }
                if(!Config.curfileHasImportResource && Config.needImportResourceName.contains(tempFile.getName())){
                    writer.write( Config.importResourceString + "\n");
                    Config.curfileHasImportResource = true;
                }
            }
            //复制到文件
            writer.write(tempString + "\n");
        }
        reader.close();
        closeWriter(writer, tempFile);

    }

    public void closeWriter(BufferedWriter writer, File oldFile) {
        if (writer == null) {
            return;
        }
        try {
            writer.flush();
            writer.close();
            String path = oldFile.getAbsolutePath();
            oldFile.delete();
            copyFile.renameTo(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BufferedWriter getReplaceFileWriter(File oldFile) {
        copyFile = new File(oldFile.getAbsolutePath() + "_import");
        try {
            if (!copyFile.exists()) {
                copyFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(copyFile));
            return writer;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("path:" + copyFile.getAbsolutePath());
        }
        return null;
    }

    public void readDir(String folder) throws IOException {
        File dir = new File(folder);
        // PrintWriter output = new PrintWriter(new FileWriter(new
        // File(outFile)));
        if (dir.isDirectory()) {
            System.out.println("#Dir#" + dir.getName() + "\n");
//            output.write("#Dir#" + dir.getName() + "\n");
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File tempFile = new File(dir, children[i]);
                if (tempFile.isDirectory()) {
                    readDir(tempFile.getAbsolutePath());
                    System.out.println("tempFile.getAbsolutePath()：" + tempFile.getAbsolutePath() + "\n");
                } else {
                    readTxt(tempFile);
                }
            }
        }
    }
}