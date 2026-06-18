package com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds;

public class CruelAndUnusualConsole implements CruelAndUnusualFrontEnd {
    @Override
    public void run() {

    }

    @Override
    public void INIT() {

    }
    @Override
    public String toString(){
        return "CONSOLE";
    }
    @Override
    public void exit() {

    }
    @Override
    public String prompt(String p) {
        return prompt(p, false);
    }
    public String prompt(String p, boolean secret){
        if (!secret) return System.console().readLine();
        return new String(System.console().readPassword());
    }

    @Override
    public void configChanged() {

    }
}
