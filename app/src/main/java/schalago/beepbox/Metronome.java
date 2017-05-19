package schalago.beepbox;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.Random;

public class Metronome implements Runnable {

    private int bpm;
    private long interval;
    private int beatsPerMeasure;
    private int beat;
    private float accentRate;
    private boolean isPlaying;

    private SoundPool soundPool;
    private int soundId;
    private int default_soundId;


    private int[] secret_soundIds;
    private boolean random_sounds;
    private Random randomGenerator;

    private Handler handler;
    private Context context;

    private final int MS_PER_MINUTE = 60000;
    private final int DEFAULT_BPM = 90;
    private final int MIN_BPM = 10;
    private final int MAX_BPM = 300;
    private final int DEFAULT_BEATS_PER_MEASURE = 4;



    public Metronome(Context context){
        this.context = context;
        createSoundPool();
        default_soundId = soundPool.load(context, R.raw.default_beep, 1);

        random_sounds = false;
        randomGenerator = new Random();
        loadSecretSounds();

        handler = new Handler();
        isPlaying = false;
        setBeatsPerMeasure(DEFAULT_BEATS_PER_MEASURE);
        beat = 0;
        accentRate = 1;
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

    public void setTapTempo(long timeBetweenTaps) {
        long minMsPerBeat = MS_PER_MINUTE / MAX_BPM;
        long maxMsPerBeat = MS_PER_MINUTE / MIN_BPM;
        Log.d("TAG", "setTapTempo() called");
        if (timeBetweenTaps > minMsPerBeat && timeBetweenTaps < maxMsPerBeat ){
            Log.d("TAG", "Good tap!");
            Log.d("TAG", "Setting bpm to: " + (int) (MS_PER_MINUTE / timeBetweenTaps));
            setBpm((int)(MS_PER_MINUTE / timeBetweenTaps));
        }
    }

    @Override
    public void run() {
        if (isPlaying) {
            soundId = getSoundId();
            handler.postDelayed(this, interval);
            updateBeatAndAccent();
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, accentRate);
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

    public void setBeatsPerMeasure(int beatsPerMeasure) {
        this.beatsPerMeasure = beatsPerMeasure;
    }

    public void updateBeatAndAccent() {
        if (beat == 0 && beatsPerMeasure != 0) {
            accentRate = 2.0f;
        }
        else {
            accentRate = 1.0f;
        }

        beat++;

        if (beat > beatsPerMeasure - 1) {
            beat = 0;
        }
    }

    public int getSoundId() {
        if (!random_sounds) {
            return default_soundId;
        }
        else {
            int i = randomGenerator.nextInt(15);
            return secret_soundIds[i];
        }

    }

    public void toggleRandomSounds() {
        if (!random_sounds) {
            random_sounds = true;
        }
        else {
            random_sounds = false;
        }
    }

    public void loadSecretSounds() {
        secret_soundIds = new int[15];
        secret_soundIds[0] = soundPool.load(context, R.raw.secret_1, 1);
        secret_soundIds[1] = soundPool.load(context, R.raw.secret_2, 1);
        secret_soundIds[2] = soundPool.load(context, R.raw.secret_3, 1);
        secret_soundIds[3] = soundPool.load(context, R.raw.secret_4, 1);
        secret_soundIds[4] = soundPool.load(context, R.raw.secret_5, 1);
        secret_soundIds[5] = soundPool.load(context, R.raw.secret_6, 1);
        secret_soundIds[6] = soundPool.load(context, R.raw.secret_7, 1);
        secret_soundIds[7] = soundPool.load(context, R.raw.secret_8, 1);
        secret_soundIds[8] = soundPool.load(context, R.raw.secret_9, 1);
        secret_soundIds[9] = soundPool.load(context, R.raw.secret_10, 1);
        secret_soundIds[10] = soundPool.load(context, R.raw.secret_11, 1);
        secret_soundIds[11] = soundPool.load(context, R.raw.secret_12, 1);
        secret_soundIds[12] = soundPool.load(context, R.raw.secret_13, 1);
        secret_soundIds[13] = soundPool.load(context, R.raw.secret_14, 1);
        secret_soundIds[14] = soundPool.load(context, R.raw.secret_15, 1);
    }
}
