package com.flower.extractlib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by nicolee on 2016/10/7.
 */

public class Test {
    public static final int TYPE_MILLSECOND = 1;
    public static final int TYPE_SECOND = 1000;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm", Locale.CHINA);
    private static String[] res = new String[]{"A1_B1","A1_B2","A1_B3","A1_B4","A1_C1","A1_C2","A1_C3","A1_C4","A1_B1_C3","A1_B2_C4"};
    private static HashSet<String> nextTags ;

    private static  ArrayList<Integer> industryList; //行业标签
    private static  ArrayList<Integer> themeList; //主题标签
    static int postion;
    private static  final int STEP = 4;
    private static  final int DIVID_NUM = 10;
    static int industryStart = 0;
    static int themeStart = 0;
    static int industryEnd = 0;
    static int themeEnd = 0;


    public static void computeTag(){
//        int loc = ((postion + 1) / DIVID_NUM -1);
//        int industryStart = loc * STEP;
//        int themeStart = loc * STEP;
        Map<Integer,List<String>> tagMap = new HashMap<>();
        int count = (industryList.size() + themeList.size())/(STEP*2);
        if((industryList.size() + themeList.size())%(STEP*2) > 0){
            count ++;
        }

        for(int loc = 0; loc < count ; loc++ ){
            industryEnd = STEP + industryStart;
            themeEnd = STEP + themeStart;
            if(industryEnd > industryList.size()){
                industryEnd = industryList.size();
                themeEnd = themeEnd + STEP - (industryEnd - industryStart);
                if(themeEnd > themeList.size()){
                    themeEnd = themeList.size();
                }
            }else{
                if(themeEnd > themeList.size()){
                    themeEnd = themeList.size();
                    industryEnd = industryEnd + STEP - (themeEnd - themeStart);
                    if(industryEnd > industryList.size()){
                        industryEnd = industryList.size() ;
                    }
                }
            }
            List<String> locList = new ArrayList<>();
            for(int i=industryStart; i<industryEnd ; i++){
                locList.add("industry"+industryList.get(i));
            }
            for(int j=themeStart; j < themeEnd; j++){
                locList.add("theme"+themeList.get(j));
            }
            tagMap.put(loc, locList);
            industryStart = industryEnd;
            themeStart = themeEnd;
        }

        for (Integer key : tagMap.keySet()) {
            List<String> locList = tagMap.get(key);
            System.out.println("loc= "+ key + " , value= " + getString(locList));
        }
    }

    private static String getString (List<String> locList ){
        String s = "";
        for(String i: locList){
            s = s+ "," + i;
        }
        return s;
    }



    public static void main(String[] args){
//        double[] loc = new double[]{0, 0};
//        int code = 1;
//        if(loc[0] == 0 ||loc[1] == 0){
//            code = 0;
//        }
//        System.out.println(code);
//        String mFileName = "";
//        String[] typeSuffixArray = mFileName.split("\\.");
//        System.out.println(typeSuffixArray.length);
//
//        List<Integer> list = new ArrayList<>();
//        list.add(5);
//        list.add(1);
//        System.out.println(list.contains(5));
//        System.out.println(list.contains(3));
        industryList = new ArrayList<>();
        themeList = new ArrayList<>();
        int industrySize = 0;
        int themeSize = 0;
        for(int i=0;i<industrySize ; i++){
            industryList.add(i);
        }
        for(int i=0;i<themeSize ; i++){
            themeList.add(i);
        }
        System.out.println("industrySize= "+ industrySize + " , themeSize= " + themeSize);
        computeTag();
    }


    /**
     * 秒转换成时间
     * hour : minute : second
     */
    public static String sec2Time(long time) {
        String timeStr;
        long hour ;
        long minute ;
        long second ;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                    minute = minute % 60;
                    second = time - hour * 3600 - minute * 60;
                    timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }



}
