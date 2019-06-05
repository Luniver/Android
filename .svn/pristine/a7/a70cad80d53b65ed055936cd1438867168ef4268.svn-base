package hrst.sczd.utils;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * 
 * @author glj
 * 2016-08-06
 * 音频播放工具
 *
 */
public class PlayRingtone extends Thread {
	private static final String TAG = "PlayRingtone";
	private MediaPlayer mMediaPlayer;
	private Context mContext;
	private int mResID;
	private boolean mRunFlag;

	public PlayRingtone(Context context) {
		mMediaPlayer = new MediaPlayer();
		mContext = context;
	}

	public PlayRingtone(Context context, int resID) {
		mMediaPlayer = new MediaPlayer();
		mContext = context;
		mResID = resID;
		mMediaPlayer = MediaPlayer.create(mContext, mResID);
		mRunFlag = true;
	}

	@Override
	public void run() {
		if (mRunFlag) {
			try {
				Log.e(TAG, "PlayRingtone is running!");
				if (mMediaPlayer != null)
					mMediaPlayer.stop();
				final AudioManager audioManager = (AudioManager) mContext
						.getSystemService(Context.AUDIO_SERVICE);
				if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					mMediaPlayer.setLooping(true);
					mMediaPlayer.prepare();
				}
				if( mMediaPlayer== null) {
					mMediaPlayer = new MediaPlayer();
				}
				mMediaPlayer.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopAlertRing() {
		mRunFlag = false;
		if(mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

}
