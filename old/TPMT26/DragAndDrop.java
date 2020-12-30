import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
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
import java.util.*;
import javax.swing.TransferHandler;
public class DragAndDrop extends TransferHandler {

    private SMATD app;

    DragAndDrop(SMATD app) {
        this.app = app;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean canImport(TransferSupport info) {
        if (!info.isDrop() && !info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        
        List<File> droppedFiles;
        try {
            droppedFiles = (List<File>)info.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            System.out.println(droppedFiles.size());
            String path = droppedFiles.get(0).getPath();
            return path.substring(path.lastIndexOf('.') + 1, path.length()).equals("csv");
        } catch (java.awt.dnd.InvalidDnDOperationException e) {
            System.out.println("test");
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            System.out.println("test1");
            return false;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }

        try {
            List<File> droppedFiles = (List<File>)info.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            String path = droppedFiles.get(0).getPath();path = path.substring(1, path.length() - 1);
            File file = new File(path);
            if (!file.exists()) {return false;}
            app.setFileChosen(path);
            app.txtNoFileChosen.setText("     " + file.getName());
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            return false;
        }
    }
}