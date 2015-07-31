package com.kuroi.contract.activity;

import com.kuroi.contract.R;
import com.kuroi.contract.model.Contract;
import com.kuroi.contract.service.Service;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends ActionBarActivity {
    private EditText number=null;
    private EditText name=null;
    private EditText type=null;
    private EditText customer=null;
    private EditText date=null;
    private EditText dateStart=null;
    private EditText dateEnd=null;
    private EditText money=null;
    private EditText discount=null;
    private EditText principal=null;
    private EditText ourSigner=null;
    private EditText cusSigner=null;
    private EditText remark=null;
    private ImageView image=null;
    private Service service=null;
    private Button pickDate = null;
    private Button pickDate2 = null;
    private Button pickDate3 = null;
    private Calendar c = null;
    private static final int CAPTURE_REQUEST_CODE = 100;
    private String picName="";
    private static final String ACTIVITY_TAG="LogDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);
            service = new Service(this);
            init();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            pickDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(1);
                }
            });
            pickDate2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(2);
                }
            });
            pickDate3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(3);
                }
            });
//            image.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Log.d(ACTIVITY_TAG,"cc");
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
//                    startActivityForResult(intent, CAPTURE_REQUEST_CODE);
//                }
//                public Uri getOutputMediaFileUri() {
//                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES), "Contract");
//                    if (!mediaStorageDir.exists()) {
//                        if (!mediaStorageDir.mkdirs()) {
//                            return null;
//                        }
//                    }
//                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                    File mediaFile;
//                    picName = "IMG_" + timeStamp + ".jpg";
//                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + picName);
//                    return Uri.fromFile(mediaFile);
//                }
//            });
//        }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAPTURE_REQUEST_CODE) {
//            Log.d(ACTIVITY_TAG,"ok");
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    Log.d(ACTIVITY_TAG, "ok1");
//                    service.save(getContent());
//                    finish();
//                    Log.d(ACTIVITY_TAG, picName);
//                    break;
//                case Activity.RESULT_CANCELED:
//                    finish();
//                    Log.d(ACTIVITY_TAG, "ok3");
//                    break;
//            }
//            Log.d(ACTIVITY_TAG,"ok4");
//        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case 1:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                                date.setText(new StringBuilder().append(year).append(
                                        (month + 1) < 10 ? "0" + (month + 1) : (month + 1)).append(
                                        (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
                            }
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
            case 2:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                                dateStart.setText(new StringBuilder().append(year).append(
                                        (month + 1) < 10 ? "0" + (month + 1) : (month + 1)).append(
                                        (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
                            }
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
            case 3:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                                dateEnd.setText(new StringBuilder().append(year).append(
                                        (month + 1) < 10 ? "0" + (month + 1) : (month + 1)).append(
                                        (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
                            }
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
        }
        return dialog;
    }
    private void init(){
        number = (EditText)findViewById(R.id.contract_number);  // get all EditText views
        name = (EditText)findViewById(R.id.contract_name);
        type = (EditText)findViewById(R.id.contract_type);
        customer = (EditText)findViewById(R.id.contract_customer);
        date = (EditText)findViewById(R.id.contract_date);
        dateStart = (EditText)findViewById(R.id.contract_dateStart);
        dateEnd = (EditText)findViewById(R.id.contract_dateEnd);
        money = (EditText)findViewById(R.id.contract_money);
        discount = (EditText)findViewById(R.id.contract_discount);
        principal = (EditText)findViewById(R.id.contract_principal);
        ourSigner = (EditText)findViewById(R.id.contract_ourSigner);
        cusSigner = (EditText)findViewById(R.id.contract_cusSigner);
        remark = (EditText)findViewById(R.id.contract_remark);
        image = (ImageView)findViewById(R.id.image_view);
        pickDate = (Button) findViewById(R.id.button);
        pickDate2 = (Button) findViewById(R.id.button2);
        pickDate3 = (Button) findViewById(R.id.button3);
    }
    private Contract getContent(){
        Contract contract = new Contract();
        contract.setNumber(number.getText().toString());
        contract.setName(name.getText().toString());
        contract.setType(type.getText().toString());
        contract.setCustomer(customer.getText().toString());
        contract.setDate(date.getText().toString());
        contract.setDateStart(dateStart.getText().toString());
        contract.setDateEnd(dateEnd.getText().toString());
        contract.setMoney(money.getText().toString());
        contract.setDiscount(discount.getText().toString());
        contract.setPrincipal(principal.getText().toString());
        contract.setOurSigner(ourSigner.getText().toString());
        contract.setCusSigner(cusSigner.getText().toString());
        contract.setRemark(remark.getText().toString());
        contract.setImg(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/Contract/"+picName);
        return contract;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {  // 保存
            if(number.getText().toString().equals(""))
                Toast.makeText(this, "编号不能为空", Toast.LENGTH_LONG).show();
            else if(name.getText().toString().equals(""))
                Toast.makeText(this, "标题不能为空", Toast.LENGTH_LONG).show();
            else if(customer.getText().toString().equals(""))
                Toast.makeText(this, "客户不能为空", Toast.LENGTH_LONG).show();
            else if(date.getText().toString().equals(""))
                Toast.makeText(this, "签约日期不能为空", Toast.LENGTH_LONG).show();
            else if(principal.getText().toString().equals(""))
                Toast.makeText(this, "负责人不能为空", Toast.LENGTH_LONG).show();
            else if(money.getText().toString().equals(""))
                Toast.makeText(this, "总金额不能为空", Toast.LENGTH_LONG).show();
            else {
                boolean flag = service.save(getContent());
                if(flag) {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(this, "添加失败", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        if (id == android.R.id.home)  // 返回
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}