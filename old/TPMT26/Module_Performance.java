import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection; 
import java.util.HashMap;
import java.util.Random;
public class Module_Performance extends JPanel {

    private HashMap<Integer, String[]> studentList;
    private HashMap<String,  Integer> averageList = new HashMap<>();
    private ArrayList<String> modules = new ArrayList<>();
    private ArrayList<Integer> averages = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();
    private Random rand = new Random();
    private int panel_height;
    
    public Module_Performance(HashMap<Integer, String[]> studentList){
        this.studentList = studentList;
        setLayout(null);
        
        getAvg();
        
        setPreferredSize(new Dimension(1065, panel_height));
        
        addLables();
    }

    public void getAvg(){
        float avg_total = 0;
        float avg_count = 0;

        //int module_counter = 3;
        ArrayList<Integer> module_counter = new ArrayList<>();
        boolean markColumn = false;
        String[] column_headers = studentList.get(0);

        for (int c = 0; c < column_headers.length; c++){
            if(column_headers[c].matches("^\\w{5}-\\d-\\w{2}$") || markColumn){
                module_counter.add(c);
                modules.add(column_headers[c]);
            }
            if (column_headers[c].isEmpty()){markColumn = true;}
        }
        panel_height = module_counter.size() * 63;
        for (int j = 0; j < module_counter.size(); j++){
            for(int i = 1; i < studentList.size(); i++){
                try {
                    int mark = Integer.parseInt(studentList.get(i)[module_counter.get(j)]);
                    avg_total += mark;
                    avg_count += 1;
                }catch (NumberFormatException e){}
            }
            int average = Math.round(avg_total / avg_count);
            averages.add(average);
            colors.add(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
            avg_total = 0;
            avg_count = 0;
        }
    }
    
    public void addLables(){
        int count = 40;
        for (int i = 0; i < modules.size(); i++){
            JLabel label = new JLabel(modules.get(i),SwingConstants.CENTER);
            add(label);
            label.setBounds(20, count, 90, 20);
            count += 60;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int counter = 40;
        for (int i = 0; i < averages.size(); i++){
            Color random_color = colors.get(i);
            g.setColor(random_color);
            g.fillRect(115, counter, averages.get(i)* 10, 20);
            
            JLabel num_label  = new JLabel(Integer.toString(averages.get(i)), SwingConstants.CENTER);
            int x = averages.get(i) * 10 + 95;
            if(x<95+23){x=95+23;}
            num_label.setBounds(x, counter, 20, 20);
            num_label.setForeground(Color.white);
            add(num_label);
            
            counter += 60;
        }
        g.setColor(Color.black);
        int newcount = counter - 20;
        g.fillRect(115, 2, 5, newcount);
        g.fillRect(115, newcount, 1000, 5);
        
        JLabel twenty_five  = new JLabel("25", SwingConstants.CENTER);
        twenty_five.setBounds(315, newcount, 80, 30);
        g.fillRect(355, 2, 2, newcount);
        add(twenty_five);
        
        JLabel fifty = new JLabel("50", SwingConstants.CENTER);
        fifty.setBounds(565, newcount, 80, 30);
        g.fillRect(605, 2, 2, newcount);
        add(fifty);
        
        JLabel seventy_five  = new JLabel("75", SwingConstants.CENTER);
        seventy_five.setBounds(815, newcount, 80, 30);
        g.fillRect(855, 2, 2, newcount);
        add(seventy_five);
        
        JLabel one_hundred  = new JLabel("100", SwingConstants.CENTER);
        one_hundred.setBounds(1065, newcount, 80, 30);
        g.fillRect(1113, 2, 2, newcount);
        add(one_hundred);
    }
    
    public HashMap<String, Integer> getModuleAvgs(){
        for (int i = 0; i < modules.size(); i++){
            averageList.put(modules.get(i), averages.get(i));
        }
        return averageList;
    }
    
    public ArrayList<String[]> getStudents(){
        Collection<String[]> values = studentList.values();
        return new ArrayList<String[]>(values); 
    }
    
    /*
     * For testing purposes
     * 
     * public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(800, 500));

        SMAT lmao = new SMAT("ExampleDataFileToBeConvertedIntoGraphs.csv", "NULL");

        Module_Performance v = new Module_Performance(lmao.getStudents());
        JScrollPane pane = new JScrollPane(v);
        frame.add(pane);
        
        //Window initializes in the center of screen regardless of size
        //frame.setLocationRelativeTo(null);
        frame.pack();
        
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/
}
