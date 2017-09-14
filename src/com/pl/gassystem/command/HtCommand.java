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
 * 杭天命令
 */

public class HtCommand {

    private final static String HT_PASSWORD = "01020304050607080000000000000000";//杭天AES key

//    private String XING_DAO="";
//    private String KUO_PIN="";

    public static String encodeHangTian(String data) {
        return parseByte2HexStr(encrypt(data, HT_PASSWORD));
    }


    public static String encodeHangTian(HtSendMessage htSendMessage) {
        String htCommand = "";

        //获得明文 命令吗+表号s+异或吗+80补全
        String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo()
                + Cfun.getBCC(Cfun.x16Str2Byte(htSendMessage.getCommandType() + htSendMessage.getBookNo()));
        //明文补全80 00
        if (seeMessage.length() < 32) {
            int addLength = 32 - seeMessage.length();
            seeMessage = seeMessage + "80";
            if (addLength > 2) {
                //判断需要加几个 00
                int add0Num = addLength / 2 - 1;
                for (int i = 0; i < add0Num; i++) {
                    seeMessage = seeMessage + "00";
                }
            }
        }
        LogUtil.i("杭天", "明文 " + seeMessage);

        //明文AES加密
        String cipherMessage = HtCommand.encodeHangTian(seeMessage);//密文
        //密文 68+"密文长度"(16进制)+密文+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//密文设置起始符号

        LogUtil.i("杭天", "明文加密 " + cipherMessage);

        //唤醒时间 ms*100 转16进制
        int time = htSendMessage.getWakeUpTime() / 100;
        String weekUpTime;
        if (time < 16) {
            //说明生成的唤醒时间就只有一位
            weekUpTime = "0" + Integer.toHexString(htSendMessage.getWakeUpTime() / 100);
        } else {
            weekUpTime = Integer.toHexString(htSendMessage.getWakeUpTime() / 100);
        }

        LogUtil.i("杭天", "唤醒时间 " + weekUpTime);
        //组合成命令 "68" +厂商编号 +密文长度+2(数据长度) +工作模式(06)+唤醒标志+唤醒时间+信道+扩频因子+加密明文+"16"
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime + "0E09"
                + cipherMessage + "16";

        LogUtil.i("杭天", "发送密文 " + commandMessage);


        return commandMessage;
    }

    public static void readMessage(String result) {
        result = "68081306011e68102e0a2999a7e0e7a018a222645c6adf821616";
        //表 04 00 00 15 查看阀门状态
        //68 08 13 06 01 1e 68 10| 2e 0a 29 99 a7 e0 e7 a0 18 a2 22 64 5c 6a df 82 |16 16
        //取中间的模块

        //先简单判断一下是不是我要的数据
        if (result.substring(0, 2).equals("68")
                && result.substring(result.length() - 4, result.length()).equals("1616")
                && result.substring(12, 14).equals("68")) {
            //截取密文
            String cipherMessage = result.substring(16, result.length() - 4);
            LogUtil.i("杭天", "获得密文" + cipherMessage);
            //获得明文
            String seeMessage = parseByte2HexStr(decrypt(parseHexStr2Byte(cipherMessage), HT_PASSWORD));
            LogUtil.i("杭天", "获得明文" + seeMessage);
            //8204000015C32474 8000000000000000

            HtGetMessage htGetMessage=new HtGetMessage();

            //前2位为命令类型
            String commandType=seeMessage.substring(0,2);

            if(commandType.equals(HtGetMessage.COMMAND_TYPE_DOOR_STATE)){
                //当前命令为查看阀门状态
                htGetMessage.setCommandType(HtGetMessage.COMMAND_TYPE_DOOR_STATE);
                //阀门状态
                String valveState=seeMessage.substring(10,12);
                htGetMessage.setValveState(valveState);
                LogUtil.i("杭天", "阀门状态" + valveState);
                //获取电压值
                String voltage=seeMessage.substring(12,14);
                htGetMessage.setVoltage(voltage);
                //获取信号强度
                String signal=seeMessage.substring(14,16);
                htGetMessage.setSignal(signal);
            }

            String bookNo=seeMessage.substring(3,10);
            htGetMessage.setBookNo(bookNo);

            LogUtil.i("杭天", "解析结果:\n"+" 命令类型:"+htGetMessage.getCommandType()
                    +" 表号:"+htGetMessage.getBookNo()
                    +" 阀门状态:"+htGetMessage.getValveState()+" 电池电压:"+htGetMessage.getVoltage()
                    +" 信号强度:"+htGetMessage.getSignal() );

        }


    }

    /**
     * 将16进制转换为二进制
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
     * 解密
     * 待解密内容
     * 解密密钥
     */
    private static byte[] decrypt(byte[] content, String password) {
        try {
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(password.getBytes()));
            // SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = hexStr2ByteArray(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            return cipher.doFinal(content); // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * 需要加密的内容
     * 加密密码
     */
    private static byte[] encrypt(String content, String password) {
        try {
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(hexStr2ByteArray(password)));
            // SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = hexStr2ByteArray(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// "算法/模式/补码方式"
            // byte[] byteContent = content.getBytes("utf-8");
            byte[] byteContent = hexStr2ByteArray(content);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            return cipher.doFinal(byteContent); // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
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
