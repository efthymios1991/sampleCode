package eu.applogic.onlinealb.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import eu.applogic.onlinealb.Adapters.RadiosAdapter;
import eu.applogic.onlinealb.HelperClasses.AppConfig;
import eu.applogic.onlinealb.HelperClasses.CircleTransform;
import eu.applogic.onlinealb.Objects.RadioStationObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/6/2017.
 */

public class RadioPlayerActivity extends AppCompatActivity implements OnClickListener, MediaPlayer.OnCompletionListener {

    private Toolbar toolbar;

    private ProgressBar playSeekBar;

    private ImageView buttonPlay;

    private ImageView buttonStopPlay;

    private ImageView radioIcon;

    private MediaPlayer player;

    private RadioStationObject selectedRadio;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSelectedRadio();

        setContentView(R.layout.radio_player_activity);

        setToolbar();

        initializeUIElements();

        initializeMediaPlayer();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(selectedRadio.getName());
    }

    private void getSelectedRadio() {
        Intent i = getIntent();
        selectedRadio = (RadioStationObject) i.getSerializableExtra(AppConfig.radio);

        if(selectedRadio == null){
            Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        }
    }

    private void initializeUIElements() {

        playSeekBar = (ProgressBar) findViewById(R.id.progressBar1);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        buttonPlay = (ImageView) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (ImageView) findViewById(R.id.buttonStopPlay);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(this);

        radioIcon = (ImageView) findViewById(R.id.radio_icon);
        applyProfilePicture(radioIcon, selectedRadio);
    }

    public static String getStationImage(String imageName) {
        return "http://mobapplications.net/logos_2016/" + imageName + ".png";
    }

    private void applyProfilePicture(ImageView imageView, RadioStationObject radio) {
        if (!TextUtils.isEmpty(radio.getImage())) {
            Glide.with(this).load(getStationImage(radio.getImage()))
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            imageView.setColorFilter(null);
        }
    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            startPlaying();
        } else if (v == buttonStopPlay) {
            stopPlaying();
        }
    }

    private void startPlaying() {
        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);

        player.prepareAsync();

        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
        playSeekBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });

        try {
            player.setDataSource(selectedRadio.getStreaming1URL());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
