package com.zm.pixan;
/* Prepare canvas and a routine to add DVH data */
import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class PixCanvas extends Canvas {
    Image img = null;
    Pixan lpx;

    public void setNewImage(String fName) {
	Toolkit t = this.getToolkit();
	img = t.getImage(fName);
	repaint();
    }

    public void paint(Graphics g) {
	Dimension d = getSize();
	if (img != null) g.drawImage(img,0,0,d.width-1,d.height-1,this);
	g.drawRect(5,5,d.width-10,d.height-10);
    }

    class ML extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
	    Dimension d = getSize();
	    int x = e.getX(), y = e.getY();
	    if (lpx.pdat.pick == 0) lpx.pdat.print(x,y);
	    else if (lpx.pdat.pick == 1) { lpx.pdat.xi = x; lpx.dxmLB.setText(String.valueOf(x)); }
	    else if (lpx.pdat.pick == 2) { lpx.pdat.xf = x; lpx.dxMLB.setText(String.valueOf(x)); }
	    else if (lpx.pdat.pick == 3) { lpx.pdat.yi = y; lpx.dymLB.setText(String.valueOf(y)); }
	    else if (lpx.pdat.pick == 4) { lpx.pdat.yf = y; lpx.dyMLB.setText(String.valueOf(y)); }
	    lpx.pdat.pick = 0;
	}
    }
 
    PixCanvas(Pixan px) {
	this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	lpx = px;
	addMouseListener(new ML());
    }

    PixCanvas(String fName, Pixan px) {
	this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	lpx = px;
	Toolkit t = this.getToolkit();
	img = t.getImage(fName);

	addMouseListener(new ML());
	repaint();
    }

    public static void main(String[] args) {
	Frame fr = new Frame();
	fr.setLayout(new GridLayout());
	fr.setSize(300,200);
	PixCanvas px = new PixCanvas("demo.jpg",null);
	fr.add(px);
	fr.setVisible(true);
    }
}