package com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds;

public interface CruelAndUnusualFrontEnd {
    void run();
    void INIT();
    void exit();
    String prompt(String p);
    String prompt(String p, boolean secret);
    void configChanged();
}
