package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JPanel;
public class Student_Ranking extends JPanel implements AdjustmentListener, ItemListener{
    private HashMap<String, Integer> students_ranked;
    private Scrollbar scrollbar;
    private final int PASS_MARK = 40;
    private final int DIST_MARK = 70;
    private final int[] MARK_LINES = new int[] {PASS_MARK, DIST_MARK};
    public Student_Ranking(Module_Performance mg){
        setBackground(Color.white);
        setPreferredSize(new Dimension(1200, 410));
        setLayout(new BorderLayout());
        
        this.scrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 0, 500, 0, 500);
        this.scrollbar.addAdjustmentListener(this);
        this.students_ranked = rankStudents(mg);
        
        add(scrollbar, BorderLayout.SOUTH);
    }
    
    public HashMap<String, Integer> rankStudents(Module_Performance mg){
        HashMap<String, Integer> students_ranked = new HashMap<>();
        HashMap<String, Integer> avgList = mg.getModuleAvgs();
        ArrayList<String[]> studs = mg.getStudents();
        ArrayList<Integer> modulesIndexs = moduleIndexes(avgList, studs);
        
        for(int i = 1;i<studs.size();i++){
            int totalMark = 0;
            int module_count = 0;
            for(int index : moduleIndexes(avgList, studs)){
                try{
                    double mark = Double.parseDouble(studs.get(i)[index]);
                    double avg = (double)avgList.get(studs.get(0)[index]);
                    avg /= 100;
                    
                    totalMark += Math.round(mark*avg);
                    module_count++;
                }catch (NumberFormatException e){continue;}
            }
            totalMark = Math.round(totalMark/(module_count));
            students_ranked.put(studs.get(i)[0], totalMark);
        }
        students_ranked = orderHashMap(students_ranked);
        return students_ranked;
    }
    
    // function to sort hashmap by values 
    public static HashMap<String, Integer> orderHashMap(HashMap<String, Integer> studs) { 
        // Create a list from elements of HashMap 
        java.util.List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer> >(studs.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >(){ 
            public int compare(Map.Entry<String, Integer> v1, Map.Entry<String, Integer> v2){ 
                return (v2.getValue()).compareTo(v1.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Integer> newMap = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> entry : list) { 
            newMap.put(entry.getKey(), entry.getValue()); 
        } 
        return newMap; 
    }
    
    public ArrayList<Integer> moduleIndexes(HashMap<String, Integer> avgList, ArrayList<String[]> studs){
        ArrayList<Integer> modulePos = new ArrayList<>();
        for(int i = 0;i<studs.get(0).length;i++){
            for (String module : avgList.keySet()){
                if(module == studs.get(0)[i]){modulePos.add(i);}
            }
        }
        return modulePos;
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
        repaint();
    }
    
    public Scrollbar getScrollBar(){return scrollbar;}
    
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            getScrollBar().setValue(0);
            repaint();
        }
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.translate(-scrollbar.getValue(), 0);
        
        int barMargin = 45;
        float barHeight;
        Integer mark = 0;
        int barStuds = 0;
        
        for (int currMark : students_ranked.values()){
            mark = currMark;
            g2d.setColor(mark >= DIST_MARK? Color.decode("#DAA520") : mark >= PASS_MARK? Color.decode("#008c00") : Color.RED);
            barHeight = mark * (getHeight() / 120f);
            g2d.fill(new Rectangle2D.Float(barMargin, (getHeight() - 80) - barHeight, 50, barHeight));
            String student ="";
            for(Map.Entry entry: students_ranked.entrySet()){
                if(mark.equals(entry.getValue())){
                    student = entry.getKey().toString();
                    break; //breaking because its one to one map
                }
            }
            g2d.drawString(student, barMargin, getHeight() - 50);
            if (barHeight < 25) {
                g2d.setColor(Color.RED);
                g2d.drawString(Integer.toString(mark), barMargin + 20, (getHeight() - 75) - Math.round(barHeight) - 10);
            } else {
                g2d.setColor(getBackground());
                g2d.drawString(Integer.toString(mark), barMargin + 20, (getHeight() - 75) - Math.round(barHeight) / 2);
            }
            barMargin += 75;
            barStuds++;
        }
        
        if (barStuds > 1) {
            scrollbar.setVisibleAmount(500);
            int max = barStuds * 72;
            scrollbar.setMaximum(max);
        } else {
            scrollbar.setMaximum(0);
            scrollbar.setVisibleAmount(500);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine(30, getHeight() - 70, scrollbar.getValue() + getWidth(), getHeight() - 70);
        g2d.drawString("Student Registration Number", scrollbar.getValue() + (getWidth() / 2) - 70, getHeight() - 30);

        g2d.drawLine(30, getHeight() - 70, 30, 30);
        AffineTransform old = g2d.getTransform();
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Overall Mark", -getHeight() / 2, 20);
        g2d.setTransform(old);

        for (int mark_line : MARK_LINES) {
            float mark_height = mark_line * (getHeight() / 120f);

            g2d.draw(new Line2D.Float(30, getHeight() - 80 - mark_height, (getWidth() / 2) + scrollbar.getValue() - 5, getHeight() - 80 - mark_height));
            g2d.drawString(String.valueOf(mark_line), scrollbar.getValue() + (getWidth() / 2), getHeight() - 75 - mark_height);
            g2d.draw(new Line2D.Float((getWidth() / 2) + scrollbar.getValue() + (String.valueOf(mark_line).length() * 9), getHeight() - 80 - mark_height, scrollbar.getValue() + getWidth(), getHeight() - 80 - mark_height));
        }
    }
    
    /*
     * For testing purposes
     * 
     * public static void main(String[] args){
        SMAT smat = new SMAT("ExampleDataFileToBeConvertedIntoGraphs.csv", "NULL");
        Module_Performance mg = new Module_Performance(smat.getStudents());
        Student_Ranking sr = new Student_Ranking(mg);
        
        JFrame frame = new JFrame("Module Performance");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 500));
        
        frame.add(sr);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
    }*/
}
