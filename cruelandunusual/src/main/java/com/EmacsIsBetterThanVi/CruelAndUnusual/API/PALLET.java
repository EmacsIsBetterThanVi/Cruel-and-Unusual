package com.EmacsIsBetterThanVi.CruelAndUnusual.API;

import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.backPallet;

public final class PALLET {
    public static int handlePallet(float rf, float gf, float bf) {
        return handlePallet(rf, gf, bf, backPallet);
    }
    public static int handlePallet(int red, int green, int blue){
        float rf = red/255f, gf = green/255f, bf = blue/255f;
        return handlePallet(rf, gf, bf, backPallet);
    }
    public static int handlePallet(int red, int green, int blue, float[] p){
        float rf = red/255f, gf = green/255f, bf = blue/255f;
        return handlePallet(rf, gf, bf, p);
    }
    public static int handlePallet(float rf, float gf, float bf, float[] p){
        if (rf==0 && gf == 0 && bf == 0) return (p.length/3)-1;
        for (int i = 0; i < (p.length/3)-1; i++) {
            if (p[i*3]==rf && p[i*3+1]==gf && p[i*3+2]==bf) return i;
            else if (p[i*3]==0 && p[i*3+1]==0 && p[i*3+2]==0) {
                p[i*3]=rf;
                p[i*3+1]=gf;
                p[i*3+2]=bf;
                return i;
            }
        }
        return (p.length/3)-1; // Pallet error, always displays as black, use this for true black
    }
}
