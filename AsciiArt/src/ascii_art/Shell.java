package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.*;

public class Shell {
    private static final String CMD_EXIT = "exit";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    public static final int MIN_PIXELS_PER_CHAR = 1;
    private static final String FONT = "Courier New";
    private static final String INITIAL_CHARS_RANGE = "0-9";
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private final BrightnessImgCharMatcher brightnessImgCharMatcher;
    private final HtmlAsciiOutput htmlAsciiOutput;
    private int charsInRow;
    private final Image img;
    private Set<Character> charSet = new HashSet<>();

    public Shell(Image img) {

        this.img = img;
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        brightnessImgCharMatcher = new BrightnessImgCharMatcher(img, FONT);
        htmlAsciiOutput = new HtmlAsciiOutput("out.html", FONT);

        addChars(INITIAL_CHARS_RANGE);
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var cmd = "";
        while(!cmd.equalsIgnoreCase(CMD_EXIT))
        {
            try {

            System.out.print(">>> ");
            var input = scanner.nextLine().trim().split(" ");
            cmd = input[0];
            switch (cmd){
                case "chars":
                    showChars();
                    break;
                case "add":
                    addChars(input[1]);
                    break;
                case "remove":
                    removeChars(input[1]);
                    break;
                case "res":
                    resChange(input[1]);
                    System.out.println("resolution: " + charsInRow + " in a row");
                    break;
                case "render":
                    render();
                    break;
            }
            }
            catch (IllegalArgumentException e){
                System.out.println("invalid argument: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void render() {
        var asciiImg = brightnessImgCharMatcher.chooseChars(charsInRow, charSet.toArray(new Character[0]));
        htmlAsciiOutput.output(asciiImg);
    }

    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        for(char c = range[0]; c <= range[1]; ++c) {
            charSet.remove(c);
        }
    }

    private void showChars() {
        charSet.stream().sorted().forEach(c-> System.out.print(c + " "));
        System.out.println();
    }

    public static char[] parseCharRange(String param){
        switch (param){
            case "space":
                return new char[]{' ', ' '};
            case "all":
                return new char[]{' ', '~'};
        }

        var parsed = param.toCharArray();

        if(parsed.length == 1){
            return new char[]{parsed[0], parsed[0]};
        }

        if(parsed.length != 3 ||
                parsed[0] > parsed[2] ||
                parsed[1] != '-'){
            throw new IllegalArgumentException(param);
        }
        return new char[]{parsed[0], parsed[2]};
    }

    private void addChars(String s) {
        char[] range = parseCharRange(s);
            for(char c = range[0]; c <= range[1]; ++c) {
                charSet.add(c);
            }
    }
    private void resChange(String s){
        switch (s){
            case "up":
                charsInRow = Math.min(charsInRow * 2, maxCharsInRow);
                break;
            case "down":
                charsInRow = Math.max(charsInRow / 2, minCharsInRow);
                break;
            default: throw new IllegalArgumentException(s);
        }
    }

}
