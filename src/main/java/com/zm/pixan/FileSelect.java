package com.zm.pixan;
/* Window for specifying a target file (either for writing or reading). Only a filename as string is
   return. FileSelect requires some ini information - starting directory and similar, see constructors.
   There is a bug when a non-existing file is specified - FUT */
//package RTgeneric;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

public class FileSelect extends Dialog {
    private String fileName;                 // Name for file to be selected
    static String sep = new String(System.getProperty("file.separator"));
    String defaultDir;                       // Window opens with this path
    String curDir;                           // Current working directory
    String[] defDirs;                        // Default shortcuts to common directories
    String filterMask = new String("*.*");   // File extension filter
    GridBagLayout gbl;
    GridBagConstraints gbc;
    Color pozadiTF = new Color(150,100,80);
    File soubor;                             // To test whether fileName is a file or a directory AND 
                                             // to get absolute paths
    int save = 0;                            // is file for reading or writing?
    boolean SELECTED = false;                // if file was selected enable closing the window
    TextField fileInput, filterTF;
    java.awt.List defDirList, dirList, fileList;
    Button rescanBT, okBT, cancelBT;
    Label filterLB;
    int i;

    public String getFileName() {
	// Access to private variable
	return fileName;
    }

    void setDefaults(String[] defaults) {
	/* Setting defaults in constructor call. These are directories, they must end with sep
	   0 : default
           1 : current
           2 - n-2 : directory shortcuts 
           n-2 : filter */
	int i;
	defaultDir = new String(defaults[0]);
	if (!defaultDir.substring(defaultDir.length()-1).equals(sep)) defaultDir += sep;
	curDir = new String(defaults[1]);
	if (!curDir.substring(curDir.length()-1).equals(sep)) curDir += sep;
	defDirs = new String[defaults.length-3];
	for (i=2;i<defaults.length-1;i++)
	    {
		defDirs[i-2] = new String(defaults[i]);
		if (!defDirs[i-2].substring(defDirs[i-2].length()-1).equals(sep)) defDirs[i-2] += sep;
	    }
	filterMask = defaults[defaults.length-1];
	return;
    }

    void renewGbc(int wx, int wy, int gw, int gh, int px, int py) {
	// resetting GBC
	gbc.weightx = wx;
	gbc.weighty = wy;
	gbc.gridwidth = gw;
	gbc.gridheight = gh;
	gbc.gridx = px;
	gbc.gridy = py;
	return;
    }

    void initFileInput() {
	// Field to keep the file name
	fileInput = new TextField(defaultDir);
       	fileInput.setBackground(pozadiTF);
	fileInput.addActionListener(new textInputAL());
	return;
    }

    void initList1() {
	// Default shortcuts to directories
	int i;
	defDirList = new java.awt.List(3);
	for (i=0;i<defDirs.length;i++) defDirList.add(defDirs[i]);
	defDirList.addActionListener(new defDirListAL());
	defDirList.addItemListener(new defDirListIL());
	return;
    }

    void initButton1() {
	// Updating current directory
	rescanBT = new Button("Rescan Directory");
	rescanBT.addActionListener(new rescanAL());
	return;
    }

    void initFilter() {
	// Filters
	filterTF = new TextField(filterMask);
       	filterTF.setBackground(pozadiTF);
	filterTF.addActionListener(new filterTFAL());
	return;
    }

    void initDirList() {
	// Directory listing initialization
	dirList = new java.awt.List(15);
	dirList.addActionListener(new dirListAL());
	dirList.addItemListener(new dirListIL());
	return;
    }

    void initFileList() {
	// File listing initialization
	fileList = new java.awt.List(15);
	fileList.addActionListener(new fileListAL());
	fileList.addItemListener(new fileListIL());
	return;
    }

    void initOkButton() {
	// Select the file
	okBT = new Button("OK");
	if (save != 2) okBT.setEnabled(false);
	okBT.addActionListener(new okAL());
	return;
    }

    void initCancelButton() {
	// Abort action
	cancelBT = new Button("Cancel");
	cancelBT.addActionListener(new okAL());
	return;
    }

