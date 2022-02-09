import FileIO.PDFHelper;
import Filters.DisplayInfoFilter;
import Filters.FixedThresholdFilter;
import Interfaces.Drawable;
import Interfaces.PixelFilter;
import com.jogamp.newt.Display;
import core.DImage;
import core.DisplayWindow;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class FilterTest{
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        SaveAndDisplayExample();
        RunTheFilter();

        ArrayList<ArrayList> students = new ArrayList<>();
        students.add(OpticalMarkReaderMain.getStudentArray(2));
        students.add(OpticalMarkReaderMain.getStudentArray(3));
        students.add(OpticalMarkReaderMain.getStudentArray(4));
        students.add(OpticalMarkReaderMain.getStudentArray(5));
        students.add(OpticalMarkReaderMain.getStudentArray(6));
        students.add(OpticalMarkReaderMain.getStudentArray(7));

        int page = 2;
        ArrayList<ArrayList> answers = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            answers.add(OpticalMarkReaderMain.crossCheck(OpticalMarkReaderMain.getAnsArray(),students.get(i),page));
            page++;
        }

        for (int i = 0; i < answers.size(); i++) {
            OpticalMarkReaderMain.writeDataToFile("src/MyFile.txt",answers.get(i));
        }


//        OpticalMarkReaderMain.writeDataToFile("src/MyFile.txt", OpticalMarkReaderMain.crossCheck(OpticalMarkReaderMain.getAnsArray(),OpticalMarkReaderMain.getStudentArray()));


    }

    private static void RunTheFilter() {
        System.out.println("Loading pdf....");
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf",1);
        DImage img = new DImage(in);       // you can make a DImage from a PImage





        System.out.println("Running filter on page 1....");
        DisplayInfoFilter filter = new DisplayInfoFilter();
        filter.processImage(img);  // if you want, you can make a different method
                                   // that does the image processing an returns a DTO with
                                   // the information you want
    }

    private static void SaveAndDisplayExample() {
        PImage img = PDFHelper.getPageImage("assets/omrtest.pdf",1);
        img.save(currentFolder + "assets/page1.png");
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf",1);

        DisplayWindow.showFor("assets/page1.png");

    }


}