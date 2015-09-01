package cn.com.chess;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

import static cn.com.chess.ViewConstant.height;
import static cn.com.chess.ViewConstant.initChessViewFinal;
import static cn.com.chess.ViewConstant.isnoPlaySound;
import static cn.com.chess.ViewConstant.sXtart;
import static cn.com.chess.ViewConstant.sYtart;
import static cn.com.chess.ViewConstant.width;
import static cn.com.chess.ViewConstant.xZoom;
import static cn.com.chess.ViewConstant.yZoom;


enum WhichView {WELCOME_VIEW, GAME_VIEW}

/**start **/
public class Chess_DJB_Activity extends Activity {
    GameView gameView;
    WhichView wv;
    WelcomeView wvv;
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap; ////声音池中声音ID与自定义声音ID的Map
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    wv = WhichView.GAME_VIEW;
                    goToGameView();

                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);//游戏过程中只允许多媒体音量,而不允许通化音量
        initPm();
        initSound();
        goToWelcomeView();
    }

    public void initPm() {
        ////获取屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int tempHeight = (int) (height = dm.heightPixels);
        int tempWidth = (int) (width = dm.widthPixels);

        if (tempHeight > tempWidth) {
            height = tempHeight;
            width = tempWidth;
        } else {
            height = tempWidth;
            width = tempHeight;
        }
        float zoomx = width / 480;
        float zoomy = height / 800;
        if (zoomx > zoomy) {
            xZoom = yZoom = zoomy;

        } else {
            xZoom = yZoom = zoomx;
        }
        sXtart = (width - 480 * xZoom) / 2;
        sYtart = (height - 800 * yZoom) / 2;
        initChessViewFinal();
    }

    public void goToGameView() {
        gameView = new GameView(Chess_DJB_Activity.this);

        setContentView(gameView);
        wv = WhichView.GAME_VIEW;
    }


    public void goToWelcomeView() {
        if (wvv == null) {
            wvv = new WelcomeView(this);
        }
        setContentView(wvv);
        wv = WhichView.WELCOME_VIEW;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode != 4) {
            return true;
        }
        if (wv == WhichView.WELCOME_VIEW) {
            return true;
        }

        if (wv == WhichView.GAME_VIEW) {
            gameView.threadFlag = false;
            System.exit(0);
            return true;
        }
        System.exit(0);
        return true;
    }

    public void initSound() {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();

        soundPoolMap.put(1, soundPool.load(this, R.raw.noxiaqi, 1));
        soundPoolMap.put(2, soundPool.load(this, R.raw.dong, 1)); //玩家走棋
        soundPoolMap.put(4, soundPool.load(this, R.raw.win, 1)); //赢了
        soundPoolMap.put(5, soundPool.load(this, R.raw.loss, 1)); //输了
    }

    public void playSound(int sound, int loop) {
        if (!isnoPlaySound) {
            return;
        }
        AudioManager mgr = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }
}