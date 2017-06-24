package test.bwie.com.quanxian_demo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //点击打开相册的点击事件
         findViewById(R.id.button_photo).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //checkSelfPermission  检测有没有  权限
                // PackageManager.PERMISSION_DENIED  拒绝权限
                 //PackageManager.PERMISSION_GRANTED  有权限
                 if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED){

                     //如果权限从有到无，如果第一次显示，弹出AlertDialog
                     //shouldShowRequestPermissionRationale  看看权限有没有发生改变
                     if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA)){
                        //第一次显示允许添加权限，弹出AlertDialog，点击ok就请求权限
                         new AlertDialog.Builder(MainActivity.this).setTitle("title")
                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         //既然没有权限   我们就请求权限
                                         ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
                                     }
                                 }).setPositiveButton("no", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         }).create().show();
                     }else {
                     //因为if的没有权限，默认的是false，小米4的6.0就一直返回false，所以要先走else里的，先请求权限
                         //既然没有权限   我们就请求权限
                         ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
                     }

                 }else{
                     camear();
                 }

             }
         });

    }


    //请求权限返回的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==1){
            //camear  权限回调
            if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                //表示用户授权
                Toast.makeText(MainActivity.this,"user Permission",Toast.LENGTH_SHORT).show();
                camear();
            }else {
                //表示用户拒绝授权

                Toast.makeText(MainActivity.this,"no Permission",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >=9){
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package",getPackageName(),null));

                }else if (Build.VERSION.SDK_INT >=8){
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName",getPackageName());
                }

                startActivity(intent);
            }
        }
    }


    public void camear(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }



















}
