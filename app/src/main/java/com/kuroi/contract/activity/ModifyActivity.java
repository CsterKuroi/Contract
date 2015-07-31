package com.kuroi.contract.activity;

import com.kuroi.contract.R;
import com.kuroi.contract.model.Contract;
import com.kuroi.contract.service.Service;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;


public class ModifyActivity extends ActionBarActivity {

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
    private Contract contract=null;
    private Button pickDate4 = null;
    private Button pickDate5 = null;
    private Button pickDate6 = null;
    private Calendar c = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        contract = new Contract();
        service = new Service(this);
        init();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if(id == -1){
            finish();
        }else{
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
        pickDate4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(4);
            }
        });
        pickDate5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(5);
            }
        });
        pickDate6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(6);
            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case 4:
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
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                );
                break;
            case 5:
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
            case 6:
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
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                );
                break;
        }
        return dialog;
    }
    private void init(){
        number = (EditText)findViewById(R.id.number);
        name = (EditText)findViewById(R.id.name);
        type = (EditText)findViewById(R.id.type);
        customer = (EditText)findViewById(R.id.customer);
        date = (EditText)findViewById(R.id.date);
        dateStart = (EditText)findViewById(R.id.dateStart);
        dateEnd = (EditText)findViewById(R.id.dateEnd);
        money = (EditText)findViewById(R.id.money);
        discount = (EditText)findViewById(R.id.discount);
        principal = (EditText)findViewById(R.id.principal);
        ourSigner = (EditText)findViewById(R.id.ourSigner);
        cusSigner = (EditText)findViewById(R.id.cusSigner);
        remark = (EditText)findViewById(R.id.remark);
        image = (ImageView)findViewById(R.id.image_view);
        pickDate4 = (Button) findViewById(R.id.button4);
        pickDate5 = (Button) findViewById(R.id.button5);
        pickDate6 = (Button) findViewById(R.id.button6);
    }
    private Contract getContent(){
        Contract c = new Contract();
        c.setId(contract.getId());
        c.setNumber(number.getText().toString());
        c.setName(name.getText().toString());
        c.setType(type.getText().toString());
        c.setCustomer(customer.getText().toString());
        c.setDate(date.getText().toString());
        c.setDateStart(dateStart.getText().toString());
        c.setDateEnd(dateEnd.getText().toString());
        c.setMoney(money.getText().toString());
        c.setDiscount(discount.getText().toString());
        c.setPrincipal(principal.getText().toString());
        c.setOurSigner(ourSigner.getText().toString());
        c.setCusSigner(cusSigner.getText().toString());
        c.setRemark(remark.getText().toString());
        return c;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
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
                boolean flag = service.update(getContent());
                if(flag) {
                    Toast.makeText(ModifyActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(ModifyActivity.this, "修改失败", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
