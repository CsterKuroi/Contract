package com.kuroi.contract.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kuroi.contract.R;
import com.kuroi.contract.model.Contract;
import com.kuroi.contract.service.ConService;

import java.io.File;

import static com.kuroi.contract.activity.ConShowActivity.getExifOrientation;


public class ConDetailActivity extends Activity {
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
    private Contract contract=null;
    private ConService service=null;
    private static final String ACTIVITY_TAG="LogDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_con);
        contract = new Contract();
        init();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id == -1) {
            finish();
        } else {
            service = new ConService(this);
            contract = service.getById(id);
            number.setText(contract.getNumber());
            name.setText(contract.getName());
            type.setText(contract.getType());
            customer.setText(contract.getCustomer());
            date.setText(contract.getDate());
            dateStart.setText(contract.getDateStart());
            dateEnd.setText(contract.getDateEnd());
            money.setText(contract.getMoney());
            discount.setText(contract.getDiscount());
            principal.setText(contract.getPrincipal());
            ourSigner.setText(contract.getOurSigner());
            cusSigner.setText(contract.getCusSigner());
            remark.setText(contract.getRemark());
            if(new File(contract.getImg()).isFile()){
                int digree=getExifOrientation(contract.getImg());
                Bitmap bm;
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = 10;
                bm = BitmapFactory.decodeFile(contract.getImg(), option);
                if (digree != 0) {
                    // 旋转图片
                    Matrix m = new Matrix();
                    m.postRotate(digree);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                            bm.getHeight(), m, true);
                }
                Log.d(ACTIVITY_TAG, "ok");
                image.setImageBitmap(bm);
            }
            Log.d(ACTIVITY_TAG, "3");
        }
        ActionBar actionBar=getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("      合同详情");
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ConDetailActivity.this, ConShowActivity.class);
                intent.putExtra("picName", contract.getImg());
                startActivity(intent);
            }
        });
    }
    public void init(){
        number = (EditText)findViewById(R.id.contract_number);
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
        Log.d(ACTIVITY_TAG, "1");
        image = (ImageView)findViewById(R.id.image_button);

    }
    private void dialog(){
        Builder builder = new Builder(ConDetailActivity.this);
        builder.setMessage("确定删除吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                service.delete(contract.getId());
                finish();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_con, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_modify) {
            Intent intent = new Intent(ConDetailActivity.this, ConModifyActivity.class);
            intent.putExtra("id", contract.getId());
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_delete) {
            dialog();
            return true;
        }
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onRestart() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if(id == -1){
            finish();
        }else{
            service = new ConService(this);
            contract = service.getById(id);
            number.setText(contract.getNumber());
            name.setText(contract.getName());
            type.setText(contract.getType());
            customer.setText(contract.getCustomer());
            date.setText(contract.getDate());
            dateStart.setText(contract.getDateStart());
            dateEnd.setText(contract.getDateEnd());
            money.setText(contract.getMoney());
            discount.setText(contract.getDiscount());
            principal.setText(contract.getPrincipal());
            ourSigner.setText(contract.getOurSigner());
            cusSigner.setText(contract.getCusSigner());
            remark.setText(contract.getRemark());
        }
        super.onRestart();
    }
}
