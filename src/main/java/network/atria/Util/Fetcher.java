package network.atria.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class Fetcher {

  private Fetcher() {
    throw new UnsupportedOperationException();
  }

  public static UUID getUUID(String playername) {
    String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/" + playername;
    String output = callURL(UUID_URL);
    StringBuilder result = new StringBuilder();
    readData(output, result);
    String u = result.toString();
    StringBuilder uuid = new StringBuilder();
    for (int i = 0; i <= 31; i++) {
      uuid.append(u.charAt(i));
      if (i == 7 || i == 11 || i == 15 || i == 19) {
        uuid.append('-');
      }
    }
    return UUID.fromString(uuid.toString());
  }

  public static String getName(UUID uuid) {
    String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
    try {
      String nameJson = IOUtils.toString(new URL(url));
      JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
      String playerSlot = nameValue.get(nameValue.size() - 1).toString();
      JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
      return nameObject.get("name").toString();
    } catch (IOException | ParseException e) {
      return "error";
    }
  }

  private static void readData(String toRead, StringBuilder result) {
    for (int i = toRead.length() - 3; i >= 0; i--) {
      if (toRead.charAt(i) != '"') {
        result.insert(0, toRead.charAt(i));
      } else {
        break;
      }
    }
  }

  private static String callURL(String urlStr) {
    StringBuilder sb = new StringBuilder();
    URLConnection urlConn;
    InputStreamReader in;
    try {
      URL url = new URL(urlStr);
      urlConn = url.openConnection();
      if (urlConn != null) {
        urlConn.setReadTimeout(60 * 1000);
      }
      if (urlConn != null && urlConn.getInputStream() != null) {
        in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
        BufferedReader bufferedReader = new BufferedReader(in);
        int cp;
        while ((cp = bufferedReader.read()) != -1) {
          sb.append((char) cp);
        }
        bufferedReader.close();
        in.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
}
