package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
public class DnDPanel extends JPanel {
   public DnDPanel(SMATD s) {
       setOpaque(false);
       setLayout(null);
       setPreferredSize(new Dimension(299, 263));
       setBorder(BorderFactory.createDashedBorder(Color.black, 2, 5, 6, false));
       
       JList list = new JList();
       list.setDragEnabled(true);
       list.setTransferHandler(new DragAndDrop(list, s));
       list.setOpaque(false);

       list.setFocusable(false);
        
        JScrollPane pane = new JScrollPane(list);
        pane.setOpaque(false);
        pane.setBounds(0, 0, 299, 263);
        pane.getViewport().setOpaque(false);
        pane.setFocusable(false);
        
        JLabel lblDragAndDrop = new JLabel("Drag and drop file");
        lblDragAndDrop.setBounds(86, 236, 114, 16);
        add(lblDragAndDrop);
        add(pane);
        
        //Icon made by https://www.flaticon.com/authors/gregor-cresnar from www.flaticon.com
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Images/upload.png"));
        Image scaledImage = imageIcon.getImage().getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageIcon = new ImageIcon(scaledImage);
        JLabel lblIcon = new JLabel(imageIcon);
        lblIcon.setBounds(0, 0, 299, 263);
        lblIcon.setOpaque(true);
        add(lblIcon);
   }
}