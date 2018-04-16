package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/26.
 */

import java.io.BufferedReader;
import java.io.PrintWriter;

public class DataModel {
    private State state;
    private BufferedReader reader;
    private String line;
    private int from;
    private PrintWriter output;

    public DataModel(State state, BufferedReader reader, String line, int from,
                     PrintWriter output) {
        super();
        this.state = state;
        this.reader = reader;
        this.line = line;
        this.from = from;
        this.output = output;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }
}
