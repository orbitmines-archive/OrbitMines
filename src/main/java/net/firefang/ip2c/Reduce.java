package net.firefang.ip2c;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Reduce {
    public static void main(String[] arrstring) throws NumberFormatException, IOException {
        if (arrstring.length < 2) {
            System.err.println("Reduce input-csv output-csv");
            return;
        }
        Reduce.reduce(arrstring[0], arrstring[1]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void reduce(String string, String string2) throws IOException {
        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(string);
            outputStream = new FileOutputStream(string2);
            Reduce.reduce(fileInputStream, outputStream);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void reduce(InputStream inputStream, OutputStream outputStream) throws IOException {
        List list = IpUtils.readCSV(inputStream);
        List list2 = Reduce.reduce(list);
        IpUtils.writeCSV(list2, outputStream);
    }

    public static List reduce(List list) throws IOException {
        LineInfo lineInfo;
        LineInfo lineInfo2;
        int n;
        Collections.sort(list, new Comparator() {

            public int compare(Object object, Object object2) {
                LineInfo lineInfo = (LineInfo) object;
                LineInfo lineInfo2 = (LineInfo) object2;
                return lineInfo2.getStartIP() - lineInfo.getStartIP() > 0 ? -1 : (lineInfo2.getStartIP() - lineInfo.getStartIP() == 0 ? 0 : 1);
            }
        });
        for (n = 0; n < list.size(); ++n) {
            lineInfo2 = n > 0 ? (LineInfo) list.get(n - 1) : null;
            lineInfo = (LineInfo) list.get(n);
            if (lineInfo.getEndIP() < lineInfo.getStartIP()) {
                throw new IllegalArgumentException("start > end at line " + lineInfo);
            }
            if (lineInfo2 == null || lineInfo.getStartIP() >= lineInfo2.getEndIP()) continue;
            System.out.println("Conflict between lines: \n" + lineInfo2 + " : " + lineInfo);
            if (lineInfo.getEndIP() <= lineInfo2.getEndIP()) {
                System.out.println("Dropping inner range : " + lineInfo);
                list.remove(n);
                continue;
            }
            if (lineInfo.getEndIP() <= lineInfo2.getEndIP()) continue;
            String string = "Conflict graphType 2, can't resolve";
            throw new IllegalArgumentException(string);
        }
        for (n = 0; n < list.size(); ++n) {
            lineInfo2 = n > 0 ? (LineInfo) list.get(n - 1) : null;
            lineInfo = (LineInfo) list.get(n);
            if (lineInfo == null) {
                throw new IllegalStateException();
            }
            if (lineInfo2 == null) continue;
            long l = lineInfo2.getEndIP();
            long l2 = lineInfo.getStartIP();
            if (!lineInfo2.getId2c().equals(lineInfo.getId2c()) || l != l2 && l + 1 != l2) continue;
            lineInfo2.setEndIP(lineInfo.getEndIP());
            list.remove(n);
            --n;
        }
        return list;
    }
}
