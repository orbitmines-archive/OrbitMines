package net.firefang.ip2c;

import com.orbitmines.bungeecord.OrbitMinesBungee;
import net.firefang.ip2c.input.MemoryMappedRandoeAccessFile;
import net.firefang.ip2c.input.RandomAccessBuffer;
import net.firefang.ip2c.input.RandomAccessFile2;
import net.firefang.ip2c.input.RandomAccessInput;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class IP2Country {
    public static final int WEBHOSTING_INFO_CSV_FORMAT = 0;
    public static final int SOFTWARE77_CSV_FORMAT = 1;
    public static final int NO_CACHE = 0;
    public static final int MEMORY_MAPPED = 1;
    public static final int MEMORY_CACHE = 2;
    private RandomAccessInput m_input;
    private int m_firstTableOffset;
    private int m_secondTableOffset;
    private int m_countriesOffset;
    private int m_numRangesFirstTable;
    private int m_numRangesSecondTable;
    private int m_numCountries;

    public IP2Country() throws IOException {
        this(0);
    }

    public IP2Country(int n) throws IOException {
        this(OrbitMinesBungee.getBungee().getResourceFolder() + "/ip/ip-to-country.bin", n);
    }

    public IP2Country(String string, int n) throws IOException {
        switch (n) {
            case 0: {
                this.m_input = new RandomAccessFile2(string, "r");
                break;
            }
            case 1: {
                this.m_input = new MemoryMappedRandoeAccessFile(string, "r");
                break;
            }
            case 2: {
                this.m_input = new RandomAccessBuffer(string);
            }
        }
        byte[] arrby = new byte[4];
        this.readFully(arrby);
        if (arrby[0] != 105 || arrby[1] != 112 || arrby[2] != 50 || arrby[3] != 99) {
            throw new IOException("Invalid file signature");
        }
        if (this.readInt() != 2) {
            throw new IOException("Invalid format version");
        }
        this.m_firstTableOffset = this.readInt();
        this.m_numRangesFirstTable = this.readInt();
        this.m_secondTableOffset = this.readInt();
        this.m_numRangesSecondTable = this.readInt();
        this.m_countriesOffset = this.readInt();
        this.m_numCountries = this.readInt();
    }

    public Country getCountry(String string) throws IOException {
        return this.getCountry(IP2Country.parseIP(string));
    }

    private static int parseIP(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, ".");
        short s = Short.parseShort(stringTokenizer.nextToken());
        short s2 = Short.parseShort(stringTokenizer.nextToken());
        short s3 = Short.parseShort(stringTokenizer.nextToken());
        short s4 = Short.parseShort(stringTokenizer.nextToken());
        int n = s << 24 & -16777216 | s2 << 16 & 16711680 | s3 << 8 & 65280 | s4 & 255;
        return n;
    }

    public Country getCountry(int n) throws IOException {
        short s;
        if (n >= 0) {
            s = this.findCountyCode(n, 0, this.m_numRangesFirstTable, true);
        } else {
            int n2 = n - Integer.MAX_VALUE;
            s = this.findCountyCode(n2, 0, this.m_numRangesSecondTable, false);
        }
        if (s == 0) {
            return null;
        }
        return this.findCountry(s, 0, this.m_numCountries);
    }

    private short findCountyCode(long l, int n, int n2, boolean bl) throws IOException {
        int n3 = (n + n2) / 2;
        Pair pair = this.getPair(n3, bl);
        long l2 = pair.m_ip;
        if (l < l2) {
            if (n + 1 == n2) {
                return 0;
            }
            return this.findCountyCode(l, n, n3, bl);
        }
        if (l > l2) {
            Pair pair2 = this.getPair(n3 + 1, bl);
            if (l < (long) pair2.m_ip) {
                return pair.m_key;
            }
            if (n + 1 == n2) {
                return 0;
            }
            return this.findCountyCode(l, n3, n2, bl);
        }
        return pair.m_key;
    }

    public Country findCountry(short s) throws IOException {
        return this.findCountry(s, 0, this.m_numCountries);
    }

    public Country findCountry(short s, int n, int n2) throws IOException {
        int n3 = (n + n2) / 2;
        short s2 = this.getCountryCode(n3);
        if (s2 == s) {
            return this.loadCountry(n3);
        }
        if (s > s2) {
            return this.findCountry(s, n3, n2);
        }
        return this.findCountry(s, n, n3);
    }

    private Country loadCountry(int n) throws IOException {
        int n2 = this.m_countriesOffset + n * 10;
        this.seek(n2);
        short s = this.readShort();
        int n3 = this.readInt();
        int n4 = this.readInt();
        this.seek(n4);
        short s2 = this.readShort();
        byte[] arrby = new byte[s2];
        this.readFully(arrby);
        return new Country(s, n3, new String(arrby));
    }

    private short getCountryCode(int n) throws IOException {
        int n2 = this.m_countriesOffset + n * 10;
        this.seek(n2);
        return this.readShort();
    }

    private Pair getPair(int n, boolean bl) throws IOException {
        int n2;
        if (bl) {
            if (n > this.m_numRangesFirstTable) {
                return new Pair();
            }
            n2 = this.m_firstTableOffset + n * 6;
        } else {
            if (n > this.m_numRangesSecondTable) {
                return new Pair();
            }
            n2 = this.m_secondTableOffset + n * 6;
        }
        this.seek(n2);
        Pair pair = new Pair();
        byte[] arrby = new byte[4];
        this.readFully(arrby);
        pair.m_ip = IP2Country.toInt(arrby);
        pair.m_key = this.readShort();
        return pair;
    }

    public static List convertCSVtoBIN(String string, String string2) throws IOException {
        List list = IpUtils.readCSV(string);
        int n = list.size();
        IpUtils.removeOverlappings(list);
        list = Reduce.reduce(list);
        if (n != list.size()) {
            System.out.println("Reduced " + (n - list.size()) + " ranges by consolidating adjacent ranges");
        }
        list = IP2Country.normalizeList(list);
        IP2Country.convertCSVtoBIN(list, string2);
        return list;
    }

    public static void convertCSVtoBIN(List list, String string) throws IOException {
        Object object;
        Object object2;
        int n;
        long l;
        int n2;
        int n3;
        Object object3;
        Object object4;
        TreeMap<Object, Country> treeMap = new TreeMap<Object, Country>(new Comparator() {

            public int compare(Object object, Object object2) {
                return ((String) object).compareTo((String) object2);
            }
        });
        RandomAccessFile randomAccessFile = new RandomAccessFile(string, "rw");
        randomAccessFile.setLength(0);
        long l2 = -1;
        randomAccessFile.write("ip2c".getBytes());
        randomAccessFile.writeInt(2);
        int n4 = (int) randomAccessFile.getFilePointer();
        randomAccessFile.writeInt(-858993460);
        randomAccessFile.writeInt(-1145324613);
        randomAccessFile.writeInt(-858993460);
        randomAccessFile.writeInt(-1145324613);
        randomAccessFile.writeInt(-858993460);
        randomAccessFile.writeInt(-1145324613);
        int n5 = (int) randomAccessFile.getFilePointer();
        randomAccessFile.seek(n4);
        randomAccessFile.writeInt(n5);
        randomAccessFile.seek(n5);
        boolean bl = true;
        int n6 = 0;
        long l3 = -1;
        LineInfo lineInfo = null;
        for (n2 = 0; n2 < list.size(); ++n2) {
            try {
                lineInfo = (LineInfo) list.get(n2);
                if (lineInfo.getStartIP() > lineInfo.getEndIP()) {
                    throw new IllegalArgumentException("start > end");
                }
                if (lineInfo.getStartIP() < l2) {
                    throw new IllegalArgumentException("File not sorted, start of range " + lineInfo.getStartIP() + " and end of previous range " + l2 + " " + lineInfo);
                }
                object3 = lineInfo.getId2c();
                object4 = lineInfo.getId3c();
                object = lineInfo.getName();
                object2 = null;
                if (object3 != null) {
                    object2 = new Country((String) object3, (String) object4, (String) object);
                    if (!treeMap.containsKey(object3)) {
                        treeMap.put(object3, (Country) object2);
                    }
                }
                if (bl && lineInfo.getStartIP() > Integer.MAX_VALUE) {
                    bl = false;
                    ++n6;
                    l2 = -1;
                    l3 = -1;
                    long l4 = randomAccessFile.getFilePointer();
                    if (l4 > Integer.MAX_VALUE) {
                        throw new RuntimeException("Invalid offset");
                    }
                    n5 = (int) randomAccessFile.getFilePointer();
                    randomAccessFile.seek(n4 + 4);
                    randomAccessFile.writeInt(n6);
                    randomAccessFile.writeInt(n5);
                    randomAccessFile.seek(n5);
                    n6 = 0;
                }
                if (bl) {
                    n3 = IP2Country.toInt(IP2Country.toBytes(lineInfo.getStartIP()));
                    n = IP2Country.toInt(IP2Country.toBytes(lineInfo.getEndIP()));
                } else {
                    l = lineInfo.getStartIP() - Integer.MAX_VALUE;
                    n3 = IP2Country.toInt(IP2Country.toBytes(l));
                    if (lineInfo.getEndIP() > 0xFFFFFFFEL) {
                        lineInfo.setEndIP(0xFFFFFFFEL);
                    }
                    n = IP2Country.toInt(IP2Country.toBytes(lineInfo.getEndIP() - Integer.MAX_VALUE));
                }
                l2 = lineInfo.getEndIP();
                l3 = n;
                randomAccessFile.write(IP2Country.toBytes(n3));
                randomAccessFile.writeShort(object2 != null ? ((Country) object2).get2c() : 0);
                ++n6;
                continue;
            } catch (RuntimeException var14_13) {
                System.err.println("Error parsing : " + lineInfo.getLineNum());
                throw var14_13;
            }
        }
        randomAccessFile.write(IP2Country.toBytes(l3));
        randomAccessFile.writeShort(0);
        n5 = (int) randomAccessFile.getFilePointer();
        randomAccessFile.seek(n4 + 12);
        randomAccessFile.writeInt(n6);
        randomAccessFile.writeInt(n5);
        randomAccessFile.writeInt(treeMap.size());
        randomAccessFile.seek(n5);
        n2 = n5;
        object3 = treeMap.keySet();
        object4 = ((Iterator) object3);
        while (((Iterator) object4).hasNext()) {
            object = (String) ((Iterator) object4).next();
            object2 = (Country) treeMap.get(object);
            n3 = ((Country) object2).get2c();
            n = ((Country) object2).get3c();
            randomAccessFile.writeShort(n3);
            randomAccessFile.writeInt(n);
            randomAccessFile.writeInt(-858993460);
        }
        int n7 = 0;
        object = ((Iterator) object3);
        while (((Iterator) object).hasNext()) {
            object2 = (String) ((Iterator) object).next();
            Country country = (Country) treeMap.get(object2);
            String string2 = country.getName();
            l = randomAccessFile.getFilePointer();
            if (l > Integer.MAX_VALUE) {
                throw new RuntimeException("Invalid offset");
            }
            randomAccessFile.seek(n2 + n7 * 10 + 6);
            randomAccessFile.writeInt((int) l);
            randomAccessFile.seek(l);
            randomAccessFile.writeShort(string2.length());
            randomAccessFile.writeBytes(string2);
            ++n7;
        }
        randomAccessFile.close();
    }

    private static List normalizeList(List list) {
        Vector<LineInfo> vector = new Vector<LineInfo>();
        long l = -1;
        boolean bl = true;
        for (int i = 0; i < list.size(); ++i) {
            LineInfo lineInfo = (LineInfo) list.get(i);
            if (l + 1 < lineInfo.getStartIP()) {
                long l2 = list.size() > i ? ((LineInfo) list.get(i)).getStartIP() : Integer.MIN_VALUE;
                LineInfo lineInfo2 = new LineInfo(l + 1, l2 - 1, null, null, "dummy", -1);
                vector.add(lineInfo2);
            }
            vector.add(lineInfo);
            if (bl) {
                LineInfo lineInfo3;
                if (i == list.size() - 1) {
                    LineInfo lineInfo4 = new LineInfo(lineInfo.getEndIP() + 1, Integer.MAX_VALUE, null, null, "dummy", -1);
                    lineInfo3 = new LineInfo(0x80000000L, 0xFFFFFFFEL, null, null, "dummy", -1);
                    vector.add(lineInfo4);
                    vector.add(lineInfo3);
                    break;
                }
                if (lineInfo.getStartIP() < Integer.MAX_VALUE && lineInfo.getEndIP() > Integer.MAX_VALUE) {
                    LineInfo lineInfo5 = new LineInfo(lineInfo.getStartIP(), 2147483646, lineInfo.getId2c(), lineInfo.getId3c(), lineInfo.getName(), lineInfo.getLineNum());
                    lineInfo3 = new LineInfo(Integer.MAX_VALUE, lineInfo.getEndIP(), lineInfo.getId2c(), lineInfo.getId3c(), lineInfo.getName(), lineInfo.getLineNum());
                    vector.add(lineInfo5);
                    vector.add(lineInfo3);
                    bl = false;
                } else if (lineInfo.getStartIP() > Integer.MAX_VALUE) {
                    bl = false;
                }
            }
            l = lineInfo.getEndIP();
        }
        return vector;
    }

    static int toInt(byte[] arrby) {
        return arrby[0] << 24 & -16777216 | arrby[1] << 16 & 16711680 | arrby[2] << 8 & 65280 | arrby[3] & 255;
    }

    static byte[] toBytes(long l) {
        byte[] arrby = new byte[]{(byte) ((l & 0xFF000000L) >> 24), (byte) ((l & 0xFF0000) >> 16), (byte) ((l & 65280) >> 8), (byte) ((l & 255) >> 0)};
        return arrby;
    }

    public static void main(String[] arrstring) throws IOException {
        String string;
        if (arrstring.length < 2) {
            IP2Country.usage();
        }
        if ((string = arrstring[0]).equals("-r")) {
            IP2Country iP2Country = new IP2Country();
            String string2 = arrstring[1];
            Country country = iP2Country.getCountry(string2);
            if (country == null) {
                System.out.println("UNKNOWN");
            } else {
                System.out.println(country.get2cStr() + "," + country.get3cStr() + "," + country.getName());
            }
        } else if (string.equals("-c")) {
            String string3 = arrstring[1];
            int n = string3.toLowerCase().lastIndexOf(".csv");
            if (n == -1) {
                throw new IllegalArgumentException("Input file name should end with csv");
            }
            String string4 = arrstring.length == 2 ? arrstring[1].substring(0, string3.length() - ".csv".length()) + ".bin" : arrstring[2];
            System.out.println("converting " + string3 + " to " + string4);
            IP2Country.convertCSVtoBIN(string3, string4);
        } else {
            IP2Country.usage();
        }
    }

    private static void usage() {
        System.err.println("Usage : ");
        System.err.println("java -jar ip2c.jar -r|-c ...");
        System.err.println();
        System.err.println("\t-r : Resolve an IP address");
        System.err.println("\t-c : Build binary file from CSV");
        System.err.println();
        System.err.println();
        System.err.println("-= Resolve an ip address =-");
        System.err.println("java -jar ip2c.jar -r ip-address");
        System.err.println("Output format:");
        System.err.println("if not found:");
        System.err.println("UNKNOWN");
        System.err.println();
        System.err.println("if found:");
        System.err.println("2C 3C NAME");
        System.err.println();
        System.err.println("Example:");
        System.err.println("java -jar ip2c.jar -r 85.64.225.159");
        System.err.println("Outputs:");
        System.err.println("IL ISR ISRAEL");
        System.err.println();
        System.err.println();
        System.err.println("-= Build binary file from CSV =-");
        System.err.println("java -jar ip2c.jar -c csv_file [bin_file]");
        System.err.println("if bin file is not specified, the name of the csv will be used.");
        System.err.println();
        System.err.println("supported CSV formats:");
        System.err.println("webhosting.info : http://ip-to-country.webhosting.info/");
        System.err.println("software77 : http://software77.net/cgi-bin/ip-country/geo-ip.pl");
        System.exit(1);
    }

    private int readInt() throws IOException {
        return this.m_input.readInt();
    }

    private short readShort() throws IOException {
        return this.m_input.readShort();
    }

    private void readFully(byte[] arrby) throws IOException {
        this.m_input.readFully(arrby);
    }

    private void seek(int n) throws IOException {
        this.m_input.seek(n);
    }

    static class Pair {
        int m_ip;
        short m_key;

        Pair() {
        }
    }
}
