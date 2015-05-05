package com.wiki4zhang.library.common;

import android.util.Log;

import java.util.Locale;

/**
 * Created by wiki on 15-5-5.
 */
public class Logger {
    /**
     * the Tag for Log
     */
    private static String TAG = "Logger";

    /**
     * charge is logging while debug.
     */
    public static boolean DEBUG = Log.isLoggable (TAG,Log.INFO);

    /**
     * customize the log's tag for your Application
     * @param tag
     *<br/>
     *Enable your log level before you start your Application
     *<br/>
     * {@code Adb shell setprop  log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag){
        if(tag == null){
            return;
        }

        if(tag.length () == 0){
            return;
        }

        d ("change tag to %s",tag);

        TAG = tag;

        DEBUG = Log.isLoggable (TAG,Log.INFO);
    }

    public static void v(String format,Object ...args){
        Log.v (TAG,buildMessage (format,args));
    }

    public static void d(String format,Object ...args){
        if(DEBUG)
            Log.d (TAG,buildMessage (format,args));
    }

    public static void i(String format,Object ...args){
        Log.i (TAG, buildMessage (format, args));
    }

    public static void e(String format,Object ...args){
        Log.e (TAG, buildMessage (format, args));
    }


    /**
     * build your own message with this format
     * @param format
     * @param args
     * @return format String
     */
    private static String buildMessage(String format,Object ...args){
        String msg = (args == null)?format:String.format (Locale.CHINA,format,args);

        //get current invoking method and class stack
        //it looks like a stack
        StackTraceElement[] stackTraceElements = new Throwable().fillInStackTrace ().getStackTrace ();

        String caller = "<unknown>";

        for (int i = 2; i < stackTraceElements.length; i++) {
            Class clz = stackTraceElements[i].getClass ();
            if (!clz.equals (Logger.class)) {//if current level class not equal Logger.getClass
                String callingClass = stackTraceElements[i].getClassName ();
                callingClass = callingClass.substring (callingClass.lastIndexOf ('.') + 1);
                callingClass = callingClass.substring (callingClass.lastIndexOf ('$') + 1);

                caller = callingClass + "." + stackTraceElements[i].getMethodName ()+
                        "<"+stackTraceElements[i].getLineNumber ()+">";
                break;
            }
        }

        return String.format (Locale.CHINA, "[%d]%s:%s",
                Thread.currentThread ().getId (), caller,msg);

    }
}
