package com.kuroi.contract.activity;

import com.kuroi.contract.R;
import com.kuroi.contract.model.Contract;
import com.kuroi.contract.service.Service;

import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class DetailActivity extends ActionBarActivity {
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
    private Service service=null;
    private static final String ACTIVITY_TAG="LogDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        contract = new Contract();
        init();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id == -1) {
            finish();
        } else {
            service = new Service(this);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ShowActivity.class);
                intent.putExtra("picName", contract.getImg());
                Log.d(ACTIVITY_TAG, contract.getImg());
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
        image = (ImageView)findViewById(R.id.image_button);
    }
    private void dialog(){
        Builder builder = new Builder(DetailActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_modify) {
            Intent intent = new Intent(DetailActivity.this, ModifyActivity.class);
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
            service = new Service(this);
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
