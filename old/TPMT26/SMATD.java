import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JOptionPane;
public class SMATD extends JFrame {
    private String fileChosenDir;
    private String outputType;
    public JTextField txtNoFileChosen;
    private SMAT smat;
    private JButton module_perf;
    private JButton stud_perf;
    private JButton stud_ranking;
    public SMATD(){
        //Initialize the main window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setUndecorated(true);
        getContentPane().setBackground(Color.white);
        setPreferredSize(new Dimension(660, 410));
        
        
        setResizable(false);
        
        //Absolute layout so components may be placed flexibly
        getContentPane().setLayout(null);
        
        JLabel label_uploadFiles = new JLabel("Upload files");
        label_uploadFiles.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
        label_uploadFiles.setBounds(35, 17, 460, 50);
        label_uploadFiles.setBackground(Color.white);
        getContentPane().add(label_uploadFiles);
        
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBounds(35, 81, 598, 292);
        getContentPane().add(panel);
        panel.setLayout(null);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 6, 299, 263);
        panel_1.setBorder(BorderFactory.createDashedBorder(Color.black, 2, 5, 6, false));
        
        //panel_1.setTransferHandler(new DragAndDrop(this));
        
        panel.add(panel_1);
        panel_1.setLayout(null);
        
        JLabel lblDragAndDrop = new JLabel("Drag and drop file");
        lblDragAndDrop.setBounds(93, 190, 116, 16);
        panel_1.add(lblDragAndDrop);
        
        ImageIcon imageIcon = new ImageIcon("Images/upload.png"); //Icon made by https://www.flaticon.com/authors/gregor-cresnar from www.flaticon.com
        Image scaledImage = imageIcon.getImage().getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageIcon = new ImageIcon(scaledImage);
        
