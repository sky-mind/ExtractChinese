package com.flower.extractlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class MainClass {
    public static void main(String[] args) throws IOException {
        InputStream in = new FileInputStream(Config.stringFile);
        try {
            Config.stringMap = DomParser.parse(in);
        }catch (Exception e){
            e.printStackTrace();
        }

        PrintWriter output = new PrintWriter(new FileWriter(new File(Config.outFile)));
        Process process = new Process(output);
        process.readDir(Config.inFolder);
//        writeMapString(output);
        output.write("needImportSettingsName: "+ Config.needImportSettingsName + "\n");
        output.flush();
        output.close();

        if(Config.isReplace){
            ProcessImport processImport = new ProcessImport();
            processImport.readDir(Config.inFolder);
        }
    }

    private static void writeMapString(PrintWriter output){
        for(String key : Config.stringMap.keySet()){
            String preFix = "<string name=\""+ Config.stringMap.get(key) + "\">";
            String endFix = "</string>";
            output.write(preFix + key + endFix+ "\n");
        }

    }
}
