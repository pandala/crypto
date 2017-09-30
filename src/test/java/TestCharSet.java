import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class TestCharSet {

  public static String bytes2HexString(byte[] b) {
    String ret = "";
    for (int i = 0; i < b.length; i++) {
      String hex = Integer.toHexString(b[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      ret += hex;
    }
    return ret;
  }

  @Test
  public void testCharSet() throws UnsupportedEncodingException {
    String str = "当";
    byte[] chs = str.getBytes();

    String hexStr = bytes2HexString(chs);
    System.out.println(hexStr);

    byte[] chs2 = str.getBytes("gb2312");
    String hexStr2 = bytes2HexString(chs2);
    System.out.println(hexStr2);
  }
}