        JLabel lblIcon = new JLabel(imageIcon);
        lblIcon.setOpaque(true);
        lblIcon.setBounds(91, 66, 120, 120);
        panel_1.add(lblIcon);
        
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(299, 0, 299, 282);
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
              protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){}
              protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                  if (isSelected) {
                      Graphics2D g2D = (Graphics2D) g;
                      g.setColor(Color.black);
                      g2D.setStroke(new BasicStroke(2));
                      g2D.drawLine(x+11, y+h, x+w-9, y+h);
                  }
                  
              }
              protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {}
        });
        tabbedPane.setBackground(Color.white);
        tabbedPane.setForeground(Color.black);
        tabbedPane.setOpaque(true);
        panel.add(tabbedPane);
        
        JPanel panel1 = new JPanel();
        panel1.setForeground(new Color(30, 144, 255));
        panel1.setBackground(Color.WHITE);
        panel1.setOpaque(false);
        tabbedPane.addTab("Options", null, panel1);
        panel1.setLayout(null);
        
        //Creating the upload button
        //Opens the system directory to chose the data file
        imageIcon = new ImageIcon("Images/button_choose-file.png"); //Icon made by https://www.flaticon.com/authors/gregor-cresnar from www.flaticon.com
        scaledImage = imageIcon.getImage().getScaledInstance(100, 30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageIcon = new ImageIcon(scaledImage);
        JButton btnUpload = new JButton(imageIcon);
        btnUpload.setBounds(16, 36, 100, 30);
        panel1.add(btnUpload);
        
        txtNoFileChosen = new JTextField();
        txtNoFileChosen.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
        txtNoFileChosen.setForeground(new Color(30, 144, 255));
        txtNoFileChosen.setText("    No file chosen");
        txtNoFileChosen.setEditable(false);
        txtNoFileChosen.setBounds(106, 33, 151, 36);
        panel1.add(txtNoFileChosen);
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.setBounds(12, 104, 245, 41);
        comboBox.addItem("PNG");
        comboBox.addItem("JTable");
        comboBox.addItem("PDF");
        panel1.add(comboBox);
        
        btnUpload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                jfc.setAcceptAllFileFilterUsed(false);

                int returnValue = jfc.showOpenDialog(null);
                
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File fileChosen = jfc.getSelectedFile();
                    
                    //Use this file to feed in data
                    fileChosenDir = fileChosen.getAbsolutePath();
                    txtNoFileChosen.setText("     "+fileChosen.getName());
                    
                    //enable the graphs
                    module_perf.setEnabled(true);
                    stud_perf.setEnabled(true);
                    stud_ranking.setEnabled(true);
                }
            }
        });
        
        JLabel lblOutputType = new JLabel("Output type");
        lblOutputType.setBounds(18, 93, 75, 16);
        panel1.add(lblOutputType);
        
        JButton btnGrw = new JButton("Convert");
        btnGrw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Use this to decide file output type
                outputType = (String)comboBox.getSelectedItem();
                if(fileChosenDir != null){smat = new SMAT(fileChosenDir, outputType);}
                else{JOptionPane.showMessageDialog(null, "No file uploaded.");}
            }
        });
        
        btnGrw.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnGrw.setBounds(12, 171, 117, 41);
        panel1.add(btnGrw);
        
        JButton btnQuit = new JButton("Quit");
        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                dispose();
            }
        });
        btnQuit.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        btnQuit.setBounds(139, 171, 117, 41);
        panel1.add(btnQuit);
        
        JPanel panel3 = new JPanel();
        panel3.setLayout(null);
        panel3.setOpaque(false);
        panel3.setBackground(Color.WHITE);
        tabbedPane.addTab("Visualisations", null, panel3, null);
        
        module_perf = new JButton("Module Performance");
        module_perf.setEnabled(false);
        module_perf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Use this to decide file output type
                outputType = (String)comboBox.getSelectedItem();
                if(fileChosenDir != null){
                    smat = new SMAT(fileChosenDir, "NULL");
                    
                    Module_Performance mg = new Module_Performance(smat.getStudents());
                    JScrollPane pane = new JScrollPane(mg);
                    
                    JDialog frame = new JDialog((JFrame) SwingUtilities.getRoot(getParent()), "Module Performance", true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setPreferredSize(new Dimension(800, 500));
                    frame.add(pane);
                    
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setResizable(true);
                }else{JOptionPane.showMessageDialog(null, "No file uploaded.");}
            }
        });
        module_perf.setBounds(62, 25, 155, 50);
        panel3.add(module_perf);
        
        stud_perf = new JButton("Student Performance");
        stud_perf.setEnabled(false);
        stud_perf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Use this to decide file output type
                outputType = (String)comboBox.getSelectedItem();
                if(fileChosenDir != null){
                    JDialog frame = new JDialog((JFrame) SwingUtilities.getRoot(getParent()), "Weak & Strong Student Bar Graph", true);
                    frame.setMinimumSize(new Dimension(1000, 555));
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    
                    smat = new SMAT(fileChosenDir, "NULL");
                    
                    Student_Performance graph = new Student_Performance(smat.getStudents());
            
                    frame.add(graph);
            
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }else{JOptionPane.showMessageDialog(null, "No file uploaded.");}
            }
        });
        stud_perf.setBounds(62, 93, 155, 50);
        panel3.add(stud_perf);
        
        stud_ranking = new JButton("Student Ranking");
        stud_ranking.setEnabled(false);
        stud_ranking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Use this to decide file output type
                outputType = (String)comboBox.getSelectedItem();
                if(fileChosenDir != null){
                    smat = new SMAT(fileChosenDir, "NULL");
                    Module_Performance mg = new Module_Performance(smat.getStudents());
                    Student_Ranking sr = new Student_Ranking(mg);
                    
                    JDialog frame = new JDialog((JFrame) SwingUtilities.getRoot(getParent()), "Student Ranking", true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setPreferredSize(new Dimension(800, 500));
                    frame.add(sr);
                    
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setResizable(true);
                }else{JOptionPane.showMessageDialog(null, "No file uploaded.");}
            }
        });
        stud_ranking.setBounds(59, 162, 155, 50);
        panel3.add(stud_ranking);
        
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Description", null, panel2);
        panel2.setOpaque(false);
        panel2.setBackground(Color.white);
        panel2.setLayout(null);
        
        StyledDocument document = new DefaultStyledDocument();
        Style style = document.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);
        JTextPane description = new JTextPane(document);
        description.setEditable(false);
        description.setText("The Education Committee (EC) of the CSEE department require "
                + "a tool to help them analyse student marks data from previous years. This "
                + "will help the planning of future modules to include into the CSEE degree, "
                + "and to fine tune existing modules to make their difficulty levels more appropriate.  "
                + "It also will allow identification of any weak or strong areas of teaching, so that high "
                + "teaching standards can be maintained.");
        description.setBounds(21, 19, 235, 211);
        panel2.add(description);
        
        JLabel lineBreak = new JLabel("");
        lineBreak.setBackground(Color.BLACK);
        lineBreak.setOpaque(true);
        lineBreak.setBounds(35, 62, 598, 2);
        getContentPane().add(lineBreak);
        
        pack();
        
        //Window initializes in the center of screen regardless of size
        setLocationRelativeTo(null);
        
        //Only set this after all items are added to the this
        setVisible(true);
    }
    
    public void setFileChosen(String fileChosenDir){this.fileChosenDir = fileChosenDir;}
}

 

