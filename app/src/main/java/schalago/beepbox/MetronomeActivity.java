package schalago.beepbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MetronomeActivity extends AppCompatActivity {

    final int bpmSeekBarCorrection = 10;

    private TextView bpmText = (TextView)findViewById(R.id.bpmText);
    private TextView italianTempoText = (TextView)findViewById(R.id.italianTempoText);
    private Button minusButton;
    private Button plusButton;
    private SeekBar bpmSeekBar;
    private ToggleButton toggleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);

        TextView bpmText = (TextView)findViewById(R.id.bpmText);
        TextView italianTempoText = (TextView)findViewById(R.id.italianTempoText);
        Button minusButton = (Button)findViewById(R.id.minusButton);
        Button plusButton = (Button)findViewById(R.id.plusButton);
        SeekBar bpmSeekBar = (SeekBar)findViewById(R.id.bpmSeekBar);
        final ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        final Metronome metronome = new Metronome();

        minusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                metronome.decreaseBpm();
                setBpmViews(metronome.getBpm());
                setBpmSeekBar(metronome.getBpm());
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                metronome.increaseBpm();
                setBpmViews(metronome.getBpm());
                setBpmSeekBar(metronome.getBpm());
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