    void placeComponents() {
	// Visualize
	renewGbc(1,0,GridBagConstraints.REMAINDER,1,0,0);
	initFileInput();
	gbl.setConstraints(fileInput,gbc);
	add(fileInput);
	
	renewGbc(1,0,5,3,0,1);
	initList1();
	gbl.setConstraints(defDirList,gbc);
	add(defDirList);

	renewGbc(1,0,15,1,5,1);
	initButton1();
	gbl.setConstraints(rescanBT,gbc);
	add(rescanBT);

	renewGbc(1,0,1,2,5,2);
	filterLB = new Label("Filter: ");
	gbl.setConstraints(filterLB,gbc);
	add(filterLB);

	renewGbc(1,0,GridBagConstraints.REMAINDER,2,6,2);
	initFilter();
	gbl.setConstraints(filterTF,gbc);
	add(filterTF);

	renewGbc(1,1,5,15,0,4);
	initDirList();
	gbl.setConstraints(dirList,gbc);
	add(dirList);

	renewGbc(1,1,GridBagConstraints.REMAINDER,15,5,4);
	initFileList();
	gbl.setConstraints(fileList,gbc);
	add(fileList);
	
	renewGbc(0,0,5,1,0,22);
	initOkButton();
	gbl.setConstraints(okBT,gbc);
	add(okBT);

	renewGbc(0,0,15,1,5,22);
	initCancelButton();
	gbl.setConstraints(cancelBT,gbc);
	add(cancelBT);
	return;
    }

    class textInputAL implements ActionListener {
	/* Reaction to ENTER in TI - if a file is chosen, we're done, otherwise
           current directory curDir updated */
	public void actionPerformed(ActionEvent e) {
	    fileName = fileInput.getText();
	    soubor = new File(fileName);
	    if (save == 1 && soubor.isFile()) {
		curDir = fileName.substring(0,fileName.lastIndexOf(sep)) + sep;
		dispose();
	    } else {
		if (soubor.isDirectory()) {
		    if (!fileName.substring(fileName.length()-1).equals(sep)) fileName = fileName + sep;
		    fileInput.setText(fileName);
		    resetCurDir(fileName);
		} else {
		    curDir = fileName.substring(0,fileName.lastIndexOf(sep)) + sep;
		    dispose();
		}
	    }
	    return;
	}
    }

