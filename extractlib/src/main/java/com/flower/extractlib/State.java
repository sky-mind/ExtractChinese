package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/26.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public abstract class State {
    protected String replacedLine;
    public abstract  boolean handle(BufferedReader reader,String line,int from,PrintWriter output, BufferedWriter writer);
}