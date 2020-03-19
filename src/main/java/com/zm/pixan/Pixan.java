package com.zm.pixan;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Pixan extends Frame {
    Label minxLB, minyLB, maxxLB, maxyLB, fileLB, dxmLB, dxMLB, dymLB, dyMLB;
    Checkbox logxCBX, logyCBX;
    TextField fileTF, minxTF, minyTF, maxxTF, maxyTF;
    TextArea logTA;
    Button browseBT, okBT, dxmBT, dxMBT, dymBT, dyMBT, saveBT, clearBT, closeBT;
    PixCanvas pc;
    Pixan instance;
    PixanDat pdat;

    GridBagLayout gbl;
    GridBagConstraints gbc;

    void renewGbc(int wx, int wy, int gw, int gh, int px, int py) {
	// resetting GBC
	gbc.weightx = wx;
	gbc.weighty = wy;
	gbc.gridwidth = gw;
	gbc.gridheight = gh;
	gbc.gridx = px;
	gbc.gridy = py;
    }

    class browseAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    FileSelect fs = new FileSelect(new Frame(),"Load File",true,0);
	    fileTF.setText(fs.getFileName());
	    String fName = new String(fs.getFileName());
	    pc.setNewImage(fName);
	}
    }

    class defineAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Object o = e.getSource();
	    if (o.equals(dxmBT)) pdat.pick = 1;
	    else if (o.equals(dxMBT)) pdat.pick = 2;
	    else if (o.equals(dymBT)) pdat.pick = 3;
	    else if (o.equals(dyMBT)) pdat.pick = 4;
	}
    }

    class okAL implements ActionListener {
        @Override
	public void actionPerformed(ActionEvent e) {
	    pdat.xfi = Double.parseDouble(minxTF.getText());
	    pdat.xff = Double.parseDouble(maxxTF.getText());
	    pdat.yfi = Double.parseDouble(minyTF.getText());
	    pdat.yff = Double.parseDouble(maxyTF.getText());
	}
    }

    class saveAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    FileSelect fs = new FileSelect(new Frame(),"Save Data",true,2);
	    File f = new File(fs.getFileName());
	    try {
		FileWriter fw = new FileWriter(f);
		BufferedWriter bfw = new BufferedWriter(fw);
		bfw.write(logTA.getText());
		bfw.close();
	    } catch (IOException ee) { System.out.println("Pixan::savaAL I/O Exception"); }
	}
    }

    class clearAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    logTA.setText("");
	}
    }

    class closeAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    dispose();
	}
    }

    public void setPick(int value) {
        pdat.pick = value;
    }
    
    public void setLimit(int value) {
        if (pdat.pick == 1) { pdat.xi = value; dxmLB.setText(String.valueOf(value)); }
	else if (pdat.pick == 2) { pdat.xf = value; dxMLB.setText(String.valueOf(value)); }
	else if (pdat.pick == 3) { pdat.yi = value; dymLB.setText(String.valueOf(value)); }
	else if (pdat.pick == 4) { pdat.yf = value; dyMLB.setText(String.valueOf(value)); }
    }

    public Pixan() {
	super.setTitle("PictureAnalyst");
	instance = this;
	gbl = new GridBagLayout();
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	gbc.insets = new Insets(5,5,5,5);
	setLayout(gbl);
	setSize(500,400);

	renewGbc(1,0,2,1,0,0);
	fileLB = new Label("Pix File");
	gbl.setConstraints(fileLB,gbc);
	add(fileLB);
	renewGbc(1,0,2,1,2,0);
	fileTF = new TextField(10);
	gbl.setConstraints(fileTF,gbc);
	add(fileTF);
	renewGbc(1,0,2,1,4,0);
	browseBT = new Button("Browse");
	gbl.setConstraints(browseBT,gbc);
	add(browseBT);
	browseBT.addActionListener(new browseAL());

	renewGbc(1,0,1,1,0,1);
	dxmBT = new Button("Define MinX");
	gbl.setConstraints(dxmBT,gbc);
	add(dxmBT);
	dxmBT.addActionListener(new defineAL());
	renewGbc(1,0,1,1,1,1);
	dxmLB = new Label("0");
	gbl.setConstraints(dxmLB,gbc);
	add(dxmLB);
	renewGbc(1,0,2,1,2,1);
	Label def1LB = new Label(" -----> ");
	gbl.setConstraints(def1LB,gbc);
	add(def1LB);
	renewGbc(1,0,1,1,4,1);
	minxLB = new Label("MinX");
	gbl.setConstraints(minxLB,gbc);
	add(minxLB);
	renewGbc(1,0,1,1,5,1);
	minxTF = new TextField("0",10);
	gbl.setConstraints(minxTF,gbc);
	add(minxTF);

	renewGbc(1,0,1,1,0,2);
	dxMBT = new Button("Define MaxX");
	gbl.setConstraints(dxMBT,gbc);
	add(dxMBT);
	dxMBT.addActionListener(new defineAL());
	renewGbc(1,0,1,1,1,2);
	dxMLB = new Label("0");
	gbl.setConstraints(dxMLB,gbc);
	add(dxMLB);
	renewGbc(1,0,2,1,2,2);
	Label def2LB = new Label(" -----> ");
	gbl.setConstraints(def2LB,gbc);
	add(def2LB);
	renewGbc(1,0,1,1,4,2);
	maxxLB = new Label("MaxX");
	gbl.setConstraints(maxxLB,gbc);
	add(maxxLB);
	renewGbc(1,0,1,1,5,2);
	maxxTF = new TextField("0",10);
	gbl.setConstraints(maxxTF,gbc);
	add(maxxTF);

	renewGbc(1,0,1,1,0,3);
	dymBT = new Button("Define MinY");
	gbl.setConstraints(dymBT,gbc);
	add(dymBT);
	dymBT.addActionListener(new defineAL());
	renewGbc(1,0,1,1,1,3);
	dymLB = new Label("0");
	gbl.setConstraints(dymLB,gbc);
	add(dymLB);
	renewGbc(1,0,2,1,2,3);
	Label def3LB = new Label(" -----> ");
	gbl.setConstraints(def3LB,gbc);
	add(def3LB);
	renewGbc(1,0,1,1,4,3);
	minyLB = new Label("MinY");
	gbl.setConstraints(minyLB,gbc);
	add(minyLB);
	renewGbc(1,0,1,1,5,3);
	minyTF = new TextField("0",10);
	gbl.setConstraints(minyTF,gbc);
	add(minyTF);

	renewGbc(1,0,1,1,0,4);
	dyMBT = new Button("Define MaxY");
	gbl.setConstraints(dyMBT,gbc);
	add(dyMBT);
	dyMBT.addActionListener(new defineAL());
	renewGbc(1,0,1,1,1,4);
	dyMLB = new Label("0");
	gbl.setConstraints(dyMLB,gbc);
	add(dyMLB);
	renewGbc(1,0,2,1,2,4);
	Label def4LB = new Label(" -----> ");
	gbl.setConstraints(def4LB,gbc);
	add(def4LB);
	renewGbc(1,0,1,1,4,4);
	maxyLB = new Label("MaxY");
	gbl.setConstraints(maxyLB,gbc);
	add(maxyLB);
	renewGbc(1,0,1,1,5,4);
	maxyTF = new TextField("0",10);
	gbl.setConstraints(maxyTF,gbc);
	add(maxyTF);

	renewGbc(1,0,2,1,0,5);
	logxCBX = new Checkbox("X Axis Logaritmic", false);
	gbl.setConstraints(logxCBX,gbc);
	add(logxCBX);
	logxCBX.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
            pdat.logX = (e.getStateChange() == 1);
            }
        });
	renewGbc(1,0,2,1,2,5);
	logyCBX = new Checkbox("Y Axis Logaritmic", false);
	gbl.setConstraints(logyCBX,gbc);
	add(logyCBX);
	logyCBX.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
            pdat.logY = (e.getStateChange() == 1);
            }
        });
	renewGbc(1,0,2,1,4,5);
	okBT = new Button("Define Axis");
	gbl.setConstraints(okBT,gbc);
	add(okBT);
	okBT.addActionListener(new okAL());

	renewGbc(1,1,1,1,0,6);
	logTA = new TextArea("",50,10,TextArea.SCROLLBARS_BOTH);
	gbl.setConstraints(logTA,gbc);
	add(logTA);
	Font fMS = new Font("MonoSpaced",Font.PLAIN,12);
	logTA.setFont(fMS);
	pdat = new PixanDat(logTA);
	renewGbc(1,1,GridBagConstraints.REMAINDER,GridBagConstraints.REMAINDER,1,6);
	pc = new PixCanvas(this);
	gbl.setConstraints(pc,gbc);
	add(pc);

	renewGbc(1,0,1,1,0,7);
	saveBT = new Button("Save");
	gbl.setConstraints(saveBT,gbc);
	add(saveBT);
	saveBT.addActionListener(new saveAL());

	renewGbc(1,0,1,1,0,8);
	clearBT = new Button("Clear");
	gbl.setConstraints(clearBT,gbc);
	add(clearBT);
	clearBT.addActionListener(new clearAL());

	renewGbc(1,0,1,1,0,9);
	closeBT = new Button("Close");
	gbl.setConstraints(closeBT,gbc);
	add(closeBT);
	closeBT.addActionListener(new closeAL());

	setVisible(true);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    dispose();
		}
	    });
    }

    public static void main(String[] args) {
	Pixan px = new Pixan();
    }

}  