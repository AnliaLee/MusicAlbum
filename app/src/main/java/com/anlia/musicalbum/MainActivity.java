package com.anlia.musicalbum;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anlia.fallingview.FallObject;
import com.anlia.fallingview.FallingView;
import com.anlia.musicalbum.view.HatTextView;
import com.anlia.musicalbum.view.MusicButton;
import com.anlia.photofactory.factory.PhotoFactory;

public class MainActivity extends AppCompatActivity {
    HatTextView textChoose;
    RelativeLayout viewAlbum;
    MusicButton btnMusic;
    FallingView fallingView;
    ImageView imgBack;

    private MediaPlayer mPlayer;
    private FallObject fallObject;
    private PhotoFactory photoFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);
        initWidget();

        //初始化一个雪花样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.drawable.ic_snow));
        fallObject = builder
                .setSpeed(5,true)
                .setSize(50,50,true)
                .setWind(5,true,true)
                .build();

        mPlayer = MediaPlayer.create(this, R.raw.jinglebells);
        mPlayer.setLooping(true);

        photoFactory = new PhotoFactory(this, this);
    }

    private void initWidget(){
        viewAlbum = (RelativeLayout) findViewById(R.id.view_album);
        imgBack = (ImageView) findViewById(R.id.img_back);
        fallingView = (FallingView) findViewById(R.id.fallingView);

        btnMusic = (MusicButton) findViewById(R.id.btn_music);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMusic.playMusic();
                try {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                        } else {
                            mPlayer.start();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnMusic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.prepare();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                btnMusic.stopMusic();
                return true;//消费此长按事件，不再向下传递
            }
        });

        textChoose = (HatTextView) findViewById(R.id.text_choose);
        textChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE

                    }, 100);
                } else {
                    showListDialog();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showListDialog();
                }else{// 没有获取到权限，做特殊处理
                    Toast.makeText(this, "请授予权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoFactory.FactoryFinish(requestCode,resultCode,data).setOnResultListener(new PhotoFactory.OnResultListener() {
            @Override
            public void TakePhotoCancel() {
                Toast.makeText(MainActivity.this, "取消拍照", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void GalleryPhotoCancel() {
                Toast.makeText(MainActivity.this, "取消选择", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void HasData(PhotoFactory.FinishBuilder resultData) {
                textChoose.setVisibility(View.GONE);
                viewAlbum.setVisibility(View.VISIBLE);
                Uri uri = resultData.GetUri();
                imgBack.setImageURI(uri);

                fallingView.addFallObject(fallObject,100);//添加100个下落物体对象

                btnMusic.playMusic();
                try {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                        } else {
                            mPlayer.start();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showListDialog() {
        final String[] items = { "使用相机拍照","从相册中挑选照片"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(MainActivity.this);
        listDialog.setTitle("选择照片");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(photoFactory == null){
                    Toast.makeText(MainActivity.this, "没有权限！", Toast.LENGTH_SHORT).show();
                }else {
                    switch (which){
                        case 0:
                            photoFactory.FactoryStart()
                                    .SetStartType(PhotoFactory.TYPE_PHOTO_UNTREATED)
                                    .Start();
                            break;
                        case 1:
                            photoFactory.FactoryStart()
                                    .SetStartType(PhotoFactory.TYPE_PHOTO_FROM_GALLERY)
                                    .Start();
                            break;
                    }
                }
            }
        });
        listDialog.show();
    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
