package com.zm.pixan;
import java.awt.*;
public class PixanDat {
    int pick;
    public boolean logX, logY;
    public int xi, xf, yi, yf;
    public double xfi, xff, yfi, yff;
    TextArea logTA;

    double scale(double v, boolean apply) {
        if (apply) {
            return Math.pow(10.0, v);
        }
        
        return v;
    }
    
    public void print(int i, int j) {
	double x, y;
	//x = (i-xi) * (scale(xff, logX) - scale(xfi, logX)) / (xf-xi);
	//y = (j-yi) * (scale(yff, logY) - scale(yfi, logY)) / (yf-yi);
        x = scale(xfi + (i-xi) * (xff - xfi) / (xf - xi), logX);
	y = scale(yfi + (j-yi) * (yff - yfi) / (yf - yi), logY);
	String value = new String(String.valueOf(x) + ", " + String.valueOf(y) + "\n");
	logTA.append(value);
    }

    public PixanDat(TextArea l) {
	logTA = l;
	pick = 0;
        logX = logY = false;
    }
}