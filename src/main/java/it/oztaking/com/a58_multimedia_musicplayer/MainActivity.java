package it.oztaking.com.a58_multimedia_musicplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static MediaPlayer mediaPlayer;
    private static SeekBar sbar;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle data = msg.getData();
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");

            sbar.setMax(duration);
            sbar.setProgress(currentPosition);
            sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_pause = (Button) findViewById(R.id.bt_pause);
        Button bt_play = (Button) findViewById(R.id.bt_play);
        Button bt_replay = (Button) findViewById(R.id.bt_replay);
        bt_pause.setOnClickListener(this);
        bt_play.setOnClickListener(this);
        bt_replay.setOnClickListener(this);

        sbar = (SeekBar) findViewById(R.id.sbMusic);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_play:
                PlayMusic();
                updateSeekBar();
                break;
            case R.id.bt_pause:
                PauseMusic();
                break;
            case R.id.bt_replay:
                ReplayMusic();
                break;
            default:
                break;
        }

    }

    //播放音乐
    public void PlayMusic() {
        mediaPlayer = new MediaPlayer();
        try {
            if (!mediaPlayer.isPlaying()){
                mediaPlayer.reset();
                mediaPlayer.setDataSource("/storage/sdcard/xpg.mp3");
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //暂停播放音乐
    public void PauseMusic() {
        mediaPlayer.pause();

    }

    //再次播放音乐
    public void ReplayMusic() {
        mediaPlayer.start();

    }

    public void updateSeekBar() {
        final int duration = mediaPlayer.getDuration();
        final Timer timer = new Timer();

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);
                msg.setData(bundle);
                MainActivity.handler.sendMessage(msg);
            }
        };

        timer.schedule(timerTask, 200, 1000);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "歌曲播放完了", Toast.LENGTH_SHORT).show();
                timer.cancel();
                timerTask.cancel();
            }
        });

    }
}
