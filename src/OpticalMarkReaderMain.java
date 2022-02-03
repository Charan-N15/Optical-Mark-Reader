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

    public static void printAns() {
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
        DImage img = new DImage(in);
        short[][] grid = img.getBWPixelGrid();


        int count = 1;
        int blackCount = 0;
        int biggestPrevValue = 0;
        int ans = -1;


        //rows start at 455 and end at 490
        //columns start at 408 and end at 608
        //dist between each choice is 38 pixels.
        // rows now start at 456 and go to 493 (37 pixels).

        for (int j = 1; j < 17; j++) { //problems
            for (int i = 1; i <= 5; i++) { //multiple choice
                for (int r = 456 + ((j - 1) * 37); r < 456 + (j * 37); r++) {
                    for (int c = 408 + ((i - 1) * 38); c < 408 + (i * 38); c++) {
                        if (grid[r][c] < 100) blackCount++;

                    }
                }
                if (blackCount > biggestPrevValue) ans = count;
                biggestPrevValue = Math.max(blackCount,biggestPrevValue);
                blackCount = 0;
                count++;

            }
            System.out.println(ans);
            biggestPrevValue = 0;
            count = 1;
            ans = -1;

        }
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
