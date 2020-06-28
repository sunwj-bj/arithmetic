package com.msyd.base;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

/**
 * A data class representing HTTP Response
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Response {

    private int statusCode;

    private String responseAsString = null;

    private InputStream is;

    private HttpURLConnection con;

    private boolean streamConsumed = false;

    public Response() {

    }

    Response(String content) {
        this.responseAsString = content;
    }

    public Response(HttpURLConnection con) throws IOException {
        this.con = con;
        this.statusCode = con.getResponseCode();
        if (null == (is = con.getErrorStream())) {
            is = con.getInputStream();
        }
        if (null != is && "gzip".equals(con.getContentEncoding())) {
            is = new GZIPInputStream(is);
        }
    }


    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseHeader(String name) {
        if (con != null) {
            return con.getHeaderField(name);
        }else {
            return null;
        }
    }

    /**
     * Returns the response stream.<br>
     * This method cannot be called after calling asString() or asDcoument()<br>
     * It is suggested to call disconnect() after consuming the stream.
     *
     * Disconnects the internal HttpURLConnection silently.
     * @return response body stream
     */
    public InputStream asStream() {
        if (streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    /**
     * Returns the response body as string.<br>
     * Disconnects the internal HttpURLConnection silently.
     * @return response body
     */
    public String asString() {
        if (null == responseAsString) {
            BufferedReader br;
            try {
                InputStream stream = asStream();
                if (null == stream) {
                    return null;
                }
                br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuffer buf = new StringBuffer();
                String line;
                while (null != (line = br.readLine())) {
                    buf.append(line).append("\n");
                }
                this.responseAsString = buf.toString();
                stream.close();
                con.disconnect();
                streamConsumed = true;
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
        return responseAsString;
    }

    public String getResponseAsString() {
        return responseAsString;
    }

    public void setResponseAsString(String responseAsString) {
        this.responseAsString = responseAsString;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
