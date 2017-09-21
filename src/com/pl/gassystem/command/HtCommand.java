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
 * ��������
 */

public class HtCommand {

    public static String HT_PASSWORD = "01020304050607080000000000000000";//����AES key


    /**
     * �������ñ�Ų���
     */
    public static String encodeHangTianSetKey(HtSendMessageSetNewKey htSendMessage) {
        String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo();
        //Ⱥ��������
        if (htSendMessage.getBookNos().size() < 10) {
            seeMessage += "0" + htSendMessage.getBookNos().size();
        } else {
            seeMessage += htSendMessage.getBookNos().size();
        }
        //ƴ�ӱ��
        for (int i = 0; i < htSendMessage.getBookNos().size(); i++) {
            seeMessage += htSendMessage.getBookNos().get(i);
        }
        //��������Կ
        seeMessage+=htSendMessage.getNewKey();

        //���
        seeMessage = seeMessage + Cfun.getBCC(Cfun.x16Str2Byte(seeMessage));
        //��ȫ32λ�ı���
        int l = seeMessage.length();
        int a = l / 32;//������
        int b = l % 32;//����

        if (b != 0) {
            //˵�����Ĳ���32��������
            if (seeMessage.length() < 32 * (a + 1)) {
                int addLength = 32 * (a + 1) - seeMessage.length();
                seeMessage = seeMessage + "80";
                if (addLength > 2) {
                    //�ж���Ҫ�Ӽ��� 00
                    int add0Num = addLength / 2 - 1;
                    for (int i = 0; i < add0Num; i++) {
                        seeMessage = seeMessage + "00";
                    }
                }
            }
        }
        LogUtil.i("����", "Ⱥ������ " + seeMessage);

        //����AES����
        String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//����
        LogUtil.i("����", "���ļ���1 " + cipherMessage);
        //���� 68+"���ĳ���"(16����)+����+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//����������ʼ����
        LogUtil.i("����", "���ļ���2 " + cipherMessage);
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
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                + cipherMessage + "16";

        LogUtil.i("����", "�������� " + commandMessage);
        return commandMessage;
    }

    /**
     * �������ñ�Ų���
     */
    public static String encodeHangTianSetParameter(HtSendMessageSetParameter htSendMessage) {
        String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo();
        //Ⱥ��������
        if (htSendMessage.getBookNos().size() < 10) {
            seeMessage += "0" + htSendMessage.getBookNos().size();
        } else {
            seeMessage += htSendMessage.getBookNos().size();
        }
        //ƴ�ӱ��
        for (int i = 0; i < htSendMessage.getBookNos().size(); i++) {
            seeMessage += htSendMessage.getBookNos().get(i);
        }
        //�������ÿ���λ
        seeMessage += htSendMessage.isNeedKuoPinYinZi();
        //��Ƶ�ŵ�
        seeMessage += htSendMessage.getKuo_pin_xin_dao();
        //��Ƶ����
        seeMessage += htSendMessage.getKuo_pin_yin_zi();
        //���ö�������
        seeMessage += htSendMessage.getDong_jie_ri();
        //���ÿ�����ֹʱ��
        seeMessage += htSendMessage.getKai_chuang_qi_zhi_shi_jian();
        //���
        seeMessage = seeMessage + Cfun.getBCC(Cfun.x16Str2Byte(seeMessage));
        //��ȫ32λ�ı���
        int l = seeMessage.length();
        int a = l / 32;//������
        int b = l % 32;//����

        if (b != 0) {
            //˵�����Ĳ���32��������
            if (seeMessage.length() < 32 * (a + 1)) {
                int addLength = 32 * (a + 1) - seeMessage.length();
                seeMessage = seeMessage + "80";
                if (addLength > 2) {
                    //�ж���Ҫ�Ӽ��� 00
                    int add0Num = addLength / 2 - 1;
                    for (int i = 0; i < add0Num; i++) {
                        seeMessage = seeMessage + "00";
                    }
                }
            }
        }
        LogUtil.i("����", "Ⱥ������ " + seeMessage);

        //����AES����
        String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//����
        LogUtil.i("����", "���ļ���1 " + cipherMessage);
        //���� 68+"���ĳ���"(16����)+����+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//����������ʼ����
        LogUtil.i("����", "���ļ���2 " + cipherMessage);
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
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                + cipherMessage + "16";

        LogUtil.i("����", "�������� " + commandMessage);
        return commandMessage;
    }

