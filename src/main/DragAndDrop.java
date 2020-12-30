package main;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
public class DragAndDrop extends TransferHandler {
    private SMATD app;
    private JList list;
    DragAndDrop(JList list, SMATD app) {
        this.app = app;
        this.list = list;
    }
    
    @Override
    public boolean canImport(TransferSupport info) {
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
            return false;
        }
        return true;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferSupport info) {
        try {
            List<File> droppedFiles = (List<File>)info.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles) {
            	//Get file
            	String filePath = file.getAbsolutePath();
            	
            	//Get extension
            	String extension = filePath.substring(filePath.lastIndexOf("."));
            	
            	//Check file type
            	if(extension.equals(".csv")){
	                app.setFileChosen(file.getAbsolutePath());
	                app.txtNoFileChosen.setText("     " + file.getName());
	                
	                //enable the graphs
                    app.enableGraphs();
                }else{JOptionPane.showMessageDialog(null, "Invalid file type.");}
            }
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            return false;
        }
    }
}