    class filterTFAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    // List fitered
	    filterMask = filterTF.getText();
	    soubor = new File(curDir);
	    resetCurDir(soubor.getAbsolutePath()+sep);
	    return;
	}
    }

    class defDirListAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    // Using shortcuts to dirs
	    soubor = new File(defDirList.getSelectedItem());
	    String newPath = new String(soubor.getAbsolutePath());
	    if (!newPath.substring(newPath.length()-1).equals(sep)) newPath += sep;
	    resetCurDir(newPath);
	    return;
	}
    }

    class defDirListIL implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    // In future, user will be able to configure this area
	    return;
	}
    }

    class rescanAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    // Update current directory
	    soubor = new File(curDir);
	    String newPath = new String(soubor.getAbsolutePath());
	    if (!newPath.substring(newPath.length()-1).equals(sep)) newPath += sep;
	    resetCurDir(newPath);
	    return;
	}
    }

    class dirListAL implements ActionListener {
	// Using one of directories in currdir
	String p;

	public void actionPerformed(ActionEvent e) {
	    if (SELECTED) { SELECTED = false; okBT.setEnabled(false); } // For those who cannot decide
	    p = curDir.substring(0,curDir.lastIndexOf(sep));
	    if (dirList.getSelectedItem().equals("..")) curDir = p.substring(0,p.lastIndexOf(sep)) + sep;
	    else curDir = curDir + dirList.getSelectedItem() + sep;
	    soubor = new File(curDir);
	    String newPath = new String(soubor.getAbsolutePath());
	    if (!newPath.substring(newPath.length()-1).equals(sep)) newPath += sep;
	    resetCurDir(newPath);
	    return;
	}
    }

    class dirListIL implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    // No action here
	    return;
	}
    }

    class fileListAL implements ActionListener {
	// Selecting file -- if already SELECTED, change it and reselect
	public void actionPerformed(ActionEvent e) {
	    if (SELECTED) curDir = curDir.substring(0,curDir.lastIndexOf(sep)) + sep;
	    fileName = curDir + fileList.getSelectedItem();
	    fileInput.setText(fileName);
	    curDir = fileName.substring(0,fileName.lastIndexOf(sep)) + sep;
	    SELECTED = true;
	    okBT.setEnabled(true);
	    return;
	}
    }

    class fileListIL implements ItemListener {
	public void itemStateChanged(ItemEvent e) {
	    return;
	}
    }


    class okAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if (fileName.equals("")) fileName = fileInput.getText();
	    dispose();
	    return;
	}
    }

    class cancelAL implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    fileName = null;
	    System.out.println(fileName);
	    dispose();
	    return;
	}
    }

    String setMask(String maskName) {
	/* String is transformed to the filter (wildcards are erased) */
	if ((maskName.length() > 2) && maskName.substring(0,2).equals("*.") && (maskName.charAt(2) != '*')) return maskName.substring(1,maskName.length());
	else return maskName;
    }

    boolean acceptable(String fileName, String maskName) {
	/* True if file fits the mask */
	if (maskName.equals("*.*") || maskName.equals("*") || maskName.length() == 0) return true;
	if (fileName.length() < maskName.length()) return false;
	if (fileName.substring(fileName.length()-maskName.length()).equals(maskName)) return true;
	else return false;
    }

    void resetCurDir(String newCurDir) {
	/* New current directory */
	curDir = newCurDir;
	fileInput.setText(curDir);
	getDirsAndFiles(curDir);
	return;
    }

    void getDirsAndFiles(String currentDir) {
	/* Reads the directory for files, mask is applied. */
	int i;
	File fileFoo, wd = new File(currentDir);
	String maska = new String(setMask(filterMask));
	String[] vypis;
	//	FilterExts fE = new FilterExts(filterMask);

	dirList.removeAll();
	fileList.removeAll();
	if (!curDir.equals(sep)) dirList.add("..");
	vypis = wd.list();
	mySort(vypis);
	for (i=0;i<vypis.length;i++)
	    {
		fileFoo = new File(wd.getAbsolutePath() + sep + vypis[i]);
		if (fileFoo.isDirectory()) dirList.add(fileFoo.getName());
		else if (fileFoo.isFile() && acceptable(fileFoo.getName(),maska)) fileList.add(fileFoo.getName());
	    }
	return;
    }	

    void mySort(String strings[]) {
	// Sorts array of string. CollationKey speeds it up
	Collator en_USCollator = Collator.getInstance(new Locale("en","US"));
	CollationKey[] keys = new CollationKey[strings.length+1];
	String tmp;
        
	for (int i = 0; i < strings.length; i++) keys[i] = en_USCollator.getCollationKey(strings[i]);
	for (int i = 0; i < strings.length; i++) {
	    for (int j = i + 1; j < strings.length; j++) { 
		if (keys[i].compareTo(keys[j]) > 0) {
		    keys[strings.length] = keys[i];
		    keys[i] = keys[j];
		    keys[j] = keys[strings.length];
		}
	    }
	}
	for (int i = 0; i < strings.length; i++) strings[i] = keys[i].getSourceString();
	return;
    }

    public FileSelect(Frame voidFrame,String title, boolean modal, int toBeSaved) {
	// Constructor without defaults - setting them and calling real constructor
	// toBeSaved - 0 for reading, 1 for saving as a file, 2 for directory
	super(voidFrame,title,modal);
	String[] defs = new String[6];

	defs[0] = new String(System.getProperty("user.home") + sep);
	defs[1] = new String(System.getProperty("user.home") + sep);
	defs[2] = new String(sep);
	defs[3] = new String(sep + "home" + sep);
	defs[4] = new String(System.getProperty("user.home") + sep);
	defs[5] = new String("*.*");
	// Real constructor starts here:
	setSize(400,350);
	save = toBeSaved;
	fileName = new String("");
	gbl = new GridBagLayout();
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	gbc.insets = new Insets(5,5,5,5);

	setDefaults(defs);
	placeComponents();
	resetCurDir(curDir);

	this.setLayout(gbl);
	this.setVisible(true);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    dispose();
		    return;
		}
	    });	
    }

    public FileSelect(Frame voidFrame,String title, String[] defs, boolean modal, int toBeSaved) {
	super(voidFrame,title,modal);
	setSize(400,350);
	save = toBeSaved;
	fileName = new String("");
	gbl = new GridBagLayout();
	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	gbc.insets = new Insets(5,5,5,5);

	setDefaults(defs);
	placeComponents();
	resetCurDir(defaultDir);

	this.setLayout(gbl);
	this.setVisible(true);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    dispose();
		    return;
		}
	    });
    }

    public static void main(String argv[]) {
	String[] defs = new String[7];
	defs[0] = new String(System.getProperty("user.home") + sep);
	defs[1] = new String(System.getProperty("user.home") + sep);
	defs[2] = new String(sep);
	defs[3] = new String(sep + "home" + sep);
	defs[4] = new String(System.getProperty("user.home") + sep);
	defs[5] = new String(sep + "mnt" + sep);
	defs[6] = new String("*.*");
	FileSelect fs = new FileSelect(new Frame(),"Selecting a File",defs,true,0);
	System.out.println(fs.getFileName());
	return;
    }
} 