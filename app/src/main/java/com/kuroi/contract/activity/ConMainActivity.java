package com.kuroi.contract.activity;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kuroi.contract.R;
import com.kuroi.contract.model.Contract;
import com.kuroi.contract.service.ConService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConMainActivity extends Activity {

    private ListView contract_list=null;
    private EditText search=null;
    private List contracts=null;
    private Contract contract=null;
    private ConService service=null;
    public static final int OPTION_DIALOG = 1;
    private PopupWindow popupWindow;
    private ListView menuListView;
    private RelativeLayout relativeLayout=null;
    private RelativeLayout relativeLayout2=null;
    private RelativeLayout relativeLayout3=null;
    private Button button=null;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private TextView countview ;
    private int sort=0;
    private static final String[] m={"创建时间排序","签约时间排序","按照金额排序"};
    private static final String ACTIVITY_TAG="LogDemo";
    private String picName="";
    private static final int CAPTURE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_con);
        ActionBar actionBar=getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("      合同");
        service = new ConService(this);
        init();
        getContent();
        adapter = new ArrayAdapter<String>(this,R.layout.myspinner_con,m);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner.setVisibility(View.VISIBLE);
        initPopupWindow();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                relativeLayout2.setVisibility(View.GONE);
                relativeLayout3.setVisibility(View.VISIBLE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                relativeLayout3.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.VISIBLE);
                search.setText("");
            }
        });
    }
    private void init(){
        contract_list = (ListView)findViewById(R.id.contract_list);
        contract_list.setCacheColorHint(Color.TRANSPARENT);
        contract_list.setOnItemClickListener(new ViewItemListener());
        search = (EditText)findViewById(R.id.search);
        search.addTextChangedListener(new SearchTextChangedListener());
        spinner = (Spinner) findViewById(R.id.Spinner01);
        relativeLayout=(RelativeLayout) findViewById(R.id.con_search_go);
        relativeLayout2=(RelativeLayout) findViewById(R.id.con_ss);
        relativeLayout3=(RelativeLayout) findViewById(R.id.con_search);
        button=(Button)findViewById(R.id.con_search_button);
    }
    private void getContent(){
        countview=(TextView) findViewById(R.id.countText);
//        countview.setText("合同总数:" + service.getCount().toString());
        List mylist = new ArrayList();
        String queryName = search.getText().toString();
        contracts = service.getByName(queryName,sort); // get an contracts array
        if(contracts != null){
            for(int i=0; i<contracts.size(); i++){
                Contract contract = (Contract)contracts.get(i);
                Calendar cal = Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int dayOfMonth=cal.get(Calendar.DAY_OF_MONTH);
                String now= new StringBuilder().append(year).append(
                        (month + 1) < 10 ? "0" + (month + 1) : (month + 1)).append(
                        (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth).toString();
                String check=(now.compareTo(contract.getDate())>0&&!contract.getDate().equals(""))?"完成":contract.getDate();
                // HashMap
                HashMap map = new HashMap();
                map.put("tv_number", contract.getNumber());
                map.put("tv_money", contract.getMoney());
                map.put("tv_name", contract.getName());
                map.put("tv_date", check);
                mylist.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mylist,R.layout.my_list_item_con,
                new String[] {"tv_number","tv_money","tv_name","tv_date"},
                new int[] {R.id.item_number,R.id.item_money,R.id.item_name,R.id.item_date});
        contract_list.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_con, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_contract) {
            if(popupWindow.isShowing())
                popupWindow.dismiss();
            else
                popUp();
            return true;
        }
        if (id == android.R.id.home)  // 返回
        {
            finish();
        }
//        if(id == R.id.camera) {
//            Log.d(ACTIVITY_TAG, "open");
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
//            startActivityForResult(intent, CAPTURE_REQUEST_CODE);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    //显示PopupWindow菜单
    private void popUp(){
        //设置位置
        popupWindow.showAsDropDown(this.findViewById(R.id.add_contract), 0, 2);
    }
    public Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Contract");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        picName = "IMG_" + timeStamp + ".jpg";
        Log.d("pn",picName);
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + picName);
        return Uri.fromFile(mediaFile);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        popupWindow.dismiss();
        if (requestCode == CAPTURE_REQUEST_CODE) {
            Log.d(ACTIVITY_TAG,"ok");
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.d(ACTIVITY_TAG, picName);
                    Contract cc=new Contract();
//                    cc.setName("照片模式");
                    cc.setImg(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES) + "/Contract/" + picName);
                    service.save(cc);
                    Log.d(ACTIVITY_TAG, picName);
                    break;
                case Activity.RESULT_CANCELED:
                    Log.d(ACTIVITY_TAG, "nonnnnnnnnnnnnnnnnnnnnn");
                    break;
            }
            Log.d(ACTIVITY_TAG,"xxxxxxxxxxxxxxxxxxxxxxxxxx");
        }
    }
    @Override
    protected void onRestart() {
        getContent();
        super.onRestart();
    }
    protected Dialog onCreateDialog(int id){
        Dialog dialog;
        switch(id){
            case OPTION_DIALOG:
                dialog = createOptionDialog();
                dialog.setCanceledOnTouchOutside(true);
                break;
            default:
                dialog = null;
        }
        return dialog;
    }
    private Dialog createOptionDialog(){
        final Dialog optionDialog;
        View optionDialogView = null;
        LayoutInflater li = LayoutInflater.from(this);
        optionDialogView = li.inflate(R.layout.option_dialog_con, null);
        optionDialog = new AlertDialog.Builder(this).setView(optionDialogView).create();
        ImageButton ibCall = (ImageButton)optionDialogView.findViewById(R.id.dialog_call);
        ImageButton ibView = (ImageButton)optionDialogView.findViewById(R.id.dialog_view);
        ImageButton ibSms = (ImageButton)optionDialogView.findViewById(R.id.dialog_sms);
        ibCall.setOnClickListener(new ImageButtonListener());
        ibView.setOnClickListener(new ImageButtonListener());
        ibSms.setOnClickListener(new ImageButtonListener());
        return optionDialog;
    }
    private void initPopupWindow(){
        View view = getLayoutInflater().inflate(R.layout.popup_window_con, null);
        menuListView = (ListView)view.findViewById(R.id.popup_list_view);
        popupWindow = new PopupWindow(view, 220, WindowManager.LayoutParams.WRAP_CONTENT);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("add","新建合同");
        data.add(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("add","照片上传");
        data.add(map2);
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.popup_list_item_con,
                new String[]{"add"},
                new int[]{R.id.add});
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(ConMainActivity.this, ConAddActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                        break;
                    case 1:
                        Log.d(ACTIVITY_TAG, "open");
                        popupWindow.dismiss();
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
                        Log.d("pn",fileUri.toString());
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                        startActivityForResult(intent2, CAPTURE_REQUEST_CODE);
                        break;
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }
//    private void popUp(){
//        popupWindow.showAsDropDown(this.findViewById(R.id.more), 0, 2);
//    }
    //**************** internal class as Listener ******************
    class SearchTextChangedListener implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {getContent();}
    }
    class ViewItemListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // get the contract from the contracts array.
            contract = (Contract)contracts.get(position);
            showDialog(OPTION_DIALOG);
        }
    }
    class ImageButtonListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.dialog_call:
                    Intent intentm = new Intent(ConMainActivity.this, ConModifyActivity.class);
                    intentm.putExtra("id", contract.getId());
                    startActivity(intentm);
                    dismissDialog(OPTION_DIALOG);
                    break;
                case R.id.dialog_view:
                    Intent intent = new Intent(ConMainActivity.this, ConDetailActivity.class);
                    intent.putExtra("id", contract.getId());
                    startActivity(intent);
                    dismissDialog(OPTION_DIALOG);
                    break;
                case R.id.dialog_sms:
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConMainActivity.this);
                    builder.setMessage("确定删除吗?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            service.delete(contract.getId());
                            getContent();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    dismissDialog(OPTION_DIALOG);
                    break;
                default:
                    dismissDialog(OPTION_DIALOG);
                    break;
            }
        }
    }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            sort=arg2;
            getContent();
        }
        public void onNothingSelected(AdapterView<?> arg0) {}
    }
}