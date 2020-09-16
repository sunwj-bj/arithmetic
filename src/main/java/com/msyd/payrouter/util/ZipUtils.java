
package com.msyd.payrouter.util;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class ZipUtils {

    /**
     *
     * <p>
     * Description:使用gzip进行解压缩
     * </p>
     * 
     * @param compressedStr
     * @return
     * @throws Exception 
     */
    public static String gunzip(String compressedStr) throws Exception {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = decode(compressedStr.getBytes());
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            System.out.print("数据解压异常" + e);
            throw new Exception(e);
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

    /**
     * Base64解密
     * <p> Create Date: 2014年9月17日 </p>
     * @return
     */
    public static byte[] decode(byte[] str) {
        byte[] result = null;
        if (str != null) {
            result = Base64.decodeBase64(str);

        }
        return result;
    }
}
