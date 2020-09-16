package com.msyd.payrouter.util;

import cfca.sm2rsa.common.Mechanism;
import cfca.sm2rsa.common.PKIException;
import cfca.util.*;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;
import cfca.x509.certificate.X509CertHelper;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RsaP1Util {

    protected static final Logger log = Logger.getLogger(RsaP1Util.class);

    public static Session session = null;

    static {
        //软库初始化
        try {
            JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
            session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        } catch (PKIException e) {
            // TODO Auto-generated catch block
            log.error("CFCA软库初始化异常:", e);
        }

    }

    /*
     * 将base64证书信息转换为证书
     */
    public X509Cert parseCert(String certStr) throws PKIException, UnsupportedEncodingException {

        X509Cert cert = X509CertHelper.parse(certStr.getBytes("UTF-8"));
        return cert;

    }

    /**
     * 6.1 RSA消息签名(PKCS#1)
     * 
     * @throws Exception
     */
    public String rsaP1Sign(String srcData, String pfxPath, String pwd) throws Exception {
        PrivateKey priKey = (PrivateKey) getRSAPriKey(pfxPath, pwd);
        String alg = Mechanism.SHA256_RSA;
        byte[] signature = new SignatureUtil2().p1SignMessage(alg, srcData.getBytes("UTF-8"), priKey, session);
        return new String(signature, "UTF-8");
    }

    public String rsaP1Sign(String srcData, PrivateKey priKey) throws Exception {
        String alg = Mechanism.SHA256_RSA;
        byte[] signature = new SignatureUtil2().p1SignMessage(alg, srcData.getBytes("UTF-8"), priKey, session);
        return new String(signature, "UTF-8");
    }

    private Key getRSAPriKey(String pfxPath, String pwd) throws Exception {
        Key priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        return priKey;
    }

    /**
     * RSA消息验签(PKCS#1)--通过路径获取公钥信息
     * @param signData
     * @param srcData
     * @param cerPath
     * @return
     * @throws Exception
     */
    public boolean rsaP1Verify(String signData, String srcData, String cerPath) throws Exception {
        String alg = Mechanism.SHA256_RSA;
        PublicKey pubKey = (PublicKey) getRSAPubKey(cerPath);
        boolean ret = new SignatureUtil2().p1VerifyMessage(alg, srcData.getBytes("UTF-8"), signData.getBytes(), pubKey,
                session);
        log.info("verify=" + ret);
        return ret;
    }

    /**
     * RSA消息验签(PKCS#1)--直接获取公钥信息
     * @param signData
     * @param srcData
     * @param cert
     * @return
     * @throws Exception
     */
    public boolean rsaP1Verify(String signData, String srcData, byte[] cert) throws Exception {
        String alg = Mechanism.SHA256_RSA;
        PublicKey pubKey = (PublicKey) getRSAPubKey(cert);
        boolean ret = new SignatureUtil2().p1VerifyMessage(alg, srcData.getBytes("UTF-8"), signData.getBytes(), pubKey,
                session);
        log.info("verify=" + ret);
        return ret;
    }

    public boolean rsaP1Verify(String signData, String srcData, PublicKey pubKey) throws Exception {
        String alg = Mechanism.SHA256_RSA;
        boolean ret = new SignatureUtil2().p1VerifyMessage(alg, srcData.getBytes("UTF-8"), signData.getBytes(), pubKey,
                session);
        log.info("verify=" + ret);
        return ret;
    }

    private Key getRSAPubKey(String cerPath) throws Exception {
        X509Cert cert = X509CertHelper.parse(cerPath);
        Key pubKey = cert.getPublicKey();
        return pubKey;
    }

    private Key getRSAPubKey(byte[] cert) throws Exception {
        X509Cert x509cert = X509CertHelper.parse(cert);
        Key pubKey = x509cert.getPublicKey();
        return pubKey;
    }

    /**
     * 7.1 PKCS#7消息加密(数字信封)
     * 
     * @throws Exception
     */
    public byte[] envelopMessage(String srcData, String cerPath) throws Exception {
        FileInputStream is = null;
        try {
            X509Cert[] certs = new X509Cert[1];
            is = new FileInputStream(cerPath);
            X509Cert cert = new X509Cert(is);
            certs[0] = cert;
            byte[] encryptedData = EnvelopeUtil.envelopeMessage(srcData.getBytes("UTF-8"), Mechanism.DES3_CBC, certs);
            return encryptedData;
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Exception e) {
                }
        }
    }

    public byte[] envelopMessageByCert(String srcData, String cer) throws Exception {

        X509Cert[] certs = new X509Cert[1];
        X509Cert cert = new X509Cert(cer.getBytes("UTF-8"));
        certs[0] = cert;
        byte[] encryptedData = EnvelopeUtil.envelopeMessage(srcData.getBytes("UTF-8"), Mechanism.DES3_CBC, certs);
        return encryptedData;
    }

    /**
     * 7.1.1 消息解密(数字信封)
     * 
     * @throws Exception
     */
    public byte[] openMessage(String encryptedData, String pfxPath, String pfxPwd) throws Exception {

        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pfxPwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pfxPwd);
        byte[] sourceData = EnvelopeUtil.openEvelopedMessage(encryptedData.getBytes(), priKey, cert, session);
        return sourceData;
    }

    public static void main(String args[]) {
        String srcData = "asdfsadfasfasdf";
        String merchantPfxFile = "D:/cert/test.pfx";
        String merchantCerFile = "D:/cert/test.cer";
        RsaP1Util rsa = new RsaP1Util();
        try {
            String sign = rsa.rsaP1Sign(srcData, merchantPfxFile, "umbpay");
            System.out.println("签名：" + sign);
            String envelopdata = new String(rsa.envelopMessage(srcData, merchantCerFile));
            System.out.println("加密：" + envelopdata);

            //备注 因为发给商户的是商户私钥和平台公钥 ，所以无法自己解自己所加的密文，也无法验签自己所签的签名。故签名加密后要发到平台，用平台的返回报文测试解密和验签是否有效。
            String opendata = new String(rsa.openMessage(envelopdata, merchantPfxFile, "umbpay"));
            System.out.println("解密：" + opendata);

            FileInputStream is = null;
            is = new FileInputStream(merchantCerFile);
            X509Cert x509Cert = new X509Cert(is);
            String cert = new String(Base64.encode(x509Cert.getEncoded()), "UTF-8");
            boolean verify = rsa.rsaP1Verify(sign, srcData, cert.getBytes());
            System.out.println("验签：" + verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
