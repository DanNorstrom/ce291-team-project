import java.awt.Color;
import java.awt.Scrollbar;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.regex.Pattern;

public class Student_Performance extends JPanel implements AdjustmentListener, ItemListener {
    private HashMap<Integer, String[]> studs;
    private String orderModule;

    private JComboBox<String> module_list;
    private Scrollbar scrollbar;

    private final int PASS_MARK = 40;
    private final int DIST_MARK = 70;
    private final int[] MARK_LINES = new int[] {PASS_MARK, DIST_MARK};
    
    public Student_Performance(HashMap<Integer, String[]> studs) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 555));
        this.studs = studs;

        JPanel bottom = new JPanel(new GridBagLayout());
        add(bottom, BorderLayout.SOUTH);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        module_list = new JComboBox<>(getModuleList());
        module_list.setSelectedIndex(1);
        module_list.addItemListener(this);
        
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        bottom.add(this.module_list, c);

        this.scrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 0, 500, 0, 500);
        this.scrollbar.addAdjustmentListener(this);
        c.weightx = 0.9;
        c.gridx = 1;
        bottom.add(this.scrollbar, c);
    }
    
    public String[] getModuleList(){
        ArrayList<String> modules = new ArrayList<>();
        Pattern modulePat = Pattern.compile("^\\w{5}-\\d-\\w{2}$");
        boolean markColumn = false;

        for (String s : this.studs.get(0)) {
            if (modulePat.matcher(s).matches() || markColumn) {
                modules.add(s);
            }
            if (s.isEmpty()){markColumn = true;}
        }
        return modules.toArray(new String[modules.size()]);
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
        repaint();
    }
    
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            getScrollBar().setValue(0);
            repaint();
        }
    }
    
    public Scrollbar getScrollBar(){return scrollbar;}
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.getBackground());
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.translate(-this.scrollbar.getValue(), 0);
        
        String column = (String) this.module_list.getSelectedItem();
        int[] colIndex = {0};
        int barMargin = 45;
        float barHeight;
        int mark;
        int barStuds = 0;

        for (int i = 0; i < this.studs.get(0).length; i++) {
            if (column.equals(this.studs.get(0)[i])) {
                colIndex[0] = i;
                break;
            }
        }

        if (!column.equals(this.orderModule)) {
            String[] titles = this.studs.get(0);
            this.studs.remove(0);

            List<String[]> listOfStuds = new ArrayList<>(this.studs.values());
            Collections.sort(listOfStuds, (s1, s2) -> {
                if (s1[colIndex[0]].isEmpty() || s2[colIndex[0]].isEmpty()) {
                    if (s1[colIndex[0]].isEmpty() && s2[colIndex[0]].isEmpty()) {
                        return 0;
                    } else {
                        if (s1[colIndex[0]].isEmpty()) {
                            return -1;
                        }
                        return 1;
                    }
                } else {
                    int i1 = Integer.parseInt(s1[colIndex[0]]);
                    int i2 = Integer.parseInt(s2[colIndex[0]]);
                    return Integer.compare(i1, i2);
                }
            });

            this.studs.clear();
            this.studs.put(0, titles);

            for (int j = 0; j < listOfStuds.size(); j++) {
                this.studs.put(j + 1, listOfStuds.get(j));
            }

            this.orderModule = column;
        }

        for (int k = 1; k < this.studs.size(); k++) {
            if (!this.studs.get(k)[colIndex[0]].isEmpty()) {
                mark = Integer.parseInt(this.studs.get(k)[colIndex[0]]);
                g2d.setColor(mark >= this.DIST_MARK? Color.decode("#DAA520") : mark >= this.PASS_MARK? Color.decode("#008c00") : Color.RED);
                barHeight = mark * (this.getHeight() / 120f);
                g2d.fill(new Rectangle2D.Float(barMargin, (this.getHeight() - 80) - barHeight, 50, barHeight));
                g2d.drawString(this.studs.get(k)[0], barMargin, this.getHeight() - 50);
                if (barHeight < 25) {
                    g2d.setColor(Color.RED);
                    g2d.drawString(this.studs.get(k)[colIndex[0]], barMargin + 20, (this.getHeight() - 75) - Math.round(barHeight) - 10);
                } else {
                    g2d.setColor(this.getBackground());
                    g2d.drawString(this.studs.get(k)[colIndex[0]], barMargin + 20, (this.getHeight() - 75) - Math.round(barHeight) / 2);
                }
                barMargin += 75;
                barStuds++;
            }
        }
        
        if (barStuds > 1) {
            this.scrollbar.setVisibleAmount(500);
            int max = barStuds * 72;
            this.scrollbar.setMaximum(max);
        } else {
            this.scrollbar.setMaximum(0);
            this.scrollbar.setVisibleAmount(500);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine(30, this.getHeight() - 70, this.scrollbar.getValue() + this.getWidth(), this.getHeight() - 70);
        g2d.drawString("Student Registration Number", this.scrollbar.getValue() + (this.getWidth() / 2) - 70, this.getHeight() - 30);

        g2d.drawLine(30, this.getHeight() - 70, 30, 30);
        AffineTransform old = g2d.getTransform();
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Mark", -this.getHeight() / 2, 20);
        g2d.setTransform(old);

        for (int mark_line : this.MARK_LINES) {
            float mark_height = mark_line * (this.getHeight() / 120f);

            g2d.draw(new Line2D.Float(30, this.getHeight() - 80 - mark_height, (this.getWidth() / 2) + this.scrollbar.getValue() - 5, this.getHeight() - 80 - mark_height));
            g2d.drawString(String.valueOf(mark_line), this.scrollbar.getValue() + (this.getWidth() / 2), this.getHeight() - 75 - mark_height);
            g2d.draw(new Line2D.Float((this.getWidth() / 2) + this.scrollbar.getValue() + (String.valueOf(mark_line).length() * 9), this.getHeight() - 80 - mark_height, this.scrollbar.getValue() + this.getWidth(), this.getHeight() - 80 - mark_height));
        }
    }
    
    public void saveImage(String name,String type) {
        BufferedImage image = new BufferedImage((int)getPreferredSize().getWidth(),(int)getPreferredSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        try{
            ImageIO.write(image, type, new File(name+"."+type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * For testing purposes
     * 
     * public static void main(String[] args) {
        JFrame frame = new JFrame("Weak & Strong Student Bar Graph");
        frame.setMinimumSize(new Dimension(1000, 555));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        SMAT lmao = new SMAT("ExampleDataFileToBeConvertedIntoGraphs.csv", "NULL");
        
        Student_Performance graph = new Student_Performance(lmao.getStudents());
        graph.saveImage("output","png");
        
        frame.add(graph);

        frame.pack();
        frame.setVisible(true);
    }*/
}
