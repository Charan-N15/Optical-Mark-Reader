import FileIO.PDFHelper;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

// Author: David Dobervich (this is my edit)
public class OpticalMarkReaderMain {

    public static void main(String[] args) {

//        String pathToPdf = fileChooser();
//        System.out.println("Loading pdf at " + pathToPdf);
//        Student s = new Student();

//
        ArrayList<ArrayList> students = new ArrayList<>();
        students.add(getStudentArray(2));
        students.add(getStudentArray(3));
        students.add(getStudentArray(4));
        students.add(getStudentArray(5));


        int page = 2;
        ArrayList<ArrayList> answers = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            answers.add(crossCheck(getAnsArray(),students.get(i),page));
            page++;
        }

        for (int i = 0; i < answers.size(); i++) {
            writeDataToFile("src/StudentAnswers.txt",answers.get(i));
        }






        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */
    }


    public static ArrayList<String> getAnsArray() {
        short[][] grid;
        ArrayList<String> answers = new ArrayList<>();
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
        DImage img = new DImage(in);
        grid = img.getBWPixelGrid();
        int count = 1;
        int blackCount = 0;
        int biggestPrevValue = 0;
        int ans = -1;

        //rows start at 455 and end at 490
        //columns start at 408 and end at 608
        //dist between each choice is 38 pixels.
        // rows now start at 456 and go to 493 (37 pixels).
        for (int n = 114; n <= 996; n += 294) {
            for (int j = 1; j <= 25; j++) { //problems
                for (int i = 1; i <= 5; i++) { //multiple choice
                    for (int r = 456 + ((j - 1) * 37); r < 456 + (j * 37); r++) {
                        for (int c = n + ((i - 1) * 38); c < n + (i * 38); c++) {
                            if (grid[r][c] < 100) blackCount++;
                        }
                    }
                    if (blackCount > biggestPrevValue) ans = count;
                    biggestPrevValue = Math.max(blackCount, biggestPrevValue);
                    blackCount = 0;
                    count++;
                }
                if (ans == 1) answers.add("A");
                if (ans == 2) answers.add("B");
                if (ans == 3) answers.add("C");
                if (ans == 4) answers.add("D");
                if (ans == 5) answers.add("E");
//                System.out.println((count2 + ": " + answers.get(count2 - 1)));
                biggestPrevValue = 0;
                count = 1;
                ans = -1;
            }
        }


        return answers;
    }

    public static ArrayList<String> getStudentArray(int page) {
        short[][] grid;
        ArrayList<String> answers = new ArrayList<>();
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf",page);
        DImage img = new DImage(in);
        grid = img.getBWPixelGrid();
        int count = 1;
        int blackCount = 0;
        int biggestPrevValue = 0;
        int ans = -1;
        int count2 = 1;

        for (int n = 114; n <= 996; n += 294) {
            for (int j = 1; j <= 25; j++) { //problems
                for (int i = 1; i <= 5; i++) { //multiple choice
                    for (int r = 456 + ((j - 1) * 37); r < 456 + (j * 37); r++) {
                        for (int c = n + ((i - 1) * 38); c < n + (i * 38); c++) {
                            if (grid[r][c] < 100) blackCount++;
                        }
                    }
                    if (blackCount > biggestPrevValue) ans = count;
                    biggestPrevValue = Math.max(blackCount, biggestPrevValue);
                    blackCount = 0;
                    count++;
                }
                if (ans == 1) answers.add("A");
                if (ans == 2) answers.add("B");
                if (ans == 3) answers.add("C");
                if (ans == 4) answers.add("D");
                if (ans == 5) answers.add("E");

                biggestPrevValue = 0;
                count = 1;
                ans = -1;
                count2++;
            }
        }
        return answers;
    }


    public static ArrayList<String> crossCheck(ArrayList<String> key, ArrayList<String> student, int page) {

        ArrayList<String> ans = new ArrayList<>();
        key = getAnsArray();
        student = getStudentArray(page);
        for (int i = 0; i < key.size(); i++) {
            if (student.get(i).equals(key.get(i))) ans.add("yes");
            else ans.add("no");


        }
        return ans;
    }

    public static ArrayList<String> totalAnsArray(ArrayList<String> key, ArrayList<String> student){
        ArrayList<String> totalAns = new ArrayList<>();
        ArrayList<String> ans1 = crossCheck(key,student,1);
        ArrayList<String> ans2 = crossCheck(key,student,2);
        ArrayList<String> ans3 = crossCheck(key,student,3);
        ArrayList<String> ans4 = crossCheck(key,student,4);
        ArrayList<String> ans5 = crossCheck(key,student,5);
        ArrayList<String> ans6 = crossCheck(key,student,6);
        for (int i = 0; i < ans1.size(); i++) {
            totalAns.add(ans1.get(i));
        }
        for (int i = 0; i < ans1.size(); i++) {
            totalAns.add(ans2.get(i));
        }
        for (int i = 0; i < ans1.size(); i++) {
            totalAns.add(ans3.get(i));
        }
        for (int i = 0; i < ans1.size(); i++) {
            totalAns.add(ans4.get(i));
        }
        for (int i = 0; i < ans1.size(); i++) {
            totalAns.add(ans5.get(i));
        }
        for (int i = 0; i < ans1.size(); i++) {
            totalAns.add(ans6.get(i));
        }
        return totalAns;
    }



    public static void writeStudentResults(String filepath, ArrayList<String> answers){
        try (FileWriter f = new FileWriter(filepath, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {

            for (int j = 0; j < answers.size()/6; j++) {
                    
            }

            writer.println(answers);


        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filepath);
            error.printStackTrace();
        }
    }

    public static void writeDataToFile(String filePath, ArrayList<String> answers) {


        try (FileWriter f = new FileWriter(filePath, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {
            int size = answers.size();
            int numWrong = 0;


            for (int i = 0; i < size; i++) {
                if(answers.get(i).equals("no")) numWrong++;
            }
            int correct = size - numWrong;
            writer.println("This student got " + correct + "/" + size );
            writer.println();

            for (int i = 0; i < answers.size(); i++) {
                writer.println((i + 1) + ": " + answers.get(i));
            }
            writer.println();






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
