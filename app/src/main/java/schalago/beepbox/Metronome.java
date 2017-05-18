package schalago.beepbox;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;

public class Metronome implements Runnable {

    private int bpm;
    private long interval;
    private boolean isPlaying;

    private SoundPool soundPool;
    private int soundId;
    private Handler handler;
    private Context context;

    private final int MS_PER_MINUTE = 60000;
    private final int DEFAULT_BPM = 90;
    private final int MIN_BPM = 10;
    private final int MAX_BPM = 300;



    public Metronome(Context context){
        this.context = context;
        createSoundPool();
        loadSound();
        handler = new Handler();
        isPlaying = false;
        setBpm(DEFAULT_BPM);
    }

    public void play() {
        handler.post(this);
        isPlaying = true;
    }

    public void pause() {
        handler.removeCallbacks(this);
        isPlaying = false;
    }

    public void restart() {
        if (isPlaying) {
            pause();
            play();
        }
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
        interval = (long) MS_PER_MINUTE / bpm;
    }

    public void increaseBpm() {
        if (bpm < MAX_BPM) {
            setBpm(++bpm);
        }
    }

    public void decreaseBpm() {
        if (bpm > MIN_BPM) {
            setBpm(--bpm);
        }
    }

    @Override
    public void run() {
        if (isPlaying) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
            handler.postDelayed(this, interval);
        }
    }

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .build();
        } else soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    private void loadSound() {
        soundId = soundPool.load(context, R.raw.default_beep, 1);
    }
}
