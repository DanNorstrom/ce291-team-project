import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
/*
    # a import of <itextPDF VERSION 5.0.6> Provided in the same folder as this file(or link here).
    
    - itextpdf artifact 5.0.6 @ https://mvnrepository.com/artifact/com.itextpdf/itextpdf/5.0.6
    - program might complain on a static variable. solves if changed to method, not latent error
 */
public class ConvertToPDF {
    
    private static BufferedImage[] sl;
    
    public ConvertToPDF(BufferedImage[] stud) {
        this.sl = stud;
    }
    
    private static String FILE = System.getProperty("user.home") + "//Desktop//Output.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public void printImagePDF() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("Ce291 Group Project");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Ce291 T26");
        document.addCreator("Ce291 T26");
    }

    // basic front page for intro
    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("CE291 Group Project", catFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(), smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("This document holds module information ",smallBold));
        document.add(preface);
        document.newPage();
    }

    // adds a second page with a image
    private static void addContent(Document document) throws DocumentException, IOException {       
        for(BufferedImage bf : sl){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ImageIO.write(bf, "png", baos);
            Image slC = Image.getInstance(baos.toByteArray());      
            slC.setAlignment(Element.ALIGN_CENTER);
            slC.scaleAbsolute(document.getPageSize().getHeight() -80, document.getPageSize().getWidth());
            //Add to document
            slC.setRotationDegrees(90);
            document.add(slC);
            document.newPage();
        }
    }

    // helper method
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

