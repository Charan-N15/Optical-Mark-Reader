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

        ArrayList<ArrayList> answers = studentScoresContent();
        for (int i = 0; i < answers.size(); i++) {
            writeStudentScores("src/StudentAnswers.txt", answers.get(i));
        }

        writeAnalysis("src/ItemAnalysis.txt");
    }


    public static ArrayList<String> getAnsArray() {
        short[][] grid;
        ArrayList<String> answers = new ArrayList<>();
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
        DImage img = new DImage(in);
        grid = img.getBWPixelGrid();

        return loopOverOptions(grid, answers);
    }

    public static ArrayList<String> getStudentArray(int page) {
        short[][] grid;
        ArrayList<String> answers = new ArrayList<>();
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", page);
        DImage img = new DImage(in);
        grid = img.getBWPixelGrid();

        return loopOverOptions(grid, answers);
    }

    public static ArrayList<String> loopOverOptions(short[][] grid, ArrayList<String> answers) {
        int count = 1;
        int blackCount = 0;
        int biggestPrevValue = 0;
        int ans = -1;

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
                if (biggestPrevValue >= 50) {
                    answers.add(numberToString(ans));
                } else answers.add("No answer");

                biggestPrevValue = 0;
                count = 1;
                ans = -1;
            }
        }
        return answers;
    }

    public static String numberToString(int ans) {
        switch (ans) {
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            default:
                return "E";

        }

    }

    public static ArrayList<Integer> getStudentID(int page) {
        short[][] grid;
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", page);
        DImage img = new DImage(in);
        grid = img.getBWPixelGrid();

        int blackCount = 0;
        int biggestPrevValue = 0;
        ArrayList<Integer> studentID = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 4; j++) {
                for (int r = 331 + ((j - 1) * 22); r <= 331 + (j * 22); r++) {
                    for (int c = 66 + ((i - 1) * 54); c <= 66 + (i * 54); c++) {
                        if (grid[r][c] < 100) blackCount++;
                    }
                }
                biggestPrevValue = Math.max(blackCount, biggestPrevValue);
                blackCount = 0;
            }
            if (biggestPrevValue > 60) studentID.add(i - 1);
            biggestPrevValue = 0;
        }
        return studentID;
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

    public static ArrayList<String> totalAnsArray() {
        ArrayList<String> totalAns = new ArrayList<>();
        ArrayList<String> key = getAnsArray();
        ArrayList<String> ans1 = crossCheck(key, getStudentArray(1), 1);
        ArrayList<String> ans2 = crossCheck(key, getStudentArray(2), 2);
        ArrayList<String> ans3 = crossCheck(key, getStudentArray(3), 3);
        ArrayList<String> ans4 = crossCheck(key, getStudentArray(4), 4);
        ArrayList<String> ans5 = crossCheck(key, getStudentArray(5), 5);
        ArrayList<String> ans6 = crossCheck(key, getStudentArray(6), 6);

        return addToTotalAnswerArray(ans1, ans2, ans3, ans4, ans5, ans6, totalAns);
    }

    public static ArrayList<String> addToTotalAnswerArray(ArrayList<String> ans1, ArrayList<String> ans2, ArrayList<String> ans3,
                                                          ArrayList<String> ans4, ArrayList<String> ans5, ArrayList<String> ans6,
                                                          ArrayList<String> totalAns) {
        totalAns.addAll(ans1);
        totalAns.addAll(ans2);
        totalAns.addAll(ans3);
        totalAns.addAll(ans4);
        totalAns.addAll(ans5);
        totalAns.addAll(ans6);
        return totalAns;
    }


    public static int wrongPerQuestion(int a, ArrayList<String> totalAns) {
        int incorrectCount = 0;
        for (int i = a; i < totalAns.size(); i += 100) {
            if (totalAns.get(i).equals("no")) {
                incorrectCount++;
            }
        }
        return incorrectCount;
    }

    public static ArrayList<ArrayList> studentScoresContent() {
        ArrayList<ArrayList> students = new ArrayList<>();
        students.add(getStudentArray(2));
        students.add(getStudentArray(3));
        students.add(getStudentArray(4));
        students.add(getStudentArray(5));
        students.add(getStudentArray(6));
        int page = 2;
        ArrayList<ArrayList> answers = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            answers.add(crossCheck(getAnsArray(), students.get(i), page));
            page++;
        }
        return answers;
    }

    public static void writeStudentScores(String filePath, ArrayList<String> answers) {
        try (FileWriter f = new FileWriter(filePath, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b)) {
            int size = answers.size();
            int numWrong = 0;
            ArrayList<Integer> studentID = getStudentID(1);

            for (int i = 0; i < size; i++) {
                if (answers.get(i).equals("no")) numWrong++;
            }
            int correct = size - numWrong;
            writer.println(studentID.toString() + " got " + correct + "/" + size);
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

    public static void writeAnalysis(String filepath) {
        ArrayList<String> totalAns = totalAnsArray();
        try (FileWriter f = new FileWriter(filepath, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {

            for (int i = 1; i <= 100; i++) {
                writer.println((i) + ". " + wrongPerQuestion(i - 1, totalAns));
            }
        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filepath);
            error.printStackTrace();
        }
    }
}
