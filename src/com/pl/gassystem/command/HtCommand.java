package com.pl.gassystem.command;

import com.pl.gassystem.bean.ht.HtSendMessageSetNewKey;
import com.pl.gassystem.utils.LogUtil;
import com.pl.common.Cfun;
import com.pl.gassystem.bean.ht.HtGetMessage;
import com.pl.gassystem.bean.ht.HtGetMessageChangeBookNoOrCumulant;
import com.pl.gassystem.bean.ht.HtGetMessageQueryParameter;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.bean.ht.HtSendMessageChange;
import com.pl.gassystem.bean.ht.HtSendMessageSetParameter;

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

    public static String HT_PASSWORD = "01020304050607080000000000000000";//杭天AES key


    /**
     * 批量设置表号参数
     */
    public static String encodeHangTianSetKey(HtSendMessageSetNewKey htSendMessage) {
        String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo();
        //群抄表数量
        if (htSendMessage.getBookNos().size() < 10) {
            seeMessage += "0" + htSendMessage.getBookNos().size();
        } else {
            seeMessage += htSendMessage.getBookNos().size();
        }
        //拼接表号
        for (int i = 0; i < htSendMessage.getBookNos().size(); i++) {
            seeMessage += htSendMessage.getBookNos().get(i);
        }
        //设置新密钥
        seeMessage+=htSendMessage.getNewKey();

        //异或
        seeMessage = seeMessage + Cfun.getBCC(Cfun.x16Str2Byte(seeMessage));
        //补全32位的倍数
        int l = seeMessage.length();
        int a = l / 32;//整数倍
        int b = l % 32;//余数

        if (b != 0) {
            //说明明文不是32的整数倍
            if (seeMessage.length() < 32 * (a + 1)) {
                int addLength = 32 * (a + 1) - seeMessage.length();
                seeMessage = seeMessage + "80";
                if (addLength > 2) {
                    //判断需要加几个 00
                    int add0Num = addLength / 2 - 1;
                    for (int i = 0; i < add0Num; i++) {
                        seeMessage = seeMessage + "00";
                    }
                }
            }
        }
        LogUtil.i("杭天", "群抄明文 " + seeMessage);

        //明文AES加密
        String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//密文
        LogUtil.i("杭天", "明文加密1 " + cipherMessage);
        //密文 68+"密文长度"(16进制)+密文+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//密文设置起始符号
        LogUtil.i("杭天", "明文加密2 " + cipherMessage);
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
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                + cipherMessage + "16";

        LogUtil.i("杭天", "发送密文 " + commandMessage);
        return commandMessage;
    }

    /**
     * 批量设置表号参数
     */
    public static String encodeHangTianSetParameter(HtSendMessageSetParameter htSendMessage) {
        String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo();
        //群抄表数量
        if (htSendMessage.getBookNos().size() < 10) {
            seeMessage += "0" + htSendMessage.getBookNos().size();
        } else {
            seeMessage += htSendMessage.getBookNos().size();
        }
        //拼接表号
        for (int i = 0; i < htSendMessage.getBookNos().size(); i++) {
            seeMessage += htSendMessage.getBookNos().get(i);
        }
        //参数设置控制位
        seeMessage += htSendMessage.isNeedKuoPinYinZi();
        //扩频信道
        seeMessage += htSendMessage.getKuo_pin_xin_dao();
        //扩频因子
        seeMessage += htSendMessage.getKuo_pin_yin_zi();
        //设置冻结日期
        seeMessage += htSendMessage.getDong_jie_ri();
        //设置开窗起止时间
        seeMessage += htSendMessage.getKai_chuang_qi_zhi_shi_jian();
        //异或
        seeMessage = seeMessage + Cfun.getBCC(Cfun.x16Str2Byte(seeMessage));
        //补全32位的倍数
        int l = seeMessage.length();
        int a = l / 32;//整数倍
        int b = l % 32;//余数

        if (b != 0) {
            //说明明文不是32的整数倍
            if (seeMessage.length() < 32 * (a + 1)) {
                int addLength = 32 * (a + 1) - seeMessage.length();
                seeMessage = seeMessage + "80";
                if (addLength > 2) {
                    //判断需要加几个 00
                    int add0Num = addLength / 2 - 1;
                    for (int i = 0; i < add0Num; i++) {
                        seeMessage = seeMessage + "00";
                    }
                }
            }
        }
        LogUtil.i("杭天", "群抄明文 " + seeMessage);

        //明文AES加密
        String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//密文
        LogUtil.i("杭天", "明文加密1 " + cipherMessage);
        //密文 68+"密文长度"(16进制)+密文+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//密文设置起始符号
        LogUtil.i("杭天", "明文加密2 " + cipherMessage);
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
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                + cipherMessage + "16";

        LogUtil.i("杭天", "发送密文 " + commandMessage);
        return commandMessage;
    }

    /**
     * 修改表号 累计量编码
     */
    public static String encodeHangTianChangeBookNoOrCumulant(HtSendMessageChange htSendMessageChange) {

        String seeMessage;

        if (htSendMessageChange.getChangeType().equals(HtSendMessageChange.CHANGE_TYPE_ALL)) {
            //获得明文 命令吗+表号+参数设置控制位+新表地址+累计量+异或吗+80补全
            seeMessage = htSendMessageChange.getCommandType() + htSendMessageChange.getBookNo() + htSendMessageChange.getChangeType()
                    + htSendMessageChange.getNewBookNo() + htSendMessageChange.getCumulant()
                    + Cfun.getBCC(Cfun.x16Str2Byte(htSendMessageChange.getCommandType() + htSendMessageChange.getBookNo() + htSendMessageChange.getChangeType()
                    + htSendMessageChange.getNewBookNo() + htSendMessageChange.getCumulant()));
        } else if (htSendMessageChange.getChangeType().equals(HtSendMessageChange.CHANGE_TYPE_BOOK_NO)) {
            seeMessage = htSendMessageChange.getCommandType() + htSendMessageChange.getBookNo() + htSendMessageChange.getChangeType()
                    + htSendMessageChange.getNewBookNo()
                    + Cfun.getBCC(Cfun.x16Str2Byte(htSendMessageChange.getCommandType() + htSendMessageChange.getBookNo() + htSendMessageChange.getChangeType()
                    + htSendMessageChange.getNewBookNo()));
        } else {
            seeMessage = htSendMessageChange.getCommandType() + htSendMessageChange.getBookNo() + htSendMessageChange.getChangeType()
                    + htSendMessageChange.getCumulant()
                    + Cfun.getBCC(Cfun.x16Str2Byte(htSendMessageChange.getCommandType() + htSendMessageChange.getBookNo() + htSendMessageChange.getChangeType()
                    + htSendMessageChange.getCumulant()));
        }
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
        LogUtil.i("杭天", "修改表号明文 " + seeMessage);


        //明文AES加密
        String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//密文
        //密文 68+"密文长度"(16进制)+密文+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//密文设置起始符号

        LogUtil.i("杭天", "明文加密 " + cipherMessage);

        //唤醒时间 ms*100 转16进制
        int time = htSendMessageChange.getWakeUpTime() / 100;
        String weekUpTime;
        if (time < 16) {
            //说明生成的唤醒时间就只有一位
            weekUpTime = "0" + Integer.toHexString(htSendMessageChange.getWakeUpTime() / 100);
        } else {
            weekUpTime = Integer.toHexString(htSendMessageChange.getWakeUpTime() / 100);
        }

        LogUtil.i("杭天", "唤醒时间 " + weekUpTime);
        //组合成命令 "68" +厂商编号 +密文长度+2(数据长度) +工作模式(06)+唤醒标志+唤醒时间+信道+扩频因子+加密明文+"16"
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessageChange.getWakeUpMark() + weekUpTime
                + htSendMessageChange.getKuoPinXinDao() + htSendMessageChange.getKuoPinYinZi()
                + cipherMessage + "16";

        LogUtil.i("杭天", "发送密文 " + commandMessage);
        return commandMessage;
    }

    /**
     * 其他
     */
    public static String encodeHangTian(HtSendMessage htSendMessage) {

        if (htSendMessage.getCopyType().equals(HtSendMessage.COPY_TYPE_GROUP)) {
            //群抄命令
            //获取明文
            String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo();
            //群抄表数量
            if (htSendMessage.getBookNos().size() < 10) {
                seeMessage += "0" + htSendMessage.getBookNos().size();
            } else {
                seeMessage += htSendMessage.getBookNos().size();
            }

            //拼接表号
            for (int i = 0; i < htSendMessage.getBookNos().size(); i++) {
                seeMessage += htSendMessage.getBookNos().get(i);
            }
            //拼接日期
            seeMessage += htSendMessage.getSetTime();
            //异或
            seeMessage = seeMessage + Cfun.getBCC(Cfun.x16Str2Byte(seeMessage));
            //补全32位的倍数
            int l = seeMessage.length();
            int a = l / 32;//整数倍
            int b = l % 32;//余数

            if (b != 0) {
                //说明明文不是32的整数倍
                if (seeMessage.length() < 32 * (a + 1)) {
                    int addLength = 32 * (a + 1) - seeMessage.length();
                    seeMessage = seeMessage + "80";
                    if (addLength > 2) {
                        //判断需要加几个 00
                        int add0Num = addLength / 2 - 1;
                        for (int i = 0; i < add0Num; i++) {
                            seeMessage = seeMessage + "00";
                        }
                    }
                }
            }

            LogUtil.i("杭天", "群抄明文 " + seeMessage);

            //明文AES加密
            String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//密文
            LogUtil.i("杭天", "明文加密1 " + cipherMessage);
            //密文 68+"密文长度"(16进制)+密文+"16"
            cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//密文设置起始符号
            LogUtil.i("杭天", "明文加密2 " + cipherMessage);
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
            String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                    + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                    + cipherMessage + "16";

            LogUtil.i("杭天", "发送密文 " + commandMessage);

            return commandMessage;
        } else {

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
            String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//密文
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
            String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                    + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                    + cipherMessage + "16";

            LogUtil.i("杭天", "发送密文 " + commandMessage);

            return commandMessage;
        }

    }

    public static HtGetMessage readMessage(String result) {

//        result = "68081306011e68102e0a2999a7e0e7a018a222645c6adf821616";
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
            //8404000015C32472 8000000000000000
            //8304000015C22474 8000000000000000
            HtGetMessage htGetMessage = new HtGetMessage();


            //前2位为命令类型
            String commandType = seeMessage.substring(0, 2);
            htGetMessage.setCommandType(commandType);
            LogUtil.i("杭天", "命令类型" + commandType);


            if (commandType.equals(HtGetMessage.COMMAND_TYPE_DOOR_STATE)
                    | commandType.equals(HtGetMessage.COMMAND_TYPE_CLOSE_DOOR)
                    | commandType.equals(HtGetMessage.COMMAND_TYPE_OPEN_DOOR)) {
                //这3种命令类型 只会获取 阀门状态 电压值 信号强度

                //阀门状态
                String valveState = seeMessage.substring(10, 12);

                htGetMessage.setValveState(valveState);
                LogUtil.i("杭天", "阀门状态" + valveState);
                //获取电压值
                String voltage = seeMessage.substring(12, 14);
                htGetMessage.setVoltage(voltage);
                //获取信号强度
                String signal = seeMessage.substring(14, 16);
                htGetMessage.setSignal(signal);
            } else if (commandType.equals(HtGetMessage.COMMAND_TYPE_COPY_NORMAL)
                    | commandType.equals(HtGetMessage.COMMAND_TYPE_COPY_FROZEN)) {
                //抄表命令为普通抄表(区别冻结抄表)

                //冻结日期
                String frozenTime = seeMessage.substring(10, 14);
                htGetMessage.setFrozenTime(frozenTime);
                //获取读数
                String copyValue = seeMessage.substring(14, 22);
                htGetMessage.setCopyValue(copyValue);

                //获取阀门状态
                String valveState = seeMessage.substring(22, 24);
                htGetMessage.setValveState(valveState);


                //获取电压值
                String voltage = seeMessage.substring(24, 26);
                htGetMessage.setVoltage(voltage);

                //获取信号强度
                String signal = seeMessage.substring(26, 28);
                htGetMessage.setSignal(signal);
            }

            String bookNo = seeMessage.substring(3, 10);
            htGetMessage.setBookNo(bookNo);

//            LogUtil.i("杭天", "解析结果:\n" + " 命令类型:" + htGetMessage.getCommandType()
//                    + " 表号:" + htGetMessage.getBookNo()
//                    + " 阀门状态:" + htGetMessage.getValveState() + " 电池电压:" + htGetMessage.getVoltage()
//                    + " 信号强度:" + htGetMessage.getSignal());
            return htGetMessage;
        }


        return null;
    }

    public static HtGetMessageChangeBookNoOrCumulant readChangeBookNoOrCumulantMessage(String result) {
        HtGetMessageChangeBookNoOrCumulant htGetMessageQueryParameter = new HtGetMessageChangeBookNoOrCumulant();
        //简单判断下 可以不要
        if (result.substring(0, 2).equals("68")
                && result.substring(result.length() - 4, result.length()).equals("1616")
                && result.substring(12, 14).equals("68")) {
            //截取密文
            String cipherMessage = result.substring(16, result.length() - 4);
            LogUtil.i("杭天", "获得密文" + cipherMessage);
            //获得明文
            String seeMessage = parseByte2HexStr(decrypt(parseHexStr2Byte(cipherMessage), HT_PASSWORD));
            LogUtil.i("杭天", "获得明文" + seeMessage);
            String commandType = seeMessage.substring(0, 2);
            htGetMessageQueryParameter.setCommandType(commandType);

            String newBookNo = seeMessage.substring(2, 10);
            htGetMessageQueryParameter.setNewBookNo(newBookNo);
            htGetMessageQueryParameter.setOperationResult(seeMessage.substring(10, 12));
        }
        return htGetMessageQueryParameter;
    }

    /**
     * 读取查询参数
     */
    public static HtGetMessageQueryParameter readQueryParameterMessage(String result) {
        HtGetMessageQueryParameter htGetMessageQueryParameter = new HtGetMessageQueryParameter();
        //简单判断下 可以不要
        if (result.substring(0, 2).equals("68")
                && result.substring(result.length() - 4, result.length()).equals("1616")
                && result.substring(12, 14).equals("68")) {
            //查询参数时候 该应答不加密
            String seeMessage = result.substring(14, result.length() - 4);
            LogUtil.i("杭天", "获得查询参数明文" + seeMessage);
            //bc 05170016 0e090100002462240103170918105428000102030405060708010e0b010000241708211434370000000000ffffffffffffffffffff

            String commandType = seeMessage.substring(0, 2);
            htGetMessageQueryParameter.setCommandType(commandType);
            String bookNo = seeMessage.substring(2, 10);
            htGetMessageQueryParameter.setBookNo(bookNo);

            htGetMessageQueryParameter.setKuo_pin_xin_dao(seeMessage.substring(10, 12));
            htGetMessageQueryParameter.setKuo_pin_yin_zi(seeMessage.substring(12, 14));
            htGetMessageQueryParameter.setDong_jie_ri(seeMessage.substring(14, 18));
            htGetMessageQueryParameter.setKai_chuang_qi_zhi_shi_jian(seeMessage.substring(18, 22));
            htGetMessageQueryParameter.setBiao_ju_zhuang_tai(seeMessage.substring(22, 24));
            htGetMessageQueryParameter.setVoltage(seeMessage.substring(24, 26));
            htGetMessageQueryParameter.setRuan_jian_ban_ben_hao(seeMessage.substring(26, 30));
            htGetMessageQueryParameter.setBiao_ju_dang_qian_shi_jian(seeMessage.substring(30, 42));
            htGetMessageQueryParameter.setMi_ma_ban_ben_hao(seeMessage.substring(42, 44));
            htGetMessageQueryParameter.setMi_ma(seeMessage.substring(44, 60));
            htGetMessageQueryParameter.setShang_ci_can_shu_kong_zhi_wei(seeMessage.substring(60, 62));
            htGetMessageQueryParameter.setShang_ban_ben_xin_dao(seeMessage.substring(62, 64));
            htGetMessageQueryParameter.setShang_ban_ben_fen_pin_yi_zi(seeMessage.substring(64, 66));
            htGetMessageQueryParameter.setShang_ban_ben_dong_jie_ri(seeMessage.substring(66, 70));
            htGetMessageQueryParameter.setShang_ban_ben_kai_chuang_qi_zhi_shi_jian(seeMessage.substring(70, 74));
            htGetMessageQueryParameter.setShang_ban_ben_can_shu_she_zhi_shi_jian(seeMessage.substring(74, 86));
            htGetMessageQueryParameter.setShang_ban_ben_mi_yao_ban_ben_hao(seeMessage.substring(86, 88));
            htGetMessageQueryParameter.setShang_ban_ben_mi_yao(seeMessage.substring(88, 104));
            htGetMessageQueryParameter.setShang_ci_mi_yao_she_zhi_shi_jian(seeMessage.substring(104, 116));

            //新表有返回抄表次数 老表没有
            try {
                htGetMessageQueryParameter.setChao_biao_ci_shu(seeMessage.substring(116, 120));
            } catch (StringIndexOutOfBoundsException e) {
                htGetMessageQueryParameter.setChao_biao_ci_shu("0000");
            }


        }
        return htGetMessageQueryParameter;
    }

    /**
     * 将16进制转换为10进制
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
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
