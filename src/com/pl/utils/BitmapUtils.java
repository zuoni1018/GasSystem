package com.pl.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {
	/**
	 * ��ָ���ļ�����ͼƬ���ڴ�
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap loadBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * ���ֽ������а�ָ����߱����ݺ�ȵ�����¼���ͼƬ���ڴ�
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] data, int width, int height) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int x = opts.outWidth / width;
		int y = opts.outHeight / height;
		opts.inSampleSize = x > y ? x : y;
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	/**
	 * ���ڴ��е�λͼ���󱣴浽ָ�����ļ�
	 * 
	 * @param bm
	 * @param file
	 * @throws IOException
	 */
	public static void save(Bitmap bm, File file) throws IOException {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}

		bm.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
	}

	/**
	 * ͼƬ��С
	 * 
	 * @param bitmap
	 * @return
	 */
	private static Bitmap Changesmall(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.97f, 0.97f); // ���Ϳ�Ŵ���С�ı���
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

}
