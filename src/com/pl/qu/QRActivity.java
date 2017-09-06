package com.pl.qu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pl.gassystem.R;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class QRActivity extends Activity implements SurfaceHolder.Callback {
    //private static String TAG = "QRSCAN";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView surface_view;
    private ImageScanner scanner;
    private Handler autoFocusHandler;
    private AsyncDecode asyncDecode;
    private boolean playBeep = true;
    //private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    //	private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    private FinderView finder_view;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_zbar_finder);
        init();
        // ��ʼ����������
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        // ����ֻ�����ģʽ����
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        // ��ʼ����??
        initBeepSound();
    }

    /**
     * ��ʼ����??
     */
    private void initBeepSound() {
//		if (playBeep && mediaPlayer == null) {
//			setVolumeControlStream(AudioManager.STREAM_MUSIC);
//			mediaPlayer = new MediaPlayer();
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			mediaPlayer.setOnCompletionListener(beepListener);
//			AssetFileDescriptor file = getResources().openRawResourceFd(
//					R.raw.beep);
//			try {
//				mediaPlayer.setDataSource(file.getFileDescriptor(),
//						file.getStartOffset(), file.getLength());
//				file.close();
//				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//				mediaPlayer.prepare();
//			} catch (IOException e) {
//				mediaPlayer = null;
//			}
//		}
        soundPool= new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
        soundPool.load(this,R.raw.scan,1);
    }

    private void init() {
        surface_view = (SurfaceView) findViewById(R.id.surface_view);
        finder_view = (FinderView) findViewById(R.id.finder_view);
        mHolder = surface_view.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        autoFocusHandler = new Handler();
        asyncDecode = new AsyncDecode();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            Log.d("DBG", "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * Ԥ������
     */
    PreviewCallback previewCallback = new PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (asyncDecode.isStoped()) {
                //				Camera.Parameters parameters = camera.getParameters();
                //				Size size = parameters.getPreviewSize();
                //				Image barcode = new Image(size.width, size.height, "Y800");
                //				barcode.setData(data);
                //				asyncDecode = new AsyncDecode();
                //				asyncDecode.execute(barcode);
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();
                //ͼƬ�Ǳ���ת??90�ȵ�
                Image source = new Image(size.width, size.height, "Y800");
                Rect scanImageRect = finder_view.getScanImageRect(size.height, size.width);
                //ͼƬ��ת??90�ȣ���ɨ����TOP��Ϊleft�ü�
                source.setCrop(scanImageRect.top, scanImageRect.left, scanImageRect.bottom, scanImageRect.right);
                source.setData(data);
                asyncDecode = new AsyncDecode();
                asyncDecode.execute(source);

            }
        }
    };

    private class AsyncDecode extends AsyncTask<Image, Void, Void> {
        private boolean stoped = true;
        private String str = "";

        @Override
        protected Void doInBackground(Image... params) {
            stoped = false;
            StringBuilder sb = new StringBuilder();
            Image barcode = params[0];
            int result = scanner.scanImage(barcode);
            if (result != 0) {
                //				mCamera.setPreviewCallback(null);
                //				mCamera.stopPreview();
                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    switch (sym.getType()) {
                        case Symbol.CODABAR:
                            //Log.d(TAG, "����??  " + sym.getData());
                            //����??
                            sb.append(sym.getData());
                            break;
                        case Symbol.CODE128:
                            //128�����ʽ��ά??
                            //Log.d(TAG, "128�����ʽ��ά??:  " + sym.getData());
                            sb.append(sym.getData());
                            break;
                        case Symbol.QRCODE:
                            //QR���ά��
                            //Log.d(TAG, "QR���ά��  :" + sym.getData());
                            sb.append(sym.getData());
                            break;
                        case Symbol.ISBN10:
                            //ISBN10ͼ���ѯ
                            //Log.d(TAG, "ISBN10ͼ���ѯ  :   " + sym.getData());
                            sb.append(sym.getData());
                            break;
                        case Symbol.ISBN13:
                            //ISBN13ͼ���ѯ
                            //Log.d(TAG, "ISBN13ͼ���ѯ   : " + sym.getData());
                            sb.append(sym.getData());
                            break;
                        case Symbol.NONE:
                            //Log.d(TAG, "δ֪   : " + sym.getData());
                            sb.append(sym.getData());
                            break;
                        default:
                            //Log.d(TAG, "����:   " + sym.getData());
                            sb.append(sym.getData());
                            break;
                    }
                }
            }
            str = sb.toString();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            stoped = true;
            if (null == str || str.equals("")) {
            } else {
                Intent intent = new Intent();
                intent.putExtra("Code", str);
                setResult(RESULT_OK, intent);
                playBeepSoundAndVibrate();// �����������񶯲�����
                finish();
            }
        }

        public boolean isStoped() {
            return stoped;
        }
    }

    /**
     * �Զ��Խ��ص�
     */
    AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    //�Զ��Խ�
    private Runnable doAutoFocus = new Runnable() {
        @Override
        public void run() {
            if (null == mCamera || null == autoFocusCallback) {
                return;
            }
            mCamera.autoFocus(autoFocusCallback);
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * ������������??
     */
    private void playBeepSoundAndVibrate() {
//		if (playBeep && mediaPlayer != null) {
//			mediaPlayer.start();
//		}
        if(playBeep){
            soundPool.play(1,1, 1, 0, 0, 1);
        }
        // ����
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
    }

//	private final OnCompletionListener beepListener = new OnCompletionListener() {
//		public void onCompletion(MediaPlayer mediaPlayer) {
//			mediaPlayer.seekTo(0);
//		}
//	};
}
