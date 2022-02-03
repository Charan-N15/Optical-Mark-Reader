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

public class FilterTest implements Drawable {
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        SaveAndDisplayExample();
        RunTheFilter();
        OpticalMarkReaderMain.printAns();

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

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
//        DisplayWindow.showFor(window.ellipse(500,500,100,100));
    }
}