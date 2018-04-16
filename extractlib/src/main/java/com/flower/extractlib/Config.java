package com.flower.extractlib;

import java.util.Map;

/**
 * Created by nicolee on 2016/9/30.
 */

public class Config {
    public static boolean isReplace = true;  //是否执行替换操作
    public static boolean isDifferent = false;  //是否不同
    public static boolean isChange = false;  //是否发生改变
    public static boolean isOutTranslate = false;  //是否输出翻译
    public static String outFile = "D:\\use\\work\\testStringExtract\\String.properties";
    public static String inFolder = "D:\\use\\work\\testStringExtract\\test";
    public static String stringFile = "D:\\use\\work\\testStringExtract\\strings.xml";
    public static String charset = "utf-8";
    public static Map<String,String> stringMap;
    public static  String curfileName = "";
    public static boolean curfileHasImportSettings = false;
    public static String needImportSettingsName = "";
    public static boolean curfileHasImportResource = false;
    public static String needImportResourceName = "";
    public static String importSettingString = "import com.flower.config.Settings;";
    public static String importResourceString = "import com.flower.client.R;";
}
