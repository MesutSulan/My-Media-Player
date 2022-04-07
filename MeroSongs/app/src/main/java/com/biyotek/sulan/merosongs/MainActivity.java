package com.biyotek.sulan.merosongs;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {


    ArrayList ar;
    Button b,bnext,bpause,bbefore,setringtone;
    private MediaPlayer m = null;
    String[] s = {"Olabilir ","Maldiven","ENO feat MERO FERRARİ", "JA JA","BALLER LOS","HOBBY HOBBY","WOLKE 10 ",
            "ALLE JAGEN MICH "," YA HERO YA MERO","AUF DEM WEG","HOPS"," INTRO","TRAUME WERDEN WAHR"};
    int[] i = {R.raw.olab,R.raw.mald,R.raw.en, R.raw.ja, R.raw.ball,R.raw.ho,R.raw.wt,R.raw.all,R.raw.yah,R.raw.auf,R.raw.hops,R.raw.intr,
    R.raw.traum};


    int say = 0;
    int reklam = 0;
    ListView liste;
    SeekBar songSeekbar;
    Thread updateseekBar;
    TextView tbas;
    private AdView adreklam;
    private InterstitialAd full_iki;

    String exStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String path=(exStoragePath +"/media/Biyotek.company/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbas=findViewById(R.id.tvbaslik);
        //banner();
        //full();
        ar=new ArrayList();
        ar.clear();

        for (say=0;say<(s.length-1);say++) {
            ar.add(say+1+"-)"+s[say]);
        }



        b = findViewById(R.id.bshare);
        setringtone = findViewById(R.id.set);
        bnext = findViewById(R.id.bnext);
        bpause = findViewById(R.id.bpause);
        bbefore = findViewById(R.id.bbefore);




        songSeekbar = findViewById(R.id.seekBar);
        liste = findViewById(R.id.l);



        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ar);
        liste.setAdapter(a);

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                say = position;
                reklam++;
                //full();
                if (m != null) m.reset();
                m = MediaPlayer.create(getApplicationContext(), i[say]);

                m.start();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        m.start();
                    }
                });
                tbas.setText(s[say]);


                if (m.isPlaying()) {
                    bpause.setBackgroundResource(R.drawable.ac);
                }
                seekbar();

//                if (reklam==2) {
//                    full_iki.show();
//                    reklam=0;
//                }
            }
        });
        setringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveas(i[say],say);
                Toast.makeText(getApplicationContext(),"saved in /media/Biyotek.company file",Toast.LENGTH_SHORT).show();

            }
        });

        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m != null) {
                    m.reset();
                    if (say<i.length-1) {
                        say++;

                        m = MediaPlayer.create(getApplicationContext(), i[say]);
                        m.start();
                        tbas.setText(s[say]);
                        seekbar();
                        Toast.makeText(getApplicationContext(),"in turn",Toast.LENGTH_SHORT).show();
                        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (say<i.length-2){
                                    sirametodu(say);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),"last",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        bbefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m != null) {
                    m.reset();
                    if (say>0) {
                        say--;
                    }
                }
                m = MediaPlayer.create(getApplicationContext(), i[say]);
                m.start();
                seekbar();
                tbas.setText(s[say]);

            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                sendWhatsAppAudio(say);

            }
        });

        bpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m!=null) {
                    if (m.isPlaying()) {
                        m.pause();
                        bpause.setBackgroundResource(R.drawable.dd);
                    } else {
                        m.start();
                        bpause.setBackgroundResource(R.drawable.ac);
                    }
                }

            }
        });



    }
    public void sirametodu( int say){

        if (say<i.length-2){

            say++;
            if (m != null) {
                m.reset();

                m = new MediaPlayer();
                m = MediaPlayer.create(getApplicationContext(), i[say]);
                m.start();
                seekbar();
                tbas.setText(s[say]);
                final int saysay = say;
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        if (saysay < i.length - 1) {

                            sirametodu(saysay);
                            seekbar();
                            tbas.setText(s[saysay]);
                        }
                    }
                });
            }
        }
        else m.start();
        seekbar();




    }


    public boolean saveas(int ressound, int c){
        byte[] buffer=null;
        InputStream fIn = getBaseContext().getResources().openRawResource(ressound);
        int size=0;

        try {
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }


        String filename=s[c]+".mp3";

        boolean exists = (new File(path)).exists();
        if (!exists){new File(path).mkdirs();}

        FileOutputStream save;
        try {
            save = new FileOutputStream(path+filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path+filename)));

        File k = new File(path, filename);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "exampletitle");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "biyotek.company ");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);

        //Insert it into the database
        this.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()), values);


        return true;
    }

    public void seekbar(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekBar = new Thread() {

            @Override
            public void run() {

                int totalDuration = m.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {

                        sleep(100);
                        currentPosition = m.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        songSeekbar.setMax(m.getDuration());

        updateseekBar.start();

        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                m.seekTo(seekBar.getProgress());

            }
        });
    }

    private void sendWhatsAppAudio(int p){
        try {
            //Copy file to external ExternalStorage.
            String mediaPath = copyFiletoExternalStorage(i[p], "sound.mp3");

            Intent shareMedia = new Intent(Intent.ACTION_SEND);
            //set WhatsApp application.
            shareMedia.setPackage("com.whatsapp");
            shareMedia.setType("audio/mp3");
            //set path of media file in ExternalStorage.
            shareMedia.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaPath));
            startActivity(Intent.createChooser(shareMedia, "Compartiendo archivo."));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "????", Toast.LENGTH_LONG).show();
        }
    }

    private String copyFiletoExternalStorage(int resourceId, String resourceName){
        String pathSDCard = Environment.getExternalStorageDirectory() + "/media/Biyotek.company/" + resourceName;
        try{
            InputStream in = getResources().openRawResource(resourceId);
            FileOutputStream out=null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  pathSDCard;
    }

//    private void full() {
//
//        full_iki.loadAd(new AdRequest.Builder().build());
//        full_iki.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//
//
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                full();
//            }
//
//        });
//    }

//    private void banner() {
//        MobileAds.initialize(this,getResources().getString(R.string.id));
//        adreklam=new AdView(this);
//        adreklam=findViewById(R.id.reklam);
//        AdRequest adRequest=new AdRequest.Builder().build();
//        adreklam.loadAd(adRequest);
//        full_iki=new InterstitialAd(this);
//        full_iki.setAdUnitId(getResources().getString(R.string.full));
//    }

}
