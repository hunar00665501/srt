package com.example.subtitle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class readFile extends AppCompatActivity {


      private static final int SELECT_FOLDER=43;
    ListView lv;
    TextView tv;
    Button exportBtn;
    String fileName;
    int vlad=0;
     int itemnum=0;
     SearchView sv;

    static   String  m_Text="";
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);
        sv= findViewById(R.id.sch);





        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<String>(readFile.this,android.R.layout.simple_list_item_1,arrayList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view= super.getView(position, convertView, parent);
                if (position % 2 == 0)
                    view.setBackgroundColor(getResources().getColor(
                            android.R.color.tab_indicator_text
                    ));
    else
                    view.setBackgroundColor(getResources().getColor(
                            android.R.color.white
                    ));
          return view;

            }
        };
        lv = (ListView) findViewById(R.id.lv);




    Intent intent=getIntent();
    String mod=intent.getStringExtra("mod");

if (mod.equals("new")){
    myOpenImagePicker();

}else if (mod.equals("rec")){
    String fn=intent.getStringExtra("fn");
    fileName=fn;
    resend();
}

        setTitle(fileName);








        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (i%2!=0) {
                    itemnum=i;
                    String selectedFromList = (String) lv.getItemAtPosition(i);
                    String DefultT = (String) lv.getItemAtPosition(i-1);

                    AlertDialog.Builder builder = new AlertDialog.Builder(readFile.this);
                    builder.setTitle(DefultT);


                    final EditText input = new EditText(readFile.this);
                    input.setHint("Write the Kurdish translation here.");
                    input.setSingleLine(false);
                    input.setLines(2);
                    input.setGravity(Gravity.RIGHT);
                    if (!selectedFromList.equals("Write the Kurdish translation here."))
                        input.setText(selectedFromList);
                    builder.setView(input);


                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            vlad=0;
                             String t=input.getText().toString();
                            String[] lines = t.split("\n");
                            int tc= lines.length;

                            if (tc<3){
                                int l1=lines[0].length();

                                if (l1>42){
                                    input.setError("Line 1 : more than (42) character");
                                    vlad=1;
                               }

                                if (tc>1){
                                    int l2=lines[1].length();
                                    if (l2>42) {
                                        input.setError("Line 2 : more than (42) character");
                                        vlad=1;
                                    }
                                }




                            }else {
                                vlad=1;
                                input.setError("You cannot have more than 2 lines.");
                            }



                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });




                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (vlad==0) {
                                m_Text = input.getText().toString();
                                arrayList.set(i,m_Text);
                            } else
                         Toast.makeText(readFile.this,"hihig",Toast.LENGTH_LONG).show();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();





                }
            }
        });




    }


    private void export(){
        String s="";
        int end=arrayList.size()-1;
        for (int i=0;i<arrayList.size();i++){


            if (arrayList.get(i).equals("Write the Kurdish translation here.")){
                lv.smoothScrollToPosition(i);
                return;
            }

            if (i==end && i%2!=0){
                s +=arrayList.get(i);

                break;
            }
            if (i%2!=0)
                s +=arrayList.get(i)+"\n\n";
        }

        try {

            File gpxfile = new File("/storage/emulated/0/Export",fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(s);
            writer.flush();
            writer.close();
            Toast.makeText(readFile.this, "Exported From Storage", Toast.LENGTH_LONG).show();
        } catch (Exception e) { Toast.makeText(readFile.this, e.getMessage(), Toast.LENGTH_LONG).show();}



    }





    @Override
    public void onBackPressed() {
        save();
        Toast.makeText(readFile.this,"auto saved",Toast.LENGTH_LONG).show();

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,1,1,"Go to");
        menu.add(0,2,2,"Save");
        menu.add(0,3,3,"Export");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==1){

            AlertDialog.Builder builder = new AlertDialog.Builder(readFile.this);
            builder.setTitle("Search");


            final EditText input = new EditText(readFile.this);
            input.setHint("Search by Number");
            input.setSingleLine(true);
           // input.setInputType();
           // input.setGravity(Gravity.RIGHT);
             builder.setView(input);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int num=Integer.parseInt(input.getText().toString());

                    lv.smoothScrollToPosition(num);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }

       else if (item.getItemId()==2){
            save();
        }
        else if (item.getItemId()==3){
            export();
        }

        return super.onOptionsItemSelected(item);
    }

    public void myOpenImagePicker() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("text/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select your file"),
                    SELECT_FOLDER);

        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            startActivityForResult(intent, SELECT_FOLDER);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==requestCode &&  resultCode== Activity.RESULT_OK){

            if (data!=null){

                Uri uri=data.getData();
                String  path=uri.getPath();
                Context context=getApplicationContext();




                path=path.substring(path.indexOf(":")+1);
                File f = new File(path);
                fileName = f.getName();

                Toast.makeText(readFile.this,fileName,Toast.LENGTH_LONG).show();
                readFile1(path);


            }


        }else {
            Intent intent=new Intent(readFile.this,MainActivity.class);
            startActivity(intent);
        }

    }



private void resend(){

    File sdcard = new File(readFile.this.getFilesDir()+"/recent/");
    File file = new File(sdcard,fileName);

    StringBuilder text = new StringBuilder();
    try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;


        int c=0;
        int whileCount=0;
        while ((line = br.readLine()) != null) {
            whileCount++;
            if (c==1 && !line.equals("")){
                String oldStr= arrayList.get(whileCount-2);
                arrayList.set(whileCount-2,oldStr+"\n"+line);

            }

            if (line.equals("")) {
                c=0;
                continue;

            }else
                c++;

            arrayList.add(line);


        }
        //  tv.setText(line2);
        lv.setAdapter(arrayAdapter);
        br.close();
    }
    catch (IOException e) {
        Toast.makeText(readFile.this,e.getMessage(),Toast.LENGTH_LONG).show();

    }




}



    private void save(){



        String s="";
        int end=arrayList.size()-1;
        for (int i=0;i<arrayList.size();i++){

            if (i==end ){
                s +=arrayList.get(i);
                break;
            }

                s +=arrayList.get(i)+"\n\n";
        }
        try {

            File gpxfile = new File(readFile.this.getFilesDir()+"/recent/",fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(s);
            writer.flush();
            writer.close();
            Toast.makeText(readFile.this, "Saved("+fileName+")", Toast.LENGTH_LONG).show();
        } catch (Exception e) { Toast.makeText(readFile.this, e.getMessage(), Toast.LENGTH_LONG).show();}



    }





    private void readFile1(String p) {


        File sdcard = Environment.getExternalStorageDirectory();

        File file = new File(sdcard,p);


        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            ArrayList<String> arr;
            int c=0;
            int whileCount=0;
            while ((line = br.readLine()) != null) {
                whileCount++;
                if (c==1 && !line.equals("")){
                   String oldStr= arrayList.get(whileCount-2);
                    arrayList.set(whileCount-2,oldStr+"\n"+line);

                }

                if (line.equals("")) {
                    c=0;
                    continue;

                }else
                    c++;

                  arrayList.add(line);
                  arrayList.add("Write the Kurdish translation here.");


            }
          //  tv.setText(line2);
          lv.setAdapter(arrayAdapter);
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(readFile.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }






    }







}