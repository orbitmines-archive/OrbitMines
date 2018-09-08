package net.firefang.ip2c;

import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class IpUtils {

    /*
    * OrbitMines - @author Fadi Shawki - 7/28/2017
    */
//    IP2Country.NO_CACHE;  // Straight on file, Fastest startup, slowest queries
//    IP2Country.MEMORY_MAPPED; // Memory mapped file, fast startup, fast quries.
//    IP2Country.MEMORY_CACHE; // load file into memory, slowerst startup, fastest queries
    private static IP2Country ip2c;

    static {
        try {
            ip2c = new IP2Country(IP2Country.MEMORY_CACHE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCountry(Player player) {
        return getCountry(player.getAddress().getHostString());
    }

    public static String getCountry(String ip) {
        Country c = null;
        try {
            c = ip2c.getCountry(ip);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        /*
            c.get2cStr() -> NL
            c.get3cStr() -> NLD
            c.getName() -> Netherlands
         */
        return c == null ? null : c.getName();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List readCSV(String string) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(string);
            List list = IpUtils.readCSV(fileInputStream);
            return list;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public static List readCSV(InputStream inputStream) throws IOException {
        String string = null;
        int n = -1;
        int n2 = -1;
        ArrayList<LineInfo> arrayList = new ArrayList<LineInfo>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((string = bufferedReader.readLine()) != null) {
            ++n;
            if (string.length() == 0 || string.trim().startsWith("#") || n2 == -1 && (n2 = IpUtils.detectFormat(string)) == -1)
                continue;
            LineInfo lineInfo = new LineInfo(string, n2, n);
            arrayList.add(lineInfo);
        }
        return arrayList;
    }

    public static int detectFormat(String string) {
        if (string.length() == 0 || string.trim().startsWith("#")) {
            return -1;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(string, "\",");
        if (stringTokenizer.countTokens() == 7) {
            System.err.println("Detected toMinecraft : SOFTWARE77");
            return 1;
        }
        if (stringTokenizer.countTokens() == 5) {
            System.err.println("Detected toMinecraft : WEBHOSTING_INFO");
            return 0;
        }
        return -1;
    }

    public static String randomizeIP() {
        short s = (short) (Math.random() * 256.0);
        short s2 = (short) (Math.random() * 256.0);
        short s3 = (short) (Math.random() * 256.0);
        short s4 = (short) (Math.random() * 256.0);
        return "" + s + "." + s2 + "." + s3 + "." + s4;
    }

    public static void removeOverlappings(List list) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (int i = 0; i < list.size(); ++i) {
            LineInfo lineInfo = i > 0 ? (LineInfo) list.get(i - 1) : null;
            LineInfo lineInfo2 = (LineInfo) list.get(i);
            if (lineInfo2.getEndIP() < lineInfo2.getStartIP()) {
                throw new IllegalArgumentException("start > end at line " + lineInfo2);
            }
            if (lineInfo == null || lineInfo2.getStartIP() >= lineInfo.getEndIP()) continue;
            System.err.println("Conflict between lines: \n" + lineInfo + "\n" + lineInfo2);
            if (lineInfo2.getEndIP() <= lineInfo.getEndIP()) {
                if (lineInfo2.getStartIP() == lineInfo.getStartIP() && lineInfo2.getEndIP() == lineInfo.getEndIP()) {
                    System.err.println("Conflicting ranges have identical start and end. removing " + lineInfo2);
                    arrayList.add(list.remove(i));
                    continue;
                }
                if (lineInfo2.getStartIP() == lineInfo.getStartIP()) {
                    arrayList.add(lineInfo.clone());
                    System.err.println("Extending the start of the outer range : ");
                    System.err.println("Outer range was:" + lineInfo);
                    lineInfo.setStartIP(lineInfo2.getEndIP() + 1);
                    System.err.println("Outer range is:" + lineInfo);
                    System.err.println("Inner range is:" + lineInfo2);
                    list.set(i, lineInfo);
                    list.set(i - 1, lineInfo2);
                    continue;
                }
                System.err.println("Dropping inner range : " + lineInfo2);
                arrayList.add(list.remove(i));
                continue;
            }
            if (lineInfo2.getEndIP() <= lineInfo.getEndIP()) continue;
            String string = "Conflict type 2, can't resolve";
            throw new IllegalArgumentException(string);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeCSV(List list, String string) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(string);
            IpUtils.writeCSV(list, outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void writeCSV(List list, OutputStream outputStream) throws IOException {
        for (int i = 0; i < list.size(); ++i) {
            LineInfo lineInfo = (LineInfo) list.get(i);
            outputStream.write(("\"" + lineInfo.getStartIP() + "\",\"" + lineInfo.getEndIP() + "\",\"" + lineInfo.getId2c() + "\",\"" + lineInfo.getId3c() + "\",\"" + lineInfo.getName() + "\"\n").getBytes());
        }
    }
}
