package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/26.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class NormalState extends State {
    public static final NormalState normal = new NormalState();

    @Override
    public boolean handle(BufferedReader reader, String line, int from,
                       PrintWriter output, BufferedWriter writer) {
        if(Config.curfileName.endsWith(".xml")){
            return exitsInXml(reader, line, from, output, writer);
        }else {
            return exitsInJavaCode(reader, line, from, output, writer);
        }
    }

    private boolean exitsInJavaCode(BufferedReader reader, String line, int from,
                                 PrintWriter output, BufferedWriter writer){
        if(line.contains("Log.d(") || line.contains("Log.e(") || line.contains("Log.i(") || line.contains("Log.v(")){
            return false;
        }
        if(line.contains(Config.importSettingString)){
            Config.curfileHasImportSettings = true;
        }
        if(line.contains(Config.importResourceString)){
            Config.curfileHasImportResource = true;
        }

        boolean out = false;
        for (int k = from; k < line.length(); k++) {
            char now = line.charAt(k);
            if ((now == '"' && k == 0)
                    || (now == '"' && k > 0 && line.charAt(k - 1) != '\\')) {
                DataModel dm = new DataModel(QuotState.quotState, reader,
                        line, k + 1, output);
                out = dm.getState().handle(dm.getReader(), dm.getLine(),
                        dm.getFrom(), dm.getOutput(), writer);
                break;
            }
        }
        return out;
    }

    private boolean exitsInXml(BufferedReader reader, String line, int from,
                                    PrintWriter output, BufferedWriter writer){
        if(!line.replace(" ","").contains("android:text=")){
            return false;
        }

        boolean out = false;
        for (int k = from; k < line.length(); k++) {
            char now = line.charAt(k);
            if ((now == '"' && k == 0)
                    || (now == '"' && k > 0)) {
                DataModel dm = new DataModel(QuotState.quotState, reader,
                        line, k + 1, output);
                out = dm.getState().handle(dm.getReader(), dm.getLine(),
                        dm.getFrom(), dm.getOutput(), writer);
                break;
            }
        }
        return out;
    }
}