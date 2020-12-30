package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class JunitTest {
	
	public static final double APPROX_ZERO = 1E-10;
	
	// T variables are generated from this Test
	// S variables are generated from software being tested
	public static final String Tpdf = "TOutput.pdf";
	public static final String Tpng = "TOutput.png";
	public static final String Ttxt = "TOutput.txt";
	public static final String Spdf = "Output.pdf";
	public static final String Spng = "Output.png";
	public static final String Stxt = "Output.txt";
	public static final String path = System.getProperty("user.home") + "//Desktop//"; 
	
	
	
	// create stub-file types to compare instances.
	
	// stub for software files
    public static JTable createTable(HashMap<Integer, String[]> studentList){
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
	
	
	
	@BeforeClass
	public static void InstanceiateSoftwareFiles() throws Exception {
		
		//create PNG
		HashMap<Integer, String[]> newHM = new HashMap<>();
		String[] strT = {"t1","t2","t1"};
		String[] strT2 = {"t2","t3"};
		newHM.put(0,strT);
		newHM.put(1,strT);
		newHM.put(2,strT2);
		newHM.put(3,strT2);
		
		
		//SMAT test = new SMAT(path+Spng,"NULL",false,"email");
		JTable newT = createTable(newHM);
		
		BufferedImage newI = SMAT.createImage(newT);		
		SMAT.outputImage(newI);
		
		//create PDF
		BufferedImage[] imageArr = new BufferedImage[1];
		imageArr[0] =  ImageIO.read(new File(path+Spng));;
		
		ConvertToPDF emptyPDF = new ConvertToPDF(imageArr);
		emptyPDF.printImagePDF();	
	}
	
	
	// Planned to be use as a file comparator, it wasn't implemented.
	@BeforeClass
	public static void InstanceiateTestFiles() throws Exception {
		try {
			//create empty png
			BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
			ImageIO.write(image,"png", new File(path + Tpng));
			
			// create empty pdf
			Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path + Tpdf));
            document.open();
            document.addTitle("test");
            document.addSubject("test");
            document.addKeywords("test");
            document.addAuthor("test");
            document.addCreator("test"); 
            Paragraph preface = new Paragraph();
            preface.add(new Paragraph(" "));
            document.add(preface);
            document.newPage();  
            document.close();  
		}
		catch(Exception e) {
			e.printStackTrace();
	   }
	}
	
	
	@Test
	public void testConstructorLogin() {
		Login L = new Login();
		assertTrue(L instanceof Login);
		L.dispose();
	}
	
	@Test
	public void testConstructorRegistration() {
		Registration R = new Registration();
		assertTrue(R instanceof Registration);
		/* probably requires sys.exit(0) level commands
		 * and Junit modules that can handle sys.exit()
		 * inside tests */
//		R.setVisible(false);
//		R.dispose();
//		R.dispatchEvent(new WindowEvent(R, WindowEvent.WINDOW_CLOSING));
//		JDialog dialog = (JDialog) SwingUtilities.getRoot(R);
//		dialog.dispose();
//		R.actionPerformed(e);
	}
	
	@Test
	public void testConstructorSMAT_PNG() {
		assertTrue((new SMAT(path+Tpng,"NULL",false,"email")) instanceof SMAT);
	}
	
	@Test
	public void testConstructorSMAT_PDF() {
		assertTrue((new SMAT(path+Tpdf,"NULL",false,"email")) instanceof SMAT);
	}
	
	
	@Test
	public void TestPDFFormat() throws IOException {
		new PdfReader(path+Spdf);
		new PdfReader(path+Tpdf);

	}
	
	@Test
	public void TestPNGFormat() throws IOException {
		ImageIO.read(new File(path+Spng));
		ImageIO.read(new File(path+Tpng));

	}
	
	
    Registration test_reg;
    //call login, saves password, generate hash, send password
    //pass password to login
    @Test
    public void checkHash(){
        //code checks to see if string is in md5 hashed format
        String test_pass = "Hello";
        String hashed_pass = Login.hashPassword(test_pass);
        assertTrue(hashed_pass.matches("^[a-fA-F0-9]{32}$"));
        assertFalse(test_pass.matches("^[a-fA-F0-9]{32}$"));
    }
    @Test
    public void checkLogin(){
        test_reg = new Registration();
        String holder = "Hello";
        String wrong_input = "Hey";
        try {
            test_reg.saveData2Flie("Hello");
            String s = Login.checkDB(holder);
            assertEquals(s, holder);
            assertNotEquals(s, wrong_input);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
       
    
    public Module_Performance abc;

    @Test
    public void testAvg(){
        HashMap <Integer, String[]> test_list = new HashMap<>();
        String[] dummy_top_array = {"CE101-4-FY","CE101-4-SP", "CE101-4-SP", "CE101-4-SP", "CE101-4-SP"};
        String[] dummy_array = {"23", "46", "77", "24", "12"};
        String[] dummy_array1 = {"40", "23", "12", "21", "57"};
        String[] dummy_array2 = {"57", "11", "19", "65", "88"};
        String[] dummy_array3 = {"88", "16", "79", "31", "8"};
        test_list.put(0, dummy_top_array);
        test_list.put(1, dummy_array);
        test_list.put(2, dummy_array1);
        test_list.put(3, dummy_array2);
        test_list.put(4, dummy_array3);
        abc = new Module_Performance(test_list);
        int get_avg = abc.averages.get(0);
        System.out.println(get_avg);
        assertEquals(52, get_avg);
    }
	
	
	//do cleanup	
	@AfterClass
	static public void Cleanup() throws IOException {
		String[] dels= {Tpdf,Tpng,Spdf,Spng};
		for (String i:  dels) {
		Files.deleteIfExists(Paths.get(path + i));
		}
	}

}
