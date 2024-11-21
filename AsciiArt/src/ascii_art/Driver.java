package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.Arrays;
import java.util.logging.Logger;

public class Driver {

    private static final String FONT = "David";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " + args[0]);
            return;
        }
        new Shell(img).run();
    }
}
