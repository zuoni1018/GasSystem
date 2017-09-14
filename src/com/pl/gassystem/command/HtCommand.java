package com.pl.gassystem.command;

import com.common.utils.LogUtil;
import com.pl.common.Cfun;
import com.pl.gassystem.bean.ht.HtGetMessage;
import com.pl.gassystem.bean.ht.HtSendMessage;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static com.pl.protocol.HhProtocol.hexStr2ByteArray;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * ��������
 */

public class HtCommand {

    private final static String HT_PASSWORD = "01020304050607080000000000000000";//����AES key

//    private String XING_DAO="";
//    private String KUO_PIN="";

    public static String encodeHangTian(String data) {
        return parseByte2HexStr(encrypt(data, HT_PASSWORD));
    }


    public static String encodeHangTian(HtSendMessage htSendMessage) {
        String htCommand = "";

        //������� ������+���s+�����+80��ȫ
        String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo()
                + Cfun.getBCC(Cfun.x16Str2Byte(htSendMessage.getCommandType() + htSendMessage.getBookNo()));
        //���Ĳ�ȫ80 00
        if (seeMessage.length() < 32) {
            int addLength = 32 - seeMessage.length();
            seeMessage = seeMessage + "80";
            if (addLength > 2) {
                //�ж���Ҫ�Ӽ��� 00
                int add0Num = addLength / 2 - 1;
                for (int i = 0; i < add0Num; i++) {
                    seeMessage = seeMessage + "00";
                }
            }
        }
        LogUtil.i("����", "���� " + seeMessage);

        //����AES����
        String cipherMessage = HtCommand.encodeHangTian(seeMessage);//����
        //���� 68+"���ĳ���"(16����)+����+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//����������ʼ����

        LogUtil.i("����", "���ļ��� " + cipherMessage);

        //����ʱ�� ms*100 ת16����
        int time = htSendMessage.getWakeUpTime() / 100;
        String weekUpTime;
        if (time < 16) {
            //˵�����ɵĻ���ʱ���ֻ��һλ
            weekUpTime = "0" + Integer.toHexString(htSendMessage.getWakeUpTime() / 100);
        } else {
            weekUpTime = Integer.toHexString(htSendMessage.getWakeUpTime() / 100);
        }

        LogUtil.i("����", "����ʱ�� " + weekUpTime);
        //��ϳ����� "68" +���̱�� +���ĳ���+2(���ݳ���) +����ģʽ(06)+���ѱ�־+����ʱ��+�ŵ�+��Ƶ����+��������+"16"
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime + "0E09"
                + cipherMessage + "16";

        LogUtil.i("����", "�������� " + commandMessage);


        return commandMessage;
    }

    public static void readMessage(String result) {
        result = "68081306011e68102e0a2999a7e0e7a018a222645c6adf821616";
        //�� 04 00 00 15 �鿴����״̬
        //68 08 13 06 01 1e 68 10| 2e 0a 29 99 a7 e0 e7 a0 18 a2 22 64 5c 6a df 82 |16 16
        //ȡ�м��ģ��

        //�ȼ��ж�һ���ǲ�����Ҫ������
        if (result.substring(0, 2).equals("68")
                && result.substring(result.length() - 4, result.length()).equals("1616")
                && result.substring(12, 14).equals("68")) {
            //��ȡ����
            String cipherMessage = result.substring(16, result.length() - 4);
            LogUtil.i("����", "�������" + cipherMessage);
            //�������
            String seeMessage = parseByte2HexStr(decrypt(parseHexStr2Byte(cipherMessage), HT_PASSWORD));
            LogUtil.i("����", "�������" + seeMessage);
            //8204000015C32474 8000000000000000

            HtGetMessage htGetMessage=new HtGetMessage();

            //ǰ2λΪ��������
            String commandType=seeMessage.substring(0,2);

            if(commandType.equals(HtGetMessage.COMMAND_TYPE_DOOR_STATE)){
                //��ǰ����Ϊ�鿴����״̬
                htGetMessage.setCommandType(HtGetMessage.COMMAND_TYPE_DOOR_STATE);
                //����״̬
                String valveState=seeMessage.substring(10,12);
                htGetMessage.setValveState(valveState);
                LogUtil.i("����", "����״̬" + valveState);
                //��ȡ��ѹֵ
                String voltage=seeMessage.substring(12,14);
                htGetMessage.setVoltage(voltage);
                //��ȡ�ź�ǿ��
                String signal=seeMessage.substring(14,16);
                htGetMessage.setSignal(signal);
            }

            String bookNo=seeMessage.substring(3,10);
            htGetMessage.setBookNo(bookNo);

            LogUtil.i("����", "�������:\n"+" ��������:"+htGetMessage.getCommandType()
                    +" ���:"+htGetMessage.getBookNo()
                    +" ����״̬:"+htGetMessage.getValveState()+" ��ص�ѹ:"+htGetMessage.getVoltage()
                    +" �ź�ǿ��:"+htGetMessage.getSignal() );

        }


    }

    /**
     * ��16����ת��Ϊ������
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * ����
     * ����������
     * ������Կ
     */
    private static byte[] decrypt(byte[] content, String password) {
        try {
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(password.getBytes()));
            // SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = hexStr2ByteArray(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // Cipher cipher = Cipher.getInstance("AES");// ����������
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "�㷨/ģʽ/���뷽ʽ"
            cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��
            return cipher.doFinal(content); // ����
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����
     * ��Ҫ���ܵ�����
     * ��������
     */
    private static byte[] encrypt(String content, String password) {
        try {
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(hexStr2ByteArray(password)));
            // SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = hexStr2ByteArray(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // Cipher cipher = Cipher.getInstance("AES");// ����������
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "�㷨/ģʽ/���뷽ʽ"
            // byte[] byteContent = content.getBytes("utf-8");
            byte[] byteContent = hexStr2ByteArray(content);
            cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��
            return cipher.doFinal(byteContent); // ����
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ��������ת����16����
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
