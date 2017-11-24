package net.firefang.ip2c;

public class Country {
    private short m_2c;
    private int m_3c;
    private String m_name;

    public Country(short s, int n, String string) {
        this.m_2c = s;
        this.m_3c = n;
        this.m_name = string;
    }

    public Country(String string, String string2, String string3) {
        if (string.length() != 2) {
            throw new IllegalArgumentException("Invalid id2c : '" + string + "'");
        }
        if (string2.length() > 3) {
            throw new IllegalArgumentException("Invalid id3c : '" + string2 + "'");
        }
        this.m_2c = (short) (string.charAt(0) << 8 | string.charAt(1));
        if (string2.length() == 0) {
            string2 = "   ";
        }
        if (string2.length() == 3) {
            this.m_3c = string2.charAt(0) << 16 | string2.charAt(1) << 8 | string2.charAt(2);
        } else if (string2.length() == 2) {
            this.m_3c = string2.charAt(0) << 8 | string2.charAt(1) << 0;
        }
        this.m_name = string3;
    }

    public short get2c() {
        return this.m_2c;
    }

    public int get3c() {
        return this.m_3c;
    }

    public String getName() {
        return this.m_name;
    }

    public String toString() {
        String string = Country.get2cStr(this.m_2c);
        String string2 = Country.get3cStr(this.m_3c);
        return "id2=" + string + ", id3c=" + string2 + ", name=" + this.m_name;
    }

    public String get3cStr() {
        return Country.get3cStr(this.m_3c);
    }

    public String get2cStr() {
        return Country.get2cStr(this.m_2c);
    }

    public static String get3cStr(int n) {
        String string = new String(new byte[]{(byte) (n >> 16), (byte) (n >> 8), (byte) n});
        if (string.equals("   ")) {
            return "";
        }
        return string;
    }

    public static String get2cStr(short s) {
        return new String(new byte[]{(byte) (s >> 8), (byte) (s & 255)});
    }

    public boolean equals(Object object) {
        if (object instanceof Country) {
            Country country = (Country) object;
            return country.m_2c == this.m_2c && country.m_3c == country.m_3c && country.m_name.equals(this.m_name);
        }
        return false;
    }
}
