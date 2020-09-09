package com.msyd.payrouter;

import com.other.OrderGenerator;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 易宝支付-分账
 * @author sunwj
 */
public class YiBaoPayWithClear {
    public static String secretKey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrS0KstpuJs75jrO2Tc7x5WfeQ8ER1tPr34OPiJfETinvTQeNzZc13V1Wm+yhk5Xsv4OWuSHCok5MwhEgc4cb03loTcS4pqAHL4u13hEycf+fTkoIQ88IpmFMcnu0l3dYqzj34fEgH7EN4byCDDd9cNtZWuzA9iitwJuoCjx4sJqxvftxPQV6gbPZEwIWPw3i1ALG8LgKDRiB/i8hw4PfRCGrYtwjOjwKsiEetxNUpROs8nUSpRBQnr8G1Z85sN6wH83eH3y2uJ4O4G/Eao3FG8rHVWPIiQaXtyFqFx8B3pFAwqYuxWaciInlNPTLMd1Ee+BDqQ9FN/tzy/q5CSTevAgMBAAECggEBAJiAYbT4wpMfNrLxI3ayhKsZgQJGFv0gioujad9OXkpCcamMsJ5tlTbZx0TpuHXTpQ/kTzgSAFLlSBbavoUQMZySVWmXyzyE+kx2FWrhm399lHzVo/zJuCRmHCCQEZwz21ey1JNkupBrNUqEzVJASIqFu9/tua4gVDn+OzraBkfREWV9E6CmQ0xl7SNz9awWpg/8zTqLMsXSR3Q6+ezd5vHZTZvkG5sLh2cKgylZoZawJgrGpW5mt+nXlPhvXmW6XcX730DWNvTgIOtduDZ0Ig5JEya8V9XC+1OcHAkpXXMG2ohABZtIKKS4yLWSwgCFErgVyien5T2kdEIGPbnE55ECgYEA88r2DHhssH9fSIzHo6yL3VMit30Qs6fjxmQXcqxewYtk11yPpn2ROaGBYitzsv/kgVLECRsfIREGsUjPqz7Al8NpgqoQEf27zWaf6x59cs3g2vB6rtilvng8C3dnSFjKZQ/tbXv/QHDz/A549rwm4fO689ptjegS3zcAaptF7okCgYEAs976k3NCpln7FzGKtMrMk5my/ed8Dj70cntxdwNBcsOhbbh1HbCsn2F/PLdqd6bJc+Qdi7hIGecUZ/6/CuEDY/8jZqm9b6B4vnxZFBTUyqHcL8cXmMUDi9p7SoSsCZ92RCD0x4SbOjhym7yuvw14gk+hlBSvWuC+YhEMRlinJncCgYBJ+12Pizvwk7amnZI36TTIhWITrLBU1K4almVHN2fJ9DM157DwJUrc4lYRJH6H43/EfwleegyITFJrmlzq6rAnXfW24UTfMNC9FFeTUj1fiXqi9jdEuBoUIwiVsjZ1jfxdjufOQcLEG4LvCrVKqu5hw0UIm1CDr9mKQ3as41HlgQKBgCjBD+NSzTolzxdtOTFHddzHiV+wEFKl/vrlb0r46N5Y5v2WOqr0edhO3eZi5HOhzak9eVhL88IyslPxy1VqsDr69wlu0iY1pMX8JK7BHYmf7OTCZl1N3kTUxvSWZOh1QfWjxfJi4Ezrt0QEF0/gfHqCEmkb2rNrkpdjp3VU5uJ3AoGBANhQ1Jl3X3UOVXQm0ZXQ7OkPkWE5XZIOb+WUcB7JTXKmpAArqI2FOe3Ki+8rMWDNgDUu+/nvSSWzrAsG8+jR9Hp+fSQ6poagwpWcitPjFYQip6RUBx6Cv6mBCMXDGnxOQIi8GJCITT86SsF1YbirDmi/Yd58xPEBbQdjysRWhr8e";
    public static String divideJson="[{\"ledgerno\": \"10000447970\",\"dividemode\": \"AMOUNT\",\"dividevalue\": \"0.01\"}]";
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) {
        //auth();
        authconfirm();
        //collectAndDivide();
    }

    private static void collectAndDivide() {
        YopRequest request = new YopRequest("SQKK10000469946", secretKey);
        request.addParam("requestno", OrderGenerator.INSTANCE.nextOrderId("msph"));
        request.addParam("issms", false);
        request.addParam("identityid", "110101198001010010");
        request.addParam("identitytype", "ID_CARD");
        request.addParam("cardtop", "621098");
        request.addParam("cardlast", "1329");
        request.addParam("amount", "100");
        request.addParam("terminalno", "SQKKSCENEKJ010");
        request.addParam("avaliabletime", 56L);
        request.addParam("callbackurl", "");
        request.addParam("requesttime", simpleDateFormat.format(new Date()));
        request.addParam("productname", "collect");
        request.addParam("dividejstr", divideJson);
        //分账结果异步通知地址
        //request.addParam("dividecallbackurl", "");
        try {
            YopResponse response = YopRsaClient.post("/rest/v1.0/paperorder/unified/pay", request);
            System.out.println("易宝分账接口返回状态："+ response.getState());
            System.out.println("易宝分账接口返回请求ID："+ response.getRequestId());
            System.out.println("易宝分账接口返回结果："+ response.getStringResult());
            System.out.println("易宝分账接口返回错误信息："+ response.getError());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 鉴权绑卡请求
     */
    private static void auth() {
        YopRequest request = new YopRequest("SQKK10000469946", secretKey);
        request.addParam("requestno", OrderGenerator.INSTANCE.nextOrderId("msph"));
        request.addParam("identityid", "110101198001010010");
        request.addParam("identitytype", "ID_CARD");
        request.addParam("advicesmstype", "MESSAGE");
        request.addParam("issms", true);
        //银行卡号
        request.addParam("cardno", "6210695610001621236");
        //身份证号
        request.addParam("idcardno","110101198001010010");
        request.addParam("idcardtype", "ID");
        //持卡人姓名
        request.addParam("username", "啦啦啦");
        //持卡人手机号（银行预留手机号）
        request.addParam("phone", "13737373737");
        request.addParam("requesttime", simpleDateFormat.format(new Date()));
        //固定值：COMMON_FOUR(验四)
        request.addParam("authtype", "COMMON_FOUR");
        try {
            YopResponse response = YopRsaClient.post("/rest/v1.0/paperorder/unified/auth/request", request);
            System.out.println("易宝鉴权绑卡接口返回状态："+ response.getState());
            System.out.println("易宝鉴权绑卡接口返回请求ID："+ response.getRequestId());
            System.out.println("易宝鉴权绑卡接口返回结果："+ response.getStringResult());
            System.out.println("易宝鉴权绑卡接口返回错误信息："+ response.getError());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void authconfirm() {
        YopRequest request = new YopRequest("SQKK10000469946", secretKey);
        request.addParam("requestno", "msph2020090815323002860620842483");
        request.addParam("validatecode", "326236");
        try {
            YopResponse response = YopRsaClient.post("/rest/v1.0/paperorder/auth/confirm", request);
            System.out.println("易宝鉴权绑卡确认接口返回状态："+ response.getState());
            System.out.println("易宝鉴权绑卡确认接口返回请求ID："+ response.getRequestId());
            System.out.println("易宝鉴权绑卡确认接口返回结果："+ response.getStringResult());
            System.out.println("易宝鉴权绑卡确认接口返回错误信息："+ response.getError());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