    /**
     * �޸ı�� �ۼ�������
     */
    public static String encodeHangTianChangeBookNoOrCumulant(HtSendMessageChange htSendMessageChange) {

        String seeMessage;

        if (htSendMessageChange.getChangeType().equals(HtSendMessageChange.CHANGE_TYPE_ALL)) {
            //������� ������+���+�������ÿ���λ+�±��ַ+�ۼ���+�����+80��ȫ
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
        LogUtil.i("����", "�޸ı������ " + seeMessage);


        //����AES����
        String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//����
        //���� 68+"���ĳ���"(16����)+����+"16"
        cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//����������ʼ����

        LogUtil.i("����", "���ļ��� " + cipherMessage);

        //����ʱ�� ms*100 ת16����
        int time = htSendMessageChange.getWakeUpTime() / 100;
        String weekUpTime;
        if (time < 16) {
            //˵�����ɵĻ���ʱ���ֻ��һλ
            weekUpTime = "0" + Integer.toHexString(htSendMessageChange.getWakeUpTime() / 100);
        } else {
            weekUpTime = Integer.toHexString(htSendMessageChange.getWakeUpTime() / 100);
        }

        LogUtil.i("����", "����ʱ�� " + weekUpTime);
        //��ϳ����� "68" +���̱�� +���ĳ���+2(���ݳ���) +����ģʽ(06)+���ѱ�־+����ʱ��+�ŵ�+��Ƶ����+��������+"16"
        String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessageChange.getWakeUpMark() + weekUpTime
                + htSendMessageChange.getKuoPinXinDao() + htSendMessageChange.getKuoPinYinZi()
                + cipherMessage + "16";

        LogUtil.i("����", "�������� " + commandMessage);
        return commandMessage;
    }

    /**
     * ����
     */
    public static String encodeHangTian(HtSendMessage htSendMessage) {

        if (htSendMessage.getCopyType().equals(HtSendMessage.COPY_TYPE_GROUP)) {
            //Ⱥ������
            //��ȡ����
            String seeMessage = htSendMessage.getCommandType() + htSendMessage.getBookNo();
            //Ⱥ��������
            if (htSendMessage.getBookNos().size() < 10) {
                seeMessage += "0" + htSendMessage.getBookNos().size();
            } else {
                seeMessage += htSendMessage.getBookNos().size();
            }

            //ƴ�ӱ��
            for (int i = 0; i < htSendMessage.getBookNos().size(); i++) {
                seeMessage += htSendMessage.getBookNos().get(i);
            }
            //ƴ������
            seeMessage += htSendMessage.getSetTime();
            //���
            seeMessage = seeMessage + Cfun.getBCC(Cfun.x16Str2Byte(seeMessage));
            //��ȫ32λ�ı���
            int l = seeMessage.length();
            int a = l / 32;//������
            int b = l % 32;//����

            if (b != 0) {
                //˵�����Ĳ���32��������
                if (seeMessage.length() < 32 * (a + 1)) {
                    int addLength = 32 * (a + 1) - seeMessage.length();
                    seeMessage = seeMessage + "80";
                    if (addLength > 2) {
                        //�ж���Ҫ�Ӽ��� 00
                        int add0Num = addLength / 2 - 1;
                        for (int i = 0; i < add0Num; i++) {
                            seeMessage = seeMessage + "00";
                        }
                    }
                }
            }

            LogUtil.i("����", "Ⱥ������ " + seeMessage);

            //����AES����
            String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//����
            LogUtil.i("����", "���ļ���1 " + cipherMessage);
            //���� 68+"���ĳ���"(16����)+����+"16"
            cipherMessage = "68" + Integer.toHexString(cipherMessage.length() / 2) + cipherMessage + "16";//����������ʼ����
            LogUtil.i("����", "���ļ���2 " + cipherMessage);
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
            String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                    + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                    + cipherMessage + "16";

            LogUtil.i("����", "�������� " + commandMessage);

            return commandMessage;
        } else {

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
            String cipherMessage = parseByte2HexStr(encrypt(seeMessage, HT_PASSWORD));//����
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
            String commandMessage = "6808" + Integer.toHexString(cipherMessage.length() / 2 + 2) + "06" + htSendMessage.getWakeUpMark() + weekUpTime
                    + htSendMessage.getKuoPinXinDao() + htSendMessage.getKuoPinYinZi()
                    + cipherMessage + "16";

            LogUtil.i("����", "�������� " + commandMessage);

            return commandMessage;
        }

    }

    public static HtGetMessage readMessage(String result) {

//        result = "68081306011e68102e0a2999a7e0e7a018a222645c6adf821616";
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
            //8404000015C32472 8000000000000000
            //8304000015C22474 8000000000000000
            HtGetMessage htGetMessage = new HtGetMessage();


            //ǰ2λΪ��������
            String commandType = seeMessage.substring(0, 2);
            htGetMessage.setCommandType(commandType);
            LogUtil.i("����", "��������" + commandType);


            if (commandType.equals(HtGetMessage.COMMAND_TYPE_DOOR_STATE)
                    | commandType.equals(HtGetMessage.COMMAND_TYPE_CLOSE_DOOR)
                    | commandType.equals(HtGetMessage.COMMAND_TYPE_OPEN_DOOR)) {
                //��3���������� ֻ���ȡ ����״̬ ��ѹֵ �ź�ǿ��

                //����״̬
                String valveState = seeMessage.substring(10, 12);

                htGetMessage.setValveState(valveState);
                LogUtil.i("����", "����״̬" + valveState);
                //��ȡ��ѹֵ
                String voltage = seeMessage.substring(12, 14);
                htGetMessage.setVoltage(voltage);
                //��ȡ�ź�ǿ��
                String signal = seeMessage.substring(14, 16);
                htGetMessage.setSignal(signal);
            } else if (commandType.equals(HtGetMessage.COMMAND_TYPE_COPY_NORMAL)
                    | commandType.equals(HtGetMessage.COMMAND_TYPE_COPY_FROZEN)) {
                //��������Ϊ��ͨ����(���𶳽᳭��)

                //��������
                String frozenTime = seeMessage.substring(10, 14);
                htGetMessage.setFrozenTime(frozenTime);
                //��ȡ����
                String copyValue = seeMessage.substring(14, 22);
                htGetMessage.setCopyValue(copyValue);

                //��ȡ����״̬
                String valveState = seeMessage.substring(22, 24);
                htGetMessage.setValveState(valveState);


                //��ȡ��ѹֵ
                String voltage = seeMessage.substring(24, 26);
                htGetMessage.setVoltage(voltage);

                //��ȡ�ź�ǿ��
                String signal = seeMessage.substring(26, 28);
                htGetMessage.setSignal(signal);
            }

            String bookNo = seeMessage.substring(3, 10);
            htGetMessage.setBookNo(bookNo);

//            LogUtil.i("����", "�������:\n" + " ��������:" + htGetMessage.getCommandType()
//                    + " ���:" + htGetMessage.getBookNo()
//                    + " ����״̬:" + htGetMessage.getValveState() + " ��ص�ѹ:" + htGetMessage.getVoltage()
//                    + " �ź�ǿ��:" + htGetMessage.getSignal());
            return htGetMessage;
        }


        return null;
    }

