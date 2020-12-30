 

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class SMAT {
    private JTable table;
    private String student;
    private int count=0;
    private HashMap<Integer, String[]> studentList;
    private String fileDirectory;
    private String outputType;
    public SMAT (String dir, String outputType){
        this.fileDirectory = dir;
        this.outputType = outputType;
        try {
            studentList = getInput();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "File not found.");
        }
        
        //Creating JTable with input values
        table = createTable(studentList);
        
        //Decide which output to give
        switch (outputType){
            case "PNG":
                //Printing out each student as png
                BufferedImage image = createImage(table);
                try {
                    outputImage(image);
                    
                    File file = new File(System.getProperty("user.home")+"/Desktop");
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    
                    JOptionPane.showMessageDialog(null, "PNG created.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Unable to create image file.");
                }
                break;
            case "JTable":
                 //Printing out each student as JTable
                JFrame frame = new JFrame();
                frame.setPreferredSize(new Dimension(1060, 410));
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(new JScrollPane(table));
                frame.pack();
                frame.setVisible(true);
                break;
            case "PDF":
                //Printing out as a PDF file
                Module_Performance mg = new Module_Performance(getStudents());
                JScrollPane pane = new JScrollPane(mg);
                Student_Ranking sr = new Student_Ranking(mg);
                
                BufferedImage imagecopy0 = createImage(table);
                BufferedImage module_Performance_image = createImage(mg);
                BufferedImage student_Ranking_image = createImage(sr);
                BufferedImage student_Performance_image = createImage(new Student_Performance(getStudents()));
                
                BufferedImage[] bfs = new BufferedImage[]{imagecopy0, student_Performance_image, module_Performance_image, student_Ranking_image};
                
                ConvertToPDF pdf = new ConvertToPDF(bfs);  
                pdf.printImagePDF();
                
                File file = new File(System.getProperty("user.home") + "//Desktop//Output.pdf");
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                    
                JOptionPane.showMessageDialog(null, "PDF created.");
                break;
            case "NULL":
                
                break;
            default: 
                JOptionPane.showMessageDialog(null, "Output error." + " type: " + outputType);
                break;
        }
    }
    
    public JTable createTable(HashMap<Integer, String[]> studentList){
        //Add each student to table model
        DefaultTableModel model = new DefaultTableModel(studentList.get(0), 0);
        for (String[] row : studentList.values()){
            if(Arrays.equals(row, studentList.get(0))){continue;}
            model.addRow(row);
        }
        
        //Initialize table with table model
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //Set horizontal alignment of text in each cell to be centered
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        //Table presentation: Column widths and alignment
        for (int i = 0; i < studentList.get(0).length-1; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            
            //Remove the blank column
            if(table.getColumnName(i).equals("")){
                table.removeColumn(column);
                i-=1;
            }
            
            switch (i){
                case 0:  column.setMaxWidth(85);
                         column.setMinWidth(85);
                         break;
                case 1:  column.setMaxWidth(40);
                         column.setMinWidth(40);
                         break;
                case 2:  column.setMaxWidth(100);
                         column.setMinWidth(100);
                         break;
                case 18: column.setMaxWidth(40);
                         column.setMinWidth(40);
                         column.setCellRenderer(centerRenderer);
                         break;
                default: column.setMaxWidth(76);
                         column.setMinWidth(76);
                         column.setCellRenderer(centerRenderer);
                         break;
            }
        }
        return table;
    }
    
    public static void outputImage(BufferedImage image) throws Exception {
        ImageIO.write(image,"png", new File(System.getProperty("user.home") +"//Output.png"));
    }
    
    public BufferedImage createImage(JPanel graph) {
        
        JScrollPane pane = new JScrollPane(graph);
        
        //This is done so we can print entire table including scrolling
        Dimension graphSize = graph.getPreferredSize();
        pane.getViewport().setViewSize(graphSize);
        
        //graph.setPreferredScrollableViewportSize(graphSize);
        graph.setSize(graphSize);
        
        
        BufferedImage image = new BufferedImage(graphSize.width, graphSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.createGraphics();
        graph.paint(g);
        g.dispose();
        
        return image;
    }
    
    public static BufferedImage createImage(JTable table){
        Dimension tableSize = table.getPreferredSize();
        //This is done so we can print entire table including scrolling
        table.setPreferredScrollableViewportSize(tableSize);
        table.setSize(tableSize);
        
        JTableHeader header =table.getTableHeader();
        Dimension headerSize = header.getPreferredSize();
        header.setSize(headerSize);
        
        BufferedImage output = new BufferedImage(tableSize.width, tableSize.height+header.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2D = (Graphics2D) output.createGraphics();
        header.paint(g2D);
        
        //To prevent painting over the header
        g2D.translate(0, header.getHeight());
        
        table.paint(g2D);
        g2D.dispose();

        return output;
    }
    
    public HashMap<Integer, String[]> getInput() throws IOException {
        
        HashMap<Integer, String[]> studentList = new HashMap<>();
        
        File csvFile = new File(fileDirectory);
        
        // create BufferedReader and read data from csv
        BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
        
        //Reading lines from csv into object hash map
        //Each hash value stores all date for one student only
        //key: 0, value: studentRegNo, Ex.no, stage, etc
        while ((student = csvReader.readLine()) != null){
            
            //Remove all special characters from the data to prevent injection
            student=student.replaceAll("[^a-zA-Z0-9,/-]", "");
            String[] data = student.split(",");
            
            //Check for duplicate entries
            if(studentList.containsValue(data)){
                System.out.println("Student " + data[0] + " already exists in the database.");
                continue;
            }
            
            //Add student to hashmap
            studentList.put(count, data);
            count+=1;
        }
        csvReader.close();
        return studentList;
    }
    
    public HashMap<Integer, String[]> getStudents(){
        return studentList;
    }
}