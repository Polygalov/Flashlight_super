package ua.com.adr.android.flashlight;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    Camera camera = null;
    //ToggleButton toggleTorch;
    ImageButton toggleTorch;
    Parameters parameters;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private final int FLASH_NOT_SUPPORTED = 0;
    private final int FLASH_TORCH_NOT_SUPPORTED = 1;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toggleTorch = (ImageButton) findViewById(R.id.toggleButton1);

       // toggleTorch.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.selektor));

        // если камера имеет вспышку
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            toggleTorch.setOnClickListener(new View.OnClickListener() {

                public void onClick(View button) {
                    //Set the button's appearance
                    button.setSelected(!button.isSelected());

                    if (button.isSelected()) {
                        //Handle selected state change
                        if (camera == null) {
                            playSound();




                            camera = Camera.open();
                            parameters = camera.getParameters();
                            List flashModes = parameters
                                    .getSupportedFlashModes();
                            if (flashModes
                                    .contains(Parameters.FLASH_MODE_TORCH)) {
                                parameters
                                        .setFlashMode(Parameters.FLASH_MODE_TORCH);
                                camera.setParameters(parameters);
                                camera.startPreview();
                            } else {
                                showDialog(MainActivity.this,
                                        FLASH_TORCH_NOT_SUPPORTED);
                            }
                            toggleTorch.setImageResource(R.drawable.btn_switch_on);
                        }
                    } else {
                        //Handle de-select state change
                             // выключаем вспышку
                        playSound();



                                parameters
                                        .setFlashMode(Parameters.FLASH_MODE_OFF);
                                camera.setParameters(parameters);
                                camera.stopPreview();
                                camera.release();
                                camera = null;
                        toggleTorch.setImageResource(R.drawable.btn_switch_off);
                    }

                }

            });

        } else {
            showDialog(MainActivity.this, FLASH_NOT_SUPPORTED);
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (camera != null) {
            camera.release();
        }
    }

    private void playSound(){

            mp = MediaPlayer.create(MainActivity.this, R.raw.sound1756);


        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }



    public void showDialog(Context context, int dialogId) {
        switch (dialogId) {
            case FLASH_NOT_SUPPORTED:
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Ваше устройство не поддерживает вспышку")
                        .setCancelable(false)
                        .setNeutralButton("Close", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
                break;
            case FLASH_TORCH_NOT_SUPPORTED:
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Ваше устройство не поддерживает вспышку")
                        .setCancelable(false)
                        .setNeutralButton("Close", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
        }
    }
}