    public static HtGetMessageChangeBookNoOrCumulant readChangeBookNoOrCumulantMessage(String result) {
        HtGetMessageChangeBookNoOrCumulant htGetMessageQueryParameter = new HtGetMessageChangeBookNoOrCumulant();
        //���ж��� ���Բ�Ҫ
        if (result.substring(0, 2).equals("68")
                && result.substring(result.length() - 4, result.length()).equals("1616")
                && result.substring(12, 14).equals("68")) {
            //��ȡ����
            String cipherMessage = result.substring(16, result.length() - 4);
            LogUtil.i("����", "�������" + cipherMessage);
            //�������
            String seeMessage = parseByte2HexStr(decrypt(parseHexStr2Byte(cipherMessage), HT_PASSWORD));
            LogUtil.i("����", "�������" + seeMessage);
            String commandType = seeMessage.substring(0, 2);
            htGetMessageQueryParameter.setCommandType(commandType);

            String newBookNo = seeMessage.substring(2, 10);
            htGetMessageQueryParameter.setNewBookNo(newBookNo);
            htGetMessageQueryParameter.setOperationResult(seeMessage.substring(10, 12));
        }
        return htGetMessageQueryParameter;
    }

    /**
     * ��ȡ��ѯ����
     */
    public static HtGetMessageQueryParameter readQueryParameterMessage(String result) {
        HtGetMessageQueryParameter htGetMessageQueryParameter = new HtGetMessageQueryParameter();
        //���ж��� ���Բ�Ҫ
        if (result.substring(0, 2).equals("68")
                && result.substring(result.length() - 4, result.length()).equals("1616")
                && result.substring(12, 14).equals("68")) {
            //��ѯ����ʱ�� ��Ӧ�𲻼���
            String seeMessage = result.substring(14, result.length() - 4);
            LogUtil.i("����", "��ò�ѯ��������" + seeMessage);
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

            //�±��з��س������ �ϱ�û��
            try {
                htGetMessageQueryParameter.setChao_biao_ci_shu(seeMessage.substring(116, 120));
            } catch (StringIndexOutOfBoundsException e) {
                htGetMessageQueryParameter.setChao_biao_ci_shu("0000");
            }


        }
        return htGetMessageQueryParameter;
    }

    /**
     * ��16����ת��Ϊ10����
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
