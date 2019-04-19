package com.example.cc.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_DIRECTORY = 0;
    private static String compressedFile;
    private static String fileType;
    private static String decompressionPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //用来选中文件后选中该应用时触发
        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            compressedFile = Uri.decode(uri.getEncodedPath());
            fileType = compressedFile.substring(compressedFile.lastIndexOf(".")+1, compressedFile.length());
            Log.v("fileType", fileType);
            if(fileType.equalsIgnoreCase("zip")){
                final Intent chooserIntent = new Intent(this, DirectoryChooserActivity.class);
                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                        .newDirectoryName("DirChooserSample")
                        .allowReadOnlyDirectory(true)
                        .allowNewDirectoryNameModification(true)
                        .build();
                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
                // REQUEST_DIRECTORY is a constant integer to identify the request, e.g. 0
                startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
            }else{
                Toast.makeText(this,"该文件格式暂不支持",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //得到打开目录时传的标识
        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                //得到解压路径
                decompressionPath = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                //解压文件
                try {
                    DecompressionFileUtil.zip(compressedFile, decompressionPath);
                    Toast.makeText(this,"解压成功",Toast.LENGTH_LONG).show();
                }catch (IOException e){
                    Toast.makeText(this,"解压失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
                //关闭应用
                this.finish();
            } else {
                // Nothing selected
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
