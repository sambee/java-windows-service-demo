package com.hhf.schedule;

import java.io.*;

/**
 * Created by Administrator on 2016/7/1.
 */
public class Log {
//    private static File file = new File("msg.log");


    public static void log(String tag, String conent){
//        BufferedWriter out = null;
//        try {
//
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
//            out.write(conent);
//            out.write("\n");
//        } catch (IOException ex) {
//
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
        System.out.println(tag + "-" + conent);
    }
}
