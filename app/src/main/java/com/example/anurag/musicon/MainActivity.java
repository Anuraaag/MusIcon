package com.example.anurag.musicon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

 //   final Handler handler_interact = new Handler();

    Runnable r1 = null;
    Runnable r2 = null;
    Runnable r3 = null;
    Runnable r4 = null;
    Runnable r5 = null;
    Runnable r6 = null;
    int status = 1;


    ImageButton play, pause, stop, stopDisabled;
    SeekBar volumeSeek, songSeek;
    TextView start, end;

    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        volumeSeek = findViewById(R.id.volumeSeek);
        songSeek = findViewById(R.id.songSeek);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);

        stopDisabled = findViewById(R.id.stopDisabled);
        stopDisabled.setClickable(false);

        pause.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);

        audioManager = (AudioManager) getApplicationContext().getSystemService(MainActivity.this.AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.test);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        stop.setClickable(false);

        volumeSeek.setMax(maxVolume);
        volumeSeek.setProgress(currentVolume);
        songSeek.setMax(mediaPlayer.getDuration()/1000);


        /* ********************                    volume control start                   ********************** */
        volumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });
        /* ********************                    volume control start                   ********************** */



        final Handler handler = new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null && mediaPlayer.isPlaying() ){

                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    songSeek.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });


        /* ********************                song seek control start                   ********************** */
        songSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);

//                    if (progress >= mediaPlayer.getDuration()/1000)
//                    {
//                        Toast.makeText(MainActivity.this, "I am in", Toast.LENGTH_SHORT).show();
//                        pause.setVisibility(View.GONE);
//                        play.setVisibility(View.VISIBLE);
//                        stop.setClickable(false);
//                        if (mediaPlayer != null) {
//                            mediaPlayer.stop();
//                            mediaPlayer.release();
//                            mediaPlayer = null;
//                        }
//                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.test);
//                        status = 0;
//                    }
                }
            }
        });
        /* ********************                song seek control end                 ********************** */


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(MainActivity.this, mp.toString(), Toast.LENGTH_SHORT).show();
                //Log.i("Hahahai","finished");
                hasCompleted();

            }
        });

//        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(MediaPlayer mediaPlayer) {
//                mediaPlayer.seekTo(songSeek.getProgress() * 1000);
//            }
//        });


        /* ********************                  play event start                  ********************** */
        play.setOnClickListener(new View.OnClickListener() {
            String urlString = "http://mysuperwebsite";
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
            PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
            Notification notification;

            public void onClick(View v) {
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");

//                start.setText(mediaPlayer.getCurrentPosition());
//                end.setText(mediaPlayer.getDuration());
                stopDisabled.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
                notification = new Notification.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
                        .setContentTitle("Music Playing")
                        .setContentText("Playing music")
                        .addAction(R.drawable.ic_play_arrow_black_24dp,"PLAY",pIntent)

                        .getNotification();
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0,notification);

                /* colors start*/

                status = 1;

                r1 = new Runnable() {
                    @Override
                    public void run() {
                        songSeek.setBackgroundColor(getResources().getColor(R.color.c1));
                        if(status == 1) {
                            handler.postDelayed(r2, 100);
                        }
                    }
                };

                r2 = new Runnable() {
                    @Override
                    public void run() {
                        songSeek.setBackgroundColor(getResources().getColor(R.color.c2));
                        if(status == 1) {
                            handler.postDelayed(r3,100);
                        }
                    }
                };

                r3 = new Runnable() {
                    @Override
                    public void run() {
                        songSeek.setBackgroundColor(getResources().getColor(R.color.c3));
                        if(status == 1) {
                            handler.postDelayed(r4,100);
                        }
                    }
                };

                r4 = new Runnable() {
                    @Override
                    public void run() {
                        songSeek.setBackgroundColor(getResources().getColor(R.color.c4));
                        if(status == 1) {
                            handler.postDelayed(r5,100);
                        }
                    }
                };

                r5 = new Runnable() {
                    @Override
                    public void run() {
                        songSeek.setBackgroundColor(getResources().getColor(R.color.c5));
                        if(status == 1) {
                            handler.postDelayed(r6,100);
                        }
                    }
                };

                r6 = new Runnable() {
                    @Override
                    public void run() {
                        songSeek.setBackgroundColor(getResources().getColor(R.color.c6));
                        if(status == 1) {
                            handler.postDelayed(r1,100);
                        }
                    }
                };
                handler.postDelayed(r1,0);

                /* color end*/
            }
        });
        /* ********************                 play event end                  ********************** */



        /* ********************                 pause event start                  ********************** */
        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                status = 0;
            }
        });
        /* ********************                pause event end                  ********************** */



        /* ********************                 stop event start                  ********************** */
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasCompleted();
            }
        });
        /* ********************               stop event end                ********************** */
    }

    public void hasCompleted()
    {
        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
        stop.setVisibility(View.GONE);
        stopDisabled.setVisibility(View.VISIBLE);
        songSeek.setProgress(0);
        if (mediaPlayer != null) {

            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
// mediaPlayer.release();
//  mediaPlayer = null;
        }
//      mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.test);
        status = 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}