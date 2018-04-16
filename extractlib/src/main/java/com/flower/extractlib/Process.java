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
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Process {

    public static Queue<DataModel> queue = new ConcurrentLinkedQueue<DataModel>();
    private PrintWriter output = null;
    private File copyFile;

    public Process(PrintWriter output) {
        this.output = output;
    }

    public void readTxt(File tempFile) throws IOException {

        System.out.println("#" + tempFile.getName() + "\n");
//        output.write("#" + tempFile.getName() + "\n");

        BufferedWriter writer = getReplaceFileWriter(tempFile);
        Config.isChange = false;
        Config.curfileName = tempFile.getName();
        Config.curfileHasImportSettings = false;
        Config.curfileHasImportResource = false;

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(tempFile), Config.charset));
        String tempString = "";
        while ((tempString = reader.readLine()) != null) {
            queue.add(new DataModel(NormalState.normal, reader, tempString, 0,
                    output));
            while (queue.size() > 0) {
                if(Config.isReplace){
                    Config.isDifferent = false;
                }
                DataModel dm = queue.remove();
                dm.getState().handle(dm.getReader(), dm.getLine(),
                        dm.getFrom(), dm.getOutput(), writer);
                //复制到文件
                if(Config.isReplace && !Config.isDifferent){
                    writer.write(dm.getLine() + "\n");
                }
            }
        }
        reader.close();
        closeWriter(writer,tempFile);

    }

    public void closeWriter(BufferedWriter writer, File oldFile){
        if(!Config.isReplace || writer == null){
            return;
        }
        try {
            writer.flush();
            writer.close();
            if (Config.isChange) {
                String path = oldFile.getAbsolutePath();
                oldFile.delete();
                copyFile.renameTo(new File(path));
            } else {
                copyFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public BufferedWriter getReplaceFileWriter(File oldFile) {
        if(Config.isReplace) {
            copyFile = new File(oldFile.getAbsolutePath()+"_replace");
            try {
                if (!copyFile.exists()) {
                    copyFile.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(copyFile));
                return writer;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("path:"+copyFile.getAbsolutePath());
            }
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
                if (tempFile.isDirectory()){
                    readDir(tempFile.getAbsolutePath());
                    System.out.println("tempFile.getAbsolutePath()：" + tempFile.getAbsolutePath() + "\n");
                } else {
                    readTxt(tempFile);
                }
            }
        }
    }
}