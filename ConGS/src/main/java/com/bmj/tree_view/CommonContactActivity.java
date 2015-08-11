package com.bmj.tree_view;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmj.bean.CommonContactBean;
import com.bmj.tree.bean.CommonContactNode;
import com.bmj.tree.bean.CommonContactTreeListViewAdapter;
import com.bmj.tree.bean.CommonContactTreeListViewAdapter.OnTreeNodeClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public abstract class CommonContactActivity extends Activity {
    //ATTENTION:为了区别部门的id和人的id，人的id保存在Node的id中，我加了MagicNumber：10000，默认部门id<10000,人员的id=id+10000
    //使用mAdapter.getAllnode()得到所有信息
    public List<CommonContactBean> mDatas = new ArrayList<CommonContactBean>();
    //private List<FileBean> mDatas2 = new ArrayList<FileBean>();
    public ListView mTree;
    public CommonContactTreeListViewAdapter mAdapter;
    public static WebSocketConnection mConnection;
    public boolean isConnected = false;
    //final String type3uri = ws://101.200.189.127:1234/ws
    final String type3uri = "ws://192.168.50.11:8000/ws";
    final static int COMMON_CONTANCT = 999;
    final static int ROOT_DIR = 0;
    public TextView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commoncontact_activity_main);
        tx = (TextView) findViewById(R.id.id_text);
        //websocket

        mConnection = new WebSocketConnection();
        connect();

        //拿到listview，传入mData，当中初始化了一个Adapter；


    }

    abstract public String getData();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commoncontact_menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_contract) {
            Toast.makeText(getApplicationContext(), getData(), Toast.LENGTH_SHORT).show();

        }

        return true;
    }


    public void connect() {
        try {
            mConnection.connect(type3uri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Toast.makeText(getApplicationContext(), "已连接", Toast.LENGTH_SHORT).show();
                    isConnected = true;
                    //发送请求
                    sendreq();
                }

                @Override
                public void onTextMessage(String payload) {
                    try {
                        //Toast.makeText(getApplicationContext(), payload, Toast.LENGTH_SHORT).show();
                        initDatas(payload);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onClose(int code, String reason) {
                    isConnected = false;
                    Toast.makeText(getApplicationContext(), "失去连接", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (WebSocketException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void sendreq() {

        if (mConnection == null || !mConnection.isConnected() || !isConnected)
            return;

        JSONObject request2 = new JSONObject();
        try {
            request2.put("type", "3");
            request2.put("cmd", "zuzhijiagouchangyonglianxiren");
            request2.put("time", "0");
            //写死了的一个uid，用于调试
            request2.put("uid", "1345");
            mConnection.sendTextMessage(request2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //拿到用户传入的数据，转化为List<CommonContactNode>以及设置Node间关系，然后根节点，从根往下遍历进行排序。
    private void initDatas(String test) throws IOException, JSONException {
//
//		InputStreamReader isr = new InputStreamReader(getAssets().open("test.json"), "UTF-8");
//		BufferedReader br = new BufferedReader(isr);
//		String line;
//		StringBuilder builder = new StringBuilder();
//		while ((line = br.readLine()) != null) {
//			builder.append(line);
//		}
        String strUTF8 = URLDecoder.decode(test, "UTF-8");


        JSONObject root = new JSONObject(strUTF8);

        //得到所有部门的JASON对象保存在一个数组里，传过来的JASON中的部门结构是pri,pID
        mDatas.add(new CommonContactBean(COMMON_CONTANCT, ROOT_DIR, "常用联系人"));
        JSONArray usercomman = root.getJSONArray("changyonglianxiren");
        for (int i = 0; i < usercomman.length(); i++) {
            JSONObject lan = usercomman.getJSONObject(i);
            //id是唯一标识符,0为根目录，name为名称
            mDatas.add(new CommonContactBean(lan.getInt("uid") + 10000, COMMON_CONTANCT, lan.getString("name")));
        }

        JSONArray userarray = root.getJSONArray("dept");
        for (int i = 0; i < userarray.length(); i++) {
            JSONObject lan = userarray.getJSONObject(i);
            if (!lan.getString("pri").contains("-")) {
                //id是唯一标识符,0为根目录，name为名称
                if (lan.getString("status").equals("1"))
                    mDatas.add(new CommonContactBean(lan.getInt("id"), ROOT_DIR, lan.getString("name")));
            } else {
                //子一级
                String[] forid = lan.getString("pri").split(",");
                if (lan.getString("status").equals("1"))
                    mDatas.add(new CommonContactBean(lan.getInt("id"), Integer.parseInt(forid[1]), lan.getString("name")));

            }
        }

        JSONArray array = root.getJSONArray("user");
        for (int j = 0; j < array.length(); j++) {
            JSONObject user = array.getJSONObject(j);
            String[] g = user.getString("udept").split(",");
            for (int m = 0; m < g.length; m++) {
                if (user.getString("status").equals("1"))
                    mDatas.add(new CommonContactBean(user.getInt("uid") + 10000, Integer.parseInt(g[m].substring(0, g[m].indexOf('-'))), user.getString("name")));
            }

        }

        try {
            //simpletree adapter
            mTree = (ListView) findViewById(R.id.id_tree);
            try {
                mAdapter = new CommonContactSimpleTreeAdapter<CommonContactBean>(tx, mTree, this, mDatas, 15);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
                @Override
                public void onClick(CommonContactNode node, int position) {

                    if (node.isLeaf()) {
                        Toast.makeText(getApplicationContext(), node.getName(),
                                Toast.LENGTH_SHORT).show();
                        //do Something if click
                    }
                }

            });
            mTree.setAdapter(mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}