package com.EmacsIsBetterThanVi.CruelAndUnusual.Components;

// Each screen is handled as a class which implements CruelScreen so we can instantiate it
public interface CruelScreen {
    void run();
    void shutdown();
}
