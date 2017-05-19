package schalago.beepbox;

import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.sql.Time;

public class MetronomeActivity extends AppCompatActivity {

    private TextView bpmText;
    private TextView italianTempoText;
    private Button minusButton;
    private Button plusButton;
    private SeekBar bpmSeekBar;
    private final int bpmSeekBarCorrection = 10;
    private ToggleButton toggleButton;

    private Boolean tempoTappedAlready = false;
    private long tapTime;

    private Handler handler;
    private Boolean minusIncrement = false;
    private Boolean plusIncrement = false;

    private Metronome metronome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);

        handler = new Handler();

        TextView bpmText = (TextView)findViewById(R.id.bpmText);
        final TextView italianTempoText = (TextView)findViewById(R.id.italianTempoText);
        Button minusButton = (Button)findViewById(R.id.minusButton);
        Button plusButton = (Button)findViewById(R.id.plusButton);
        Button tapTempoButton = (Button)findViewById(R.id.tapTempoButton);
        Button secretButton = (Button)findViewById(R.id.secretButton);
        SeekBar bpmSeekBar = (SeekBar)findViewById(R.id.bpmSeekBar);
        final ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        String[] arraySpinner = new String[] {"No first beat accent", "2 beats per measure",
                "3 beats per measure", "4 beats per measure", "5 beats per measure",
                "6 beats per measure", "7 beats per measure", "8 beats per measure",
                "9 beats per measure", "10 beats per measure", "11 beats per measure",
                "12 beats per measure" };
        Spinner timeSignatureSpinner = (Spinner) findViewById(R.id.timeSignatureSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSignatureSpinner.setAdapter(adapter);

        metronome = new Metronome(this);


        minusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                metronome.decreaseBpm();
                setBpmViews(metronome.getBpm());
                setBpmSeekBar(metronome.getBpm());
            }
        });

        final Runnable minusRunnable = new Runnable() {
            @Override
            public void run() {
                if (minusIncrement) {
                    metronome.decreaseBpm();
                    setBpmViews(metronome.getBpm());
                    setBpmSeekBar(metronome.getBpm());
                    handler.postDelayed(this, 100);
                }
            }
        };

        minusButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                minusIncrement = true;
                handler.post(minusRunnable);
                return false;
            }
        });

        minusButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && minusIncrement ){
                    minusIncrement = false;
                }
                return false;
            }
        });


        plusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                metronome.increaseBpm();
                setBpmViews(metronome.getBpm());
                setBpmSeekBar(metronome.getBpm());
            }
        });

        final Runnable plusRunnable = new Runnable() {
            @Override
            public void run() {
                if (plusIncrement) {
                    metronome.increaseBpm();
                    setBpmViews(metronome.getBpm());
                    setBpmSeekBar(metronome.getBpm());
                    handler.postDelayed(this, 100);
                }
            }
        };

        plusButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                plusIncrement = true;
                handler.post(plusRunnable);
                return false;
            }
        });

        plusButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && plusIncrement ){
                    plusIncrement = false;
                }
                return false;
            }
        });

        tapTempoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (!tempoTappedAlready) {
                    tapTime = System.currentTimeMillis();
                    tempoTappedAlready = true;
                }
                else {
                    metronome.setTapTempo(System.currentTimeMillis() - tapTime);
                    setBpmViews(metronome.getBpm());
                    setBpmSeekBar(metronome.getBpm());
                    tempoTappedAlready = false;
                }
            }
        });



        secretButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                metronome.toggleRandomSounds();
            }
        });

        bpmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                metronome.setBpm(i + bpmSeekBarCorrection);
                setBpmViews(i + bpmSeekBarCorrection);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                metronome.restart();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(toggleButton.isChecked()){
                    metronome.play();
                }
                else {
                    metronome.pause();
                }
            }
        });

        timeSignatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selection = parentView.getItemAtPosition(position).toString();
                int beatsPerMeasure;

                switch (selection) {
                    case "2 beats per measure":
                        beatsPerMeasure = 2;
                        break;
                    case "3 beats per measure":
                        beatsPerMeasure = 3;
                        break;
                    case "4 beats per measure":
                        beatsPerMeasure = 4;
                        break;
                    case "5 beats per measure":
                        beatsPerMeasure = 5;
                        break;
                    case "6 beats per measure":
                        beatsPerMeasure = 6;
                        break;
                    case "7 beats per measure":
                        beatsPerMeasure = 7;
                        break;
                    case "8 beats per measure":
                        beatsPerMeasure = 8;
                        break;
                    case "9 beats per measure":
                        beatsPerMeasure = 9;
                        break;
                    case "10 beats per measure":
                        beatsPerMeasure = 10;
                        break;
                    case "11 beats per measure":
                        beatsPerMeasure = 11;
                        break;
                    case "12 beats per measure":
                        beatsPerMeasure = 12;
                        break;
                    default:
                        beatsPerMeasure = 0;
                }

                metronome.setBeatsPerMeasure(beatsPerMeasure);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void setBpmViews(int bpm) {
        TextView bpmText = (TextView)findViewById(R.id.bpmText);
        bpmText.setText(bpm + " bpm");

        TextView italianTempoText = (TextView)findViewById(R.id.italianTempoText);
        italianTempoText.setText(tempoMarking(bpm));
    }

    public void setBpmSeekBar(int bpm) {
        SeekBar bpmSeekBar = (SeekBar)findViewById(R.id.bpmSeekBar);
        bpmSeekBar.setProgress(bpm - bpmSeekBarCorrection);
    }


    public String tempoMarking(int bpm) {
        String tempo;
        if (bpm > 0 && bpm < 40) {
            tempo = "Lento assai";
        }
        else if (bpm > 39 && bpm < 60) {
            tempo = "Largo";
        }
        else if (bpm > 59 && bpm < 66) {
            tempo = "Larghetto";
        }
        else if (bpm > 65 && bpm < 76) {
            tempo = "Adagio";
        }
        else if (bpm > 75 && bpm < 108) {
            tempo = "Andante";
        }
        else if (bpm > 107 && bpm < 120) {
            tempo = "Moderato";
        }
        else if (bpm > 119 && bpm < 140) {
            tempo = "Allegro";
        }
        else if (bpm > 139 && bpm < 168) {
            tempo = "Vivace";
        }
        else if (bpm > 167 && bpm < 200) {
            tempo = "Presto";
        }
        else {
            tempo = "Prestissimo";
        }

        return tempo;
    }
}
