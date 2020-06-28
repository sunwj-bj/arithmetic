package com.msyd.base;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.awt.*;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class StringUtil {
    private static Logger LOG = Logger.getLogger(StringUtil.class);
    private static Pattern NumPattern = Pattern.compile("[0-9]*");

    public static boolean isNumeric(String str) {
        return NumPattern.matcher(str).matches();
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String getSexFromId(String tIdNo) {
        try {
            if (null == tIdNo) {
                return "";
            }
            if (tIdNo.length() != 15 && tIdNo.length() != 18) {
                return "";
            }
            String sex = "";
            if (tIdNo.length() == 15) {
                sex = tIdNo.substring(14, 15);
            } else {
                sex = tIdNo.substring(16, 17);
            }
            try {
                int iSex = Integer.parseInt(sex);
                iSex %= 2;
                if (iSex == 0) {
                    return "女";
                }
                if (iSex == 1) {
                    return "男";
                }
            } catch (Exception ex) {
                return "";
            }
        } catch (Exception e) {
            LOG.error("获取男女失败", e);
            return "";
        }
        return "";
    }

    // MD5加密
    public static String md5(String source) {
        if (source == null) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("A MD5 exception is occur.");
            return null;
        }
    }

    public static String replaceSpecialtyStr(String str, String replace) {
        String pattern = "\\s*|\t|\r|\n";// 去除字符串中空格、换行、制表
        if (isEmpty(replace))
            replace = "";
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }

    /**
     * json字符串转换成java对象
     * @param json
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, Class<T> c) {
        T o = null;
        try {
            o = new ObjectMapper().readValue(json, c);
        } catch (Exception e) {
            LOG.error(e);
        }
        return o;
    }


    /**
     * java对象转字符串
     * @param o
     * @return
     */
    public static String object2JSON(Object o) {
        ObjectMapper om = new ObjectMapper();
        Writer w = new StringWriter();
        String json = null;
        try {
            om.writeValue(w, o);
            json = w.toString();
            w.close();
        } catch (Exception e) {
            LOG.error(e);
        }
        return json;
    }


    /**
     * 返回byte的数据大小对应的文本
     * 
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }

    // 验证码相关
    // 设置图片的宽度
    public static int WIDTH = 65;

    // 设置图片的高度
    public static int HEIGHT = 22;

    public static void drawBackground(Graphics g) {
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < 120; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            g.setColor(new Color(red, green, blue));
            g.drawOval(x, y, 1, 0);
        }
    }

    public static void drawRands(Graphics g, char[] rands) {
        Random random = new Random();
        int red = random.nextInt(110);
        int green = random.nextInt(50);
        int blue = random.nextInt(50);
        g.setColor(new Color(red, green, blue));
        g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18));
        g.drawString("" + rands[0], 1, 17);
        g.drawString("" + rands[1], 16, 15);
        g.drawString("" + rands[2], 31, 18);
        g.drawString("" + rands[3], 46, 16);
    }

    public static char[] generateCheckCode() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[4];
        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * 36);
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

    public static String moneyFormat(Double money) {
        NumberFormat nf = new DecimalFormat("#,##0.00#元");
        String moneyStr = nf.format(money);
        return moneyStr;
    }

    public static String moneyFormat3(BigDecimal money) {
        NumberFormat nf = new DecimalFormat("#,##0.00#");
        String moneyStr = nf.format(money);
        return moneyStr;
    }

    public static String numFormat(long l) {
        NumberFormat nf = new DecimalFormat("#,###");
        String moneyStr = nf.format(l);
        return moneyStr;
    }

    /**
     * 去掉样式字段
     * 
     * @return
     */
    public static String removeStyleChar(String styleStr) {
        if (styleStr == null)
            return null;
        char[] cs = styleStr.toCharArray();
        StringBuilder sb = new StringBuilder("");
        boolean b = true;
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == '<') {
                b = false;
                continue;
            }
            if (cs[i] == '>') {
                b = true;
                continue;
            }
            if (b) {
                sb.append(cs[i]);
            }
        }
        int len = sb.length();

        sb = new StringBuilder(sb.toString().replaceAll("马上摇奖。", ""));
        int index = sb.indexOf("民生易贷团队");
        if (index > 0) {
            return sb.substring(0, index);
        } else {
            return sb.substring(0, len);
        }

    }

    /**
     * 金额格式化
     * 
     * @param s
     *            金额
     * @param len
     *            小数位数
     * @return 格式后的金额
     */
    public static String formatMoney(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,##0.00");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.00");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    /**
     * 去除 地区 名称 所带的'省'、'自治区'、'市' 后缀
     * 
     * @param areaName
     * @return
     */
    public static String parseAreaName(String areaName) {
        if (isEmpty(areaName)) {
            return areaName;
        }
        int index = areaName.indexOf("省");
        if (index < 0) {
            index = areaName.indexOf("自治区");
        }
        if (index < 0) {
            index = areaName.indexOf("市");
        }
        if (index < 0) {
            return areaName;
        }
        return areaName.substring(0, index);
    }


    /**
     * 校验银行卡卡号
     * 
     * @param bankCardNo
     * @return
     */
    public static boolean checkBankCardNo(String bankCardNo) {
        if (bankCardNo == null || bankCardNo.length() == 0 || !bankCardNo.matches("\\d+")) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCardNo.substring(0, bankCardNo.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCardNo.charAt(bankCardNo.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    private static Pattern pattern1 = Pattern.compile("[0-9]");

    private static Pattern pattern2 = Pattern.compile("[a-z]");

    private static Pattern pattern3 = Pattern.compile("[A-Z]");

    private static Pattern pattern4 = Pattern.compile("[`~!@#$^&*()_=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");

    private static Pattern pattern5 = Pattern
            .compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

    /**
     * 检测 用户密码强度
     * 
     * @param pwd
     * @return
     */
    public static boolean checkUserPwd(String pwd) {
        if (pwd == null || pwd.indexOf(" ") != -1) {
            return false;
        }
        int levelNum = 0;
        if (pattern1.matcher(pwd).find()) {
            levelNum++;
        }
        if (pattern2.matcher(pwd).find()) {
            levelNum++;
        }
        if (pattern3.matcher(pwd).find()) {
            levelNum++;
        }
        if (pattern4.matcher(pwd).find()) {
            levelNum++;
        }
        if (levelNum < 2) {
            return false;
        }
        return true;
    }

    /**
     * 获取客户端版本号
     * 
     * @param version
     * @return 返回0表示获取失败
     */
    public static int getClientVisionCode(String version) {
        if (version == null) {
            return 0;
        }
        String versionCode = version.toUpperCase().replaceAll("V", "");
        versionCode = versionCode.replaceAll("\\.", "");
        try {
            return Integer.parseInt(versionCode);
        } catch (Exception e) {
            LOG.error("getClientVisionCode is error", e);
        }
        return 0;
    }

    /**
     * 手机号星号显示
     * 
     * @param mobile
     * @return 返回0表示获取失败
     */
    public static String getMobileMask(String mobile) {
        if (mobile == null || "".equals(mobile) || mobile.length() != 11) {
            return mobile;
        }
        String res = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
        return res;
    }

    /**
     * 身份证号生日部分*号展示
     * 
     * @param idCard
     * @return
     */
    public static String getIdCard(String idCard) {
        String res = null;
        if (idCard == null || "".equals(idCard)) {
            return idCard;
        } else {
            if (idCard.length() == 15) {
                res = idCard.substring(0, 8) + "****" + idCard.substring(12, 15);
            } else if (idCard.length() == 18) {
                res = idCard.substring(0, 10) + "****" + idCard.substring(14, 18);
            }
        }
        return res;
    }

    /**
     * 得到几天后的时间
     * 
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到几个月后的时间
     * 
     * @param d
     * @param month
     * @return
     */
    public static Date getDateAfterMonth(Date d, int month) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + month);
        return now.getTime();
    }

    public static Date stringToDate(String str, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            LOG.error("stringToDate exception is occur.", e);
        }
        return date;
    }

    public static String dateToStr(Date dateDate, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 比较两个日期之间的大小
     * 
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result >= 0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 日期加减
     * 
     * @param date：基准日期
     * @param num：正数代表加，负数代表减
     * @param field：参考Calendar的值
     * @return
     */
    public static Date addDate(Date date, int num, int field) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, num);
        return c.getTime();
    }

    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int compare_date(String date1, String date2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            LOG.error("时间大小比较出现异常", e);
        }
        return -2;
    }

    /**
     * 身份证号生日部分*号展示
     * 
     * @param idCard
     * @return
     */
    public static String getIdCardMadk(String idCard) {
        String res = null;
        if (idCard == null || "".equals(idCard)) {
            return idCard;
        } else {
            if (idCard.length() == 15) {
                res = idCard.substring(0, 3) + "*****" + idCard.substring(12, 15);
            } else if (idCard.length() == 18) {
                res = idCard.substring(0, 3) + "********" + idCard.substring(14, 18);
            }
        }
        return res;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean sameDate(Date d1, Date d2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(d1).equals(fmt.format(d2));
    }


    private static String getStar(String con, int len) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < con.length() - len; i++) {
            buf.append("*");
        }
        return buf.toString();
    }

    /**
     * checkMailBox(邮箱校验)
     * 
     * @param email
     * @return String DOM对象
     * @Exception 异常对象
     * @since CodingExample Ver(编码范例查看) 1.1
     */
    public static boolean checkMailBox(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return pattern5.matcher(email).matches();
    }

}
