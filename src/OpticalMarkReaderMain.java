import FileIO.PDFHelper;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.*;

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
        for (int n = 114; n <=996 ; n+=294) { //original values are 408 and 702
            for (int j = 1; j <= 25; j++) { //problems
                for (int i = 1; i <= 5; i++) { //multiple choice
                    for (int r = 456 + ((j - 1) * 37); r < 456 + (j * 37); r++) {
                        for (int c = n + ((i - 1) * 38); c < n + (i * 38); c++) {
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
    }

    public static String[] getAnsArray() {
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
        DImage img = new DImage(in);
        short[][] grid = img.getBWPixelGrid();


        int count = 1;
        int blackCount = 0;
        int biggestPrevValue = 0;
        int ans = -1;

        //array to store answers:
        String[] answers = new String[16];


        //rows start at 455 and end at 490
        //columns start at 408 and end at 608
        //dist between each choice is 38 pixels.
        // rows now start at 456 and go to 493 (37 pixels).
        for (int n = 408; n <= 702; n += 294) {
            for (int j = 1; j < 17; j++) { //problems
                for (int i = 1; i <= 5; i++) { //multiple choice
                    for (int r = 456 + ((j - 1) * 37); r < 456 + (j * 37); r++) {
                        for (int c = 408 + ((i - 1) * 38); c < 408 + (i * 38); c++) {
                            if (grid[r][c] < 100) blackCount++;

                        }
                    }
                    if (blackCount > biggestPrevValue) ans = count;
                    biggestPrevValue = Math.max(blackCount, biggestPrevValue);
                    blackCount = 0;
                    count++;

                }
                if (ans == 1) answers[j - 1] = "A";
                if (ans == 2) answers[j - 1] = "B";
                if (ans == 3) answers[j - 1] = "C";
                if (ans == 4) answers[j - 1] = "D";
                if (ans == 5) answers[j - 1] = "E";
                biggestPrevValue = 0;
                count = 1;
                ans = -1;

            }
        }
        return answers;
    }

    public static void writeDataToFile(String filePath, String []answers) {
        try (FileWriter f = new FileWriter(filePath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {

            for (int i = 0; i < 16; i++) {
                writer.println((i+26)+", "+answers[i]);
            }


        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
        }
    }

    public static void writeDataToFile1(String filePath, String data) {
        try (FileWriter f = new FileWriter(filePath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {


            writer.println(data);


        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
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
