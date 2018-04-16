package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/26.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class QuotState extends State {
    public static final QuotState quotState = new QuotState();
    public static int keynum = 0;

    @Override
    public boolean handle(BufferedReader reader, String line, int from,
                          PrintWriter output, BufferedWriter writer) {
        int beginIndex = from, endIndex = from, k = from;
        boolean out = false;
        replacedLine = line;
        for (k = from; k < line.length(); k++) {
            char now = line.charAt(k);
            if ((now == '"' && k == 0)
                    || (now == '"' && k > 0 && line.charAt(k - 1) != '\\')) {
//                Process.queue.add(new DataModel(NormalState.normal, reader,
//                        line, k + 1, output));
                DataModel dm = new DataModel(NormalState.normal, reader,
                        line, k + 1, output);
                dm.getState().handle(dm.getReader(), dm.getLine(),
                        dm.getFrom(), dm.getOutput(), writer);
                break;
            } else if (line.substring(k, k + 1).getBytes().length > 1) {
                for (; k < line.length(); k++) {
                    if (line.charAt(k) == '"' && line.charAt(k - 1) != '\\') {
                        endIndex = k;
                        break;
                    }
                }
                System.out.println("key: " + keynum++ + "  "
                        + line.substring(beginIndex, endIndex));

                try {
                    String value = line.substring(beginIndex, endIndex);
                    if (!Config.stringMap.containsKey(value)) {

                        String name = "zbj_string_" + Config.stringMap.size();
                        if (!Config.isReplace) {
                            Config.stringMap.put(value, name);
                        }
                        String preFix = "<string name=\"" + name + "\">";
                        String endFix = "</string>";
                        if (Config.isOutTranslate) {
                            TranslateApi.get(value, output);
                        } else {
                            output.write(preFix + value + endFix + "\n");
                            output.flush();
                        }
                    }
                    if (Config.isReplace ) {
                        if(Config.curfileName.endsWith(".xml")){
                            replaceXml(value, line, beginIndex, endIndex);
                        }else{
                            replaceJavaCode(value, line, beginIndex, endIndex);
                        }
                        Config.isDifferent = true;
                        Config.isChange = true;
                    }
                    out = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (out) {
//            Process.queue.add(new DataModel(NormalState.normal, reader, line,
//                    k + 1, output));
            DataModel dm = new DataModel(NormalState.normal, reader,
                    replacedLine, k + 1, output);
           boolean hasExists = dm.getState().handle(dm.getReader(), dm.getLine(),
                    dm.getFrom(), dm.getOutput(), writer);
            if (!hasExists) {
                try {
                    String res = isEmpty(dm.getState().replacedLine) ? replacedLine : dm.getState().replacedLine;
                    if(isEmpty(res)){
                        res = line;
                    }
                    if(Config.isReplace){
                        writer.write(res);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return out;
    }

    private void replaceXml(String value, String line, int beginIndex, int endIndex){
        String replaceStr = "@string/";
        if(Config.stringMap.get(value) != null && Config.stringMap.get(value).length() > 0){
            replacedLine = line.substring(0, beginIndex) +
                    replaceStr + Config.stringMap.get(value) +
                    line.substring(endIndex, line.length()) + "\n";
        }
    }

    private void replaceJavaCode(String value, String line, int beginIndex, int endIndex){
        String replaceStr = "Settings.resources";
        if(!Config.curfileHasImportSettings && !Config.needImportSettingsName.contains(Config.curfileName)){
            Config.needImportSettingsName = Config.needImportSettingsName + "," + Config.curfileName;
        }
        if(!Config.curfileHasImportResource && !Config.needImportResourceName.contains(Config.curfileName)){
            Config.needImportResourceName = Config.needImportResourceName + "," + Config.curfileName;
        }
        if(Config.stringMap.get(value) != null && Config.stringMap.get(value).length() > 0){
            replacedLine = line.substring(0, beginIndex - 1) +
                    replaceStr + ".getString(R.string." + Config.stringMap.get(value) + ")" +
                    line.substring(endIndex + 1, line.length()) + "\n";
        }
    }

    private boolean isEmpty(String s){
        if(s == null || s.length() == 0){
            return true;
        }
        return false;
    }

}