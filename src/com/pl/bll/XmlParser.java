package com.pl.bll;

import com.pl.entity.BookInfo;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.CopyDataPhoto;
import com.pl.entity.GroupBind;
import com.pl.entity.GroupInfo;
import com.pl.entity.HrCustomerInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class XmlParser {
	/**
	 * ��������������ȡʵ�弯��, ʹ��pull������ʽ
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ArrayList<BookInfo> parseBookInfos(InputStream in)
			throws XmlPullParserException, IOException {
		ArrayList<BookInfo> bookInfos = null;
		BookInfo bookInfo = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				bookInfos = new ArrayList<BookInfo>();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("BookInfo".equals(tagName)) {
					// ��ʼ����һ���µ�BookInfo
					bookInfo = new BookInfo();
					// //��ȡ�����ϵ�ID
					// m.setId(Integer.parseInt(parser.getAttributeValue(null,
					// "id")));
				} else if ("bookNo".equals(tagName)) {
					bookInfo.setBookNo(parser.nextText());
				} else if ("estateNo".equals(tagName)) {
					bookInfo.setEstateNo(parser.nextText());
				} else if ("bookName".equals(tagName)) {
					bookInfo.setBookName(parser.nextText());
				} else if ("staffNo".equals(tagName)) {
					bookInfo.setStaffNo(parser.nextText());
				} else if ("Remark".equals(tagName)) {
					bookInfo.setRemark(parser.nextText());
				} else if ("meterTypeNo".equals(tagName)) {
					bookInfo.setMeterTypeNo(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				if ("BookInfo".equals(parser.getName())) {
					bookInfos.add(bookInfo);
				}
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}

		return bookInfos;
	}

	public ArrayList<GroupInfo> parseGroupInfos(InputStream in)
			throws XmlPullParserException, IOException {
		ArrayList<GroupInfo> groupInfos = null;
		GroupInfo groupInfo = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				groupInfos = new ArrayList<GroupInfo>();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("GroupInfo".equals(tagName)) {
					// ��ʼ����һ���µ�BookInfo
					groupInfo = new GroupInfo();
				} else if ("groupNo".equals(tagName)) {
					groupInfo.setGroupNo(parser.nextText());
				} else if ("estateNo".equals(tagName)) {
					groupInfo.setEstateNo(parser.nextText());
				} else if ("groupName".equals(tagName)) {
					groupInfo.setGroupName(parser.nextText());
				} else if ("bookNo".equals(tagName)) {
					groupInfo.setBookNo(parser.nextText());
				} else if ("Remark".equals(tagName)) {
					groupInfo.setRemark(parser.nextText());
				} else if ("meterTypeNo".equals(tagName)) {
					groupInfo.setMeterTypeNo(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				if ("GroupInfo".equals(parser.getName())) {
					groupInfos.add(groupInfo);
				}
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return groupInfos;
	}

	public ArrayList<GroupBind> parseGroupBind(InputStream in)
			throws XmlPullParserException, IOException {
		ArrayList<GroupBind> groupBinds = null;
		GroupBind groupBind = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				groupBinds = new ArrayList<GroupBind>();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("GroupBind".equals(tagName)) {
					// ��ʼ����һ���µ�BookInfo
					groupBind = new GroupBind();
				} else if ("groupNo".equals(tagName)) {
					groupBind.setGroupNo(parser.nextText());
				} else if ("meterNo".equals(tagName)) {
					groupBind.setMeterNo(parser.nextText().trim());
				} else if ("meterName".equals(tagName)) {
					groupBind.setMeterName(parser.nextText());
				} else if ("MeterType".equals(tagName)) {
					groupBind.setMeterType(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				if ("GroupBind".equals(parser.getName())) {
					groupBinds.add(groupBind);
				}
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return groupBinds;
	}

	public HrCustomerInfo parseHrCustomerInfo(InputStream in)
			throws XmlPullParserException, IOException {
		HrCustomerInfo hrCustomerInfo = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				hrCustomerInfo = new HrCustomerInfo();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("YICODE".equals(tagName)) {
					hrCustomerInfo.setYICODE(parser.nextText());
				} else if ("HUNAME".equals(tagName)) {
					hrCustomerInfo.setHUNAME(parser.nextText());
				} else if ("ADDR".equals(tagName)) {
					hrCustomerInfo.setADDR(parser.nextText());
				} else if ("OTEL".equals(tagName)) {
					hrCustomerInfo.setOTEL(parser.nextText());
				} else if ("MQBBH".equals(tagName)) {
					hrCustomerInfo.setMQBBH(parser.nextText());
				} else if ("CODE".equals(tagName)) {
					hrCustomerInfo.setCODE(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return hrCustomerInfo;
	}

	public CopyDataPhoto parseCopyDataPhoto(InputStream in)
			throws XmlPullParserException, IOException {
		CopyDataPhoto copyDataPhoto = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				copyDataPhoto = new CopyDataPhoto();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("CommunicateNo".equals(tagName)) {
					copyDataPhoto.setCommunicateNo(parser.nextText());
				} else if ("ReadState".equals(tagName)) {
					copyDataPhoto.setReadState(Integer.parseInt(parser
							.nextText()));
				} else if ("ImageName".equals(tagName)) {
					copyDataPhoto.setImageName(parser.nextText());
				} else if ("DevState".equals(tagName)) {
					copyDataPhoto.setDevState(parser.nextText());
				} else if ("DevPower".equals(tagName)) {
					copyDataPhoto.setDevPower(parser.nextText());
				} else if ("OcrRead".equals(tagName)) {
					copyDataPhoto.setOcrRead(parser.nextText());
				} else if ("OcrState".equals(tagName)) {
					copyDataPhoto.setOcrState(Integer.parseInt(parser
							.nextText()));
				} else if ("OcrTime".equals(tagName)) {
					copyDataPhoto.setOcrTime(parser.nextText());
				} else if ("ThisRead".equals(tagName)) {
					copyDataPhoto.setThisRead(parser.nextText());
				} else if ("meterName".equals(tagName)) {
					copyDataPhoto.setMeterName(parser.nextText());
				} else if ("OcrResult".equals(tagName)) {
					copyDataPhoto.setOcrResult(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				if ("CopyDataPhoto".equals(parser.getName())) {

				}
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return copyDataPhoto;
	}

	public ArrayList<CopyData> parseCopyDatas(InputStream in)
			throws XmlPullParserException, IOException {
		ArrayList<CopyData> copyDatas = null;
		CopyData copyData = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				copyDatas = new ArrayList<CopyData>();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("CopyData".equals(tagName)) {
					// ��ʼ����һ���µ�BookInfo
					copyData = new CopyData();
				} else if ("meterNo".equals(tagName)) {
					copyData.setMeterNo(parser.nextText().trim());
				} else if ("lastShow".equals(tagName)) {
					copyData.setLastShow(parser.nextText());
				} else if ("lastDosage".equals(tagName)) {
					copyData.setLastDosage(parser.nextText());
				} else if ("currentShow".equals(tagName)) {
					copyData.setCurrentShow(parser.nextText());
				} else if ("currentDosage".equals(tagName)) {
					copyData.setCurrentDosage(parser.nextText());
				} else if ("unitPrice".equals(tagName)) {
					copyData.setUnitPrice(parser.nextText());
				} else if ("printFlag".equals(tagName)) {
					copyData.setPrintFlag(Integer.parseInt(parser.nextText()));
				} else if ("meterState".equals(tagName)) {
					copyData.setMeterState(Integer.parseInt(parser.nextText()));
				} else if ("copyWay".equals(tagName)) {
					copyData.setCopyWay(parser.nextText());
				} else if ("copyState".equals(tagName)) {
//					copyData.setCopyState(Integer.parseInt(parser.nextText()));
					copyData.setCopyState(0);
				} else if ("copyTime".equals(tagName)) {
					copyData.setCopyTime(parser.nextText());
				} else if ("copyMan".equals(tagName)) {
					copyData.setCopyMan(parser.nextText());
				} else if ("isBalance".equals(tagName)) {
					copyData.setIsBalance(Integer.parseInt(parser.nextText()));
				} else if ("meterName".equals(tagName)) {
					copyData.setMeterName(parser.nextText());
				} else if ("dBm".equals(tagName)) {
					copyData.setdBm(parser.nextText());
				} else if ("elec".equals(tagName)) {
					copyData.setElec(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				if ("CopyData".equals(parser.getName())) {
					copyDatas.add(copyData);
				}
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return copyDatas;
	}

	public ArrayList<CopyDataICRF> parseCopyDataICRFs(InputStream in)
			throws XmlPullParserException, IOException {
		ArrayList<CopyDataICRF> copyDataICRFs = null;
		CopyDataICRF copyDataICRF = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				copyDataICRFs = new ArrayList<CopyDataICRF>();
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("Errors".equals(tagName)) {
					return null;
				} else if ("CopyDataICRF".equals(tagName)) {
					// ��ʼ����һ���µ�BookInfo
					copyDataICRF = new CopyDataICRF();
				} else if ("meterNo".equals(tagName)) {
					copyDataICRF.setMeterNo(parser.nextText().trim());
				} else if ("Cumulant".equals(tagName)) {
					copyDataICRF.setCumulant(parser.nextText());
				} else if ("SurplusMoney".equals(tagName)) {
					copyDataICRF.setSurplusMoney(parser.nextText());
				} else if ("OverZeroMoney".equals(tagName)) {
					copyDataICRF.setOverZeroMoney(parser.nextText());
				} else if ("BuyTimes".equals(tagName)) {
					copyDataICRF
							.setBuyTimes(Integer.parseInt(parser.nextText()));
				} else if ("OverFlowTimes".equals(tagName)) {
					copyDataICRF.setOverFlowTimes(Integer.parseInt(parser
							.nextText()));
				} else if ("MagAttTimes".equals(tagName)) {
					copyDataICRF.setMagAttTimes(Integer.parseInt(parser
							.nextText()));
				} else if ("CardAttTimes".equals(tagName)) {
					copyDataICRF.setCardAttTimes(Integer.parseInt(parser
							.nextText()));
				} else if ("MeterState".equals(tagName)) {
					copyDataICRF.setMeterState(Integer.parseInt(parser
							.nextText()));
				} else if ("StateMessage".equals(tagName)) {
					copyDataICRF.setStateMessage(parser.nextText());
				} else if ("CurrMonthTotal".equals(tagName)) {
					copyDataICRF.setCurrMonthTotal(parser.nextText());
				} else if ("Last1MonthTotal".equals(tagName)) {
					copyDataICRF.setLast1MonthTotal(parser.nextText());
				} else if ("Last2MonthTotal".equals(tagName)) {
					copyDataICRF.setLast2MonthTotal(parser.nextText());
				} else if ("Last3MonthTotal".equals(tagName)) {
					copyDataICRF.setLast3MonthTotal(parser.nextText());
				} else if ("copyWay".equals(tagName)) {
					copyDataICRF.setCopyWay(parser.nextText());
				} else if ("copyTime".equals(tagName)) {
					copyDataICRF.setCopyTime(parser.nextText());
				} else if ("copyMan".equals(tagName)) {
					copyDataICRF.setCopyMan(parser.nextText());
				} else if ("copyState".equals(tagName)) {
//					copyDataICRF.setCopyState(Integer.parseInt(parser
//							.nextText()));
					copyDataICRF.setCopyState(0);
				} else if ("meterName".equals(tagName)) {
					copyDataICRF.setMeterName(parser.nextText());
				} else if ("dBm".equals(tagName)) {
					copyDataICRF.setdBm(parser.nextText());
				} else if ("elec".equals(tagName)) {
					copyDataICRF.setElec(parser.nextText());
				} else if ("unitPrice".equals(tagName)) {
					copyDataICRF.setUnitPrice(parser.nextText());
				} else if ("accMoney".equals(tagName)) {
					copyDataICRF.setAccMoney(parser.nextText());
				} else if ("accBuyMoney".equals(tagName)) {
					copyDataICRF.setAccBuyMoney(parser.nextText());
				} else if ("currentShow".equals(tagName)) {
					copyDataICRF.setCurrentShow(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				if ("CopyDataICRF".equals(parser.getName())) {
					copyDataICRFs.add(copyDataICRF);
				}
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return copyDataICRFs;
	}

	public String parseUserName(InputStream in) throws XmlPullParserException,
			IOException {
		String userName = null;
		// ����������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		// ��������Դ
		parser.setInput(new InputStreamReader(in));
		// ��ȡ��ʼ�¼�����
		int eventType = parser.getEventType();
		// ���¼����Ͳ���END_DOCUMENTʱ��ѭ������
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// �ж��¼����Ͳ�������Ӧ����
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:// ��ʼ�����ĵ�
				break;
			case XmlPullParser.START_TAG:// ��ʼ������ǩ
				String tagName = parser.getName();// ��ȡ��ǩ��
				// �жϱ�ǩ������ִ����Ӧ����
				if ("error".equals(tagName)) {
					String u = parser.nextText();
					if (u.equals("ok")) {
						userName = u;
					}
				}
				break;
			case XmlPullParser.END_TAG:// ������ǩ����
				break;
			}
			// ��������һ�¼�
			eventType = parser.next();
		}
		return userName;
	}

}
