package com.msyd.base;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient implements java.io.Serializable {

    private static final int connectionTimeout = 8000;

    private static final int readTimeout = 8000;

    public static Response httpPostRequest(String url, String jsonParams) {
        Response res = null;
        try {
            HttpURLConnection con = getConnection(url);

            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("user-agent", "android");
            con.setDoOutput(true);

            OutputStream osw = con.getOutputStream();
            if (null != jsonParams) {
                osw.write(jsonParams.getBytes());
            }
            osw.flush();
            osw.close();

            res = new Response(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        //con.setConnectTimeout(connectionTimeout);
        //con.setReadTimeout(readTimeout);
        return con;
    }

    public static String doGet(String getURL) throws IOException {
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer result = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            result.append(lines);
        }
        reader.close();
        connection.disconnect();

        return result.toString();
    }

}
