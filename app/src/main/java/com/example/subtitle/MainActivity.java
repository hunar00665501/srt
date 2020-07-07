package com.example.subtitle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    FloatingActionButton readBtn;
    ListView lv;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        File f=new File("/storage/emulated/0/Export");
        if (!f.exists())
        f.mkdir();


        File file = new File(MainActivity.this.getFilesDir(), "recent");
        if (!file.exists()) {
            file.mkdir();
        }



        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);

        lv = (ListView) findViewById(R.id.rlv);






        File folder = new File(MainActivity.this.getFilesDir()+"/recent");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String  ff=listOfFiles[i].getName()+"";
                arrayList.add(ff);

            }
        }



     lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             String selectedFromList = (String) lv.getItemAtPosition(i);
            Intent intent=new Intent(MainActivity.this,readFile.class);
            intent.putExtra("mod","rec");
            intent.putExtra("fn",selectedFromList);
            startActivity(intent);
         }
     });


   lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
       @Override
       public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {

           final String selectedFromList = (String) lv.getItemAtPosition(i);

           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
           builder.setTitle("Delete File");
           builder.setMessage("Are you sure to delete ("+selectedFromList+")");



           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {


                   File file = new File(MainActivity.this.getFilesDir()+"/recent",selectedFromList);

                   if(file.delete())
                   {
                       Intent intent = getIntent();
                       finish();
                       startActivity(intent);


                   }
                   else
                   {
                       Toast.makeText(MainActivity.this,"d'not delete",Toast.LENGTH_LONG).show();

                   }




               }
           });
           builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           });

           builder.show();





           return true;
       }
   });





        lv.setAdapter(arrayAdapter);





        readBtn=findViewById(R.id.readBtn);

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,readFile.class);
                intent.putExtra("mod","new");
                startActivity(intent);
            }
        });


        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }


    }



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }










}