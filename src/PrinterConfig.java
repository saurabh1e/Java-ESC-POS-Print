import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrinterConfig {

    public final byte[] CTL_LF = {0x0a}; // Print and line feed
    public static final byte[] CAN_HT = {0x1b, 0x44, 0x00}; // Cancel  Horizontal Tab
    public static final byte[] HT = {0x09}; // Horizontal Tab
    public static final byte[] LINE_SPACE_24 = {0x1b, 0x33, 24}; // Set the line spacing at 24
    public static final byte[] LINE_SPACE_30 = {0x1b, 0x33, 30}; // Set the line spacing at 30
    //Image
    public static final byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33};
    // Printer hardware
    public static final byte[] HW_INIT = {0x1b, 0x40}; // Clear data in buffer and reset modes
    // Cash Drawer
    public static final byte[] CD_KICK_2 = {0x1b, 0x70, 0x00}; // Sends a pulse to pin 2 []
    public static final byte[] CD_KICK_5 = {0x1b, 0x70, 0x01}; // Sends a pulse to pin 5 []
    // Paper
    public static final byte[] PAPER_FULL_CUT = {0x1d, 0x56, 0x00}; // Full cut paper
    public static final byte[] PAPER_PART_CUT = {0x1d, 0x56, 0x01}; // Partial cut paper
    // Text format
    public static final byte[] TXT_NORMAL = {0x1b, 0x21, 0x00}; // Normal text
    public static final byte[] TXT_2HEIGHT = {0x1b, 0x21, 0x10}; // Double height text
    public static final byte[] TXT_2WIDTH = {0x1b, 0x21, 0x20}; // Double width text
    public static final byte[] TXT_4SQUARE = {0x1b, 0x21, 0x30}; // Quad area text
    public static final byte[] TXT_UNDERL_OFF = {0x1b, 0x2d, 0x00}; // Underline font OFF
    public static final byte[] TXT_UNDERL_ON = {0x1b, 0x2d, 0x01}; // Underline font 1-dot ON
    public static final byte[] TXT_UNDERL2_ON = {0x1b, 0x2d, 0x02}; // Underline font 2-dot ON
    public static final byte[] TXT_BOLD_OFF = {0x1b, 0x45, 0x00}; // Bold font OFF
    public static final byte[] TXT_BOLD_ON = {0x1b, 0x45, 0x01}; // Bold font ON
    public static final byte[] TXT_FONT_A = {0x1b, 0x4d, 0x00}; // Font type A
    public static final byte[] TXT_FONT_B = {0x1b, 0x4d, 0x01}; // Font type B
    public static final byte[] TXT_ALIGN_LT = {0x1b, 0x61, 0x00}; // Left justification
    public static final byte[] TXT_ALIGN_CT = {0x1b, 0x61, 0x01}; // Centering
    public static final byte[] TXT_ALIGN_RT = {0x1b, 0x61, 0x02}; // Right justification
    public static final byte[] LEFT_MARGIN = {0x1b, 0x6c, 0x08}; // Left Margin

    public byte[] TWO_HT = new byte[]{};
    public byte[] THREE_HT = new byte[]{};
    public String DIV = "";
    public HashMap<String, byte[]> code = new HashMap<String, byte[]>();

    private static String getCode(byte[] code) {

        try {
            return new String(code, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    public void setSize(Integer size) {
        switch (size) {
            case (80): {
                TWO_HT = new byte[]{0x1b, 0x44, 0x16, 0x24, 0x00};
                THREE_HT = new byte[]{0x1b, 0x44, 0x20, 0x28, 0x36, 0x00};
                DIV = "----------------------------------------------";
                break;
            }
            case (72): {
                TWO_HT = new byte[]{0x1b, 0x44, 0x10, 0x16, 0x00};
                THREE_HT = new byte[]{0x1b, 0x44, 0x14, 0x20, 0x25, 0x00};
                DIV = "---------------------------------------";
                break;
            }
            case (58): {
                TWO_HT = new byte[]{0x1b, 0x44, 0x08, 0x16, 0x00};
                THREE_HT = new byte[]{0x1b, 0x44, 0x12, 0x16, 0x25, 0x00};
                DIV = "-----------------------------------";
                break;
            }
        }
    }

    public String getFormattedString(String content) {

        String pattern = "<(.|\\n)+?>";
        List<String> allMatches = new ArrayList<>();

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(content);
        while (m.find()) {
            allMatches.add(m.group(0));
        }

        for (String allMatch : allMatches) {
            String code = allMatch;
            try {
                if (code.substring(1, code.length() - 1).equals("DIV")) {
                    code = (String) ReflectUtils.getValueOf(this, code.substring(1, code.length() - 1));
                } else {
                    code = getCode((byte[]) ReflectUtils.getValueOf(this, code.substring(1, code.length() - 1)));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            content = content.replaceFirst(allMatch, code);
        }
        return content;
    }

}

