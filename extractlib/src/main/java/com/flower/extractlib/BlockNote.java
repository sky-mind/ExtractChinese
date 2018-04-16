package com.flower.extractlib;//package com.example;
//
///**
// * Created by nicolee on 2016/9/26.
// */
//
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//class BlockNote extends State {
//    public static final BlockNote blockNote = new BlockNote();
//
//    @Override
//    public boolean handle(BufferedReader reader, String line, int from,
//                       PrintWriter output ,BufferedWriter writer) {
//        // TODO Auto-generated method stub
//        boolean out = false;
//        int k = from;
//        for (k = from; k < line.length(); k++) {
//            char now = line.charAt(k);
//            if ('*' == now && k < line.length() - 1
//                    && ('/' == line.charAt(k + 1))) {
//                out = true;
//                break;
//            }
//        }
//
//        try {
//            label:
//            while (!out && (line = reader.readLine()) != null) {
//                for (k = 0; k < line.length(); k++) {
//                    char now2 = line.charAt(k);
//                    if ('*' == now2 && k < line.length() - 1
//                            && ('/' == line.charAt(k + 1))) {
//                        out = true;
//                        break label;
//                    }
//                }
//            }
////            if (out) {
////                Process.queue.add(new DataModel(NormalState.normal, reader,
////                        line, k + 2, output));
////            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//}
