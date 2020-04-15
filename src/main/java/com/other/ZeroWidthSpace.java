package com.other;

public class ZeroWidthSpace {
    //中文转unicode编码
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }
    //unicode编码转中文
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            // 16进制parse整形字符串。
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
    //汉字转换成二进制字符串
    public static String strToBinStr(String str) {
        char[] chars=str.toCharArray();
        StringBuffer result = new StringBuffer();
        for(int i=0; i<chars.length; i++) {
            result.append(Integer.toBinaryString(chars[i]));
            result.append(" ");
        }
        return result.toString();
    }
    //二进制字符串转换成汉字
    public static String BinStrTostr(String binary) {
        String[] tempStr=binary.split(" ");
        char[] tempChar=new char[tempStr.length];
        for(int i=0;i<tempStr.length;i++) {
            tempChar[i]=BinstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }
    //将二进制字符串转换成int数组
    public static int[] BinstrToIntArray(String binStr) {
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
    //将二进制转换成字符
    public static char BinstrToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }
        return (char)sum;
    }
    //零宽字加密
    public static String ZeroWidthWrdEncryption(String str){
        char[] chars=gbEncoding(str).toCharArray();
        StringBuffer result = new StringBuffer();
        StringBuffer jtext = new StringBuffer("\\uFEFF");
        for(int i=0; i<chars.length; i++) {
            if(Integer.toBinaryString(chars[i]).length()==7){
                result.append("0");
                result.append(Integer.toBinaryString(chars[i]));
            }else {
                result.append("00");
                result.append(Integer.toBinaryString(chars[i]));
            }
        }
        for(int i=0; i<result.toString().length(); i++) {
            if(i%2==0){
                switch(result.toString().substring(i,i+2)){
                    case "00" :
                        jtext.append("\\u200a");
                        break;
                    case "01" :
                        jtext.append("\\u200b");
                        break;
                    case "10" :
                        jtext.append("\\u200c");
                        break;
                    case "11" :
                        jtext.append("\\u200d");
                        break;
                }
            }
        }
        jtext.append("\\uFEFF");
        return decodeUnicode(jtext.toString());
    }
    //零宽字解密
    public static String ZeroWidthWordDecryption(String txt){
        String text = gbEncoding(txt);
        String str = text.substring(text.indexOf("\\ufeff")+6, text.lastIndexOf("\\ufeff"));
        str = str.replace("\\u200a","00");
        str = str.replace("\\u200b","01");
        str = str.replace("\\u200c","10");
        str = str.replace("\\u200d","11");
        char[] tempChar=new char[str.length()/8];
        for(int i=0;i<str.length();i++) {
            if(i%8==0){
                tempChar[i/8]=BinstrToChar(str.substring(i,i+8));
            }
        }
        return decodeUnicode(String.valueOf(tempChar));
    }

    public static void main(String[] args) {
        String cat = ZeroWidthWrdEncryption("猫");
        System.out.println("加密后:"+cat);
        System.out.println("加密后长度:"+cat.length());
        System.out.println("解密出:"+ZeroWidthWordDecryption(cat));
        System.out.println("解密后长度:"+ZeroWidthWordDecryption(cat).length());
    }
}
