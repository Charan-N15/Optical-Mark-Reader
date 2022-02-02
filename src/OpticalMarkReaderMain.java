import FileIO.PDFHelper;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.File;

// Author: David Dobervich (this is my edit)
public class OpticalMarkReaderMain {

    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);


        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */


    }

    public static void getLength(){
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf",1);
        DImage img = new DImage(in);
        short[][] grid = img.getBWPixelGrid();
        int length = 490;
        for (int r = 450; r < length; r++) { //rows between each question is ~34 pixels
            for (int c = 427; c < 600; c++) {
                for (int i = r; i < length ; i = i + r / 5) {
                    if(grid[r][c] > 1);
                }


            }
        }
        System.out.println(grid.length);

    }



    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }
}
