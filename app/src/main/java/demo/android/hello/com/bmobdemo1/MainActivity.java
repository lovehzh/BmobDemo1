package demo.android.hello.com.bmobdemo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private Button btnInsert, btnUpdate, btnDelete, btnQuerry;
    private Button btnQuerryUsers;
    private Button btnGoRycycleView;
    private String objectId = "";

    private static  int pagesize = 10;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    /**
     * 初始化视图
     */
    private  void initView() {
        btnInsert = (Button)findViewById(R.id.btn_insert_user);
        btnInsert.setOnClickListener(this);

        btnUpdate = (Button)findViewById(R.id.btn_update_user);
        btnUpdate.setOnClickListener(this);

        btnDelete = (Button)findViewById(R.id.btn_delete_user);
        btnDelete.setOnClickListener(this);

        btnQuerry = (Button)findViewById(R.id.btn_querry_user);
        btnQuerry.setOnClickListener(this);

        btnQuerryUsers = (Button)findViewById(R.id.btn_querry_datas);
        btnQuerryUsers.setOnClickListener(this);

        btnGoRycycleView = (Button)findViewById(R.id.btn_go_list_activity);
        btnGoRycycleView.setOnClickListener(this);
    }

    /**
     * 创建一条person数据 createPersonData
     *
     * @Title: createPersonData
     * @throws
     */
    private void createPerson() {
        final Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Log.i("test", "s: " + s + " e: " + e.toString());
                // 返回成功就可以得到person里面的值
                if(e == null) {
                    ShowToast("创建数据成功:" + p2.getObjectId());
                    objectId = p2.getObjectId();
                }else {
                    ShowToast("创建数据失败:" + s + " " + e.toString());
                }

            }
        });
    }

    /**
     * 更新指定objectId的person数据
     *
     * @return void
     * @throws
     */
    private void updatePersonByObjectId() {
        //将指定ObjectId的Person这一行数据中的address内容更新为“北京朝阳”
        final Person p2 = new Person();
        p2.setAddress("北京朝阳");
        p2.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ShowToast("更新成功：更新后的地址->" + p2.getAddress());
            }
        });
    }

    /**
     * 删除指定ObjectId的person数据 deletePersonByObjectId
     *
     * @Title: deletePersonByObjectId
     * @return void
     * @throws
     */
    private void deletePersonByObjectId() {
        Person p2 = new Person();
        p2.setObjectId(objectId);
        p2.delete(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {

                ShowToast("删除成功");
            }
        });
    }

    /** 查询指定ObjectId的person数据
     * queryPerson
     * @Title: queryPerson
     * @throws
     */
    private void queryPersonByObjectId() {
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.getObject(objectId, new QueryListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                ShowToast("查询成功:名称->" + person.getName()+"，地址->" + person.getAddress());
            }
        });
    }

    /**
     * 分页查询
     */
    private void queryPersons() {
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        //注意分页的写法 count * pagesize
        bmobQuery.setLimit(pagesize).setSkip(count * pagesize).order("-createdAt").findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if(e == null) {
                    ShowToast("查到" + list.size() + "条数据");
                    count = ++count;
                    for (Person person : list) {
                        Log.i("test", person.getName() + " " + person.getAddress());
                    }
                }else {
                    ShowToast("出错了" + e.toString());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert_user:
                createPerson();
                break;

            case R.id.btn_update_user:
                updatePersonByObjectId();
                break;

            case R.id.btn_delete_user:
                deletePersonByObjectId();
                break;

            case R.id.btn_querry_user:
                queryPersonByObjectId();
                break;

            case R.id.btn_querry_datas:
                queryPersons();
                break;

            case  R.id.btn_go_list_activity:
                Intent intent = new Intent(this, RecycleViewActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }
    }
}
