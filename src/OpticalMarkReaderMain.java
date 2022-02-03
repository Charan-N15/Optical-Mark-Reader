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

        int length = 608;
        int count = 1;
        int blackCount = 0;
        int whiteCount = 0;
        int prevBlackCount = 0;
        int ans = -1;
        for (int r = 455; r < 490; r++) { //rows between each question is ~34 pixels
            for (int c = 400; c < length; c++) {
                for (int i = c; i <  c + (42 * count); i = i + 41) {
                    if(grid[r][i] > 100) whiteCount++;
                    if(grid[r][i] < 100) blackCount++;
                    if(blackCount > prevBlackCount) ans = count;


                    prevBlackCount = blackCount;


                }
                whiteCount = 0;
                blackCount = 0;
                count++;
            }
        }
        System.out.println(ans);

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
