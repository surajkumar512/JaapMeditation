package com.bajajtechllc.jaapmeditation;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    RadioGroup radioGroup;
    Button play;
    Button stopButton;
    EditText textViewNoOfTimes;
    TextView textViewTotalNoOfTimes;

    int counter = 0;
    int count = 108;
    int noOfTimes = 108;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupCount);
        play = (Button) findViewById(R.id.playButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        textViewNoOfTimes = (EditText) findViewById(R.id.textViewNoOfTimes);
        textViewTotalNoOfTimes = (TextView) findViewById(R.id.textViewTotalNoOfTimes);

        /**
         * Set cursor to end of text in edittext when user clicks Next on Keyboard.
         */
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ((EditText) view).setSelection(((EditText) view).getText().length());
                }
            }
        };

        textViewNoOfTimes.setOnFocusChangeListener(onFocusChangeListener);


        textViewNoOfTimes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.d("JAP : setOnKeyListener", textViewNoOfTimes.getText().toString());
                return false;
            }
        });

        textViewNoOfTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int viewCount) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateNumberOfTimes();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    counter = 0;
                    setCounter(counter + " / " + noOfTimes);
                    mediaPlayer.release();
                }
                isPlaying = false;
                stopButton.setVisibility(View.INVISIBLE);
                play.setBackgroundResource(R.drawable.play);
            }
        });
        stopButton.setVisibility(View.INVISIBLE);

        radioGroup.setEnabled(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                RadioButton selRadioButton = (RadioButton) findViewById(radioButton.getId());
                //Log.d("TAG", "radioGroup.getCheckedRadioButtonId(): " + index + " - " + selRadioButton.getText());
                count = Integer.parseInt(selRadioButton.getText().toString());
                calculateNumberOfTimes();


            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    isPlaying = false;
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        setCounter(counter + " / " + noOfTimes);
                        mediaPlayer.release();
                    }
                    play.setBackgroundResource(R.drawable.play);
                    stopButton.setVisibility(View.VISIBLE);

                } else {
                    isPlaying = true;
                    mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.ommantra);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            counter++;
                            //Log.d("TAG", "onCompletion: " + counter + " / " + noOfTimes);
                            setCounter(counter + " / " + noOfTimes);
                            if (counter < count) {
                                mp.seekTo(0);
                                mp.start();
                            } else {
                                mp.stop();
                                counter = 0;
                            }
                        }
                    });
                    mediaPlayer.start(); // no need to call prepare(); create() does that for you
                    //Log.d("TAG", "mediaPlayer.getDuration(): " + mediaPlayer.getDuration());

                    play.setBackgroundResource(R.drawable.pause);
                    stopButton.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void calculateNumberOfTimes(){
        noOfTimes = count;
        String strValue =textViewNoOfTimes.getText().toString();
        if(strValue.toString()!=null && strValue.toString().trim().length() > 0){
            noOfTimes = Integer.parseInt(strValue.toString()) * count;
            //Log.d("JAP : noOfTimes = ", String.valueOf(noOfTimes));
        }
        textViewTotalNoOfTimes.setText(String.valueOf(noOfTimes));
        textViewTotalNoOfTimes.refreshDrawableState();
        setCounter(counter + " / " + noOfTimes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCounter(String value) {
        //Log.d("TAG", "setCounter: " + value);
        TextView cuttentCounter = (TextView) findViewById(R.id.textViewCurrentCounterValue);
        cuttentCounter.setText(value);
        cuttentCounter.refreshDrawableState();
    }

}
