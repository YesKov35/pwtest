package com.example.lnik3.pwtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String[] mGroupsArray = new String[] { "Transfer to", "Transfer from" };

    List<Transfer> transfers;
    Spinner sp;
    TextView count;
    User user;
    List<User> users;
    ArrayAdapter<String> adapter;
    private static final String PREF_FORM = "PREF_FORM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = findViewById(R.id.count);
        Button go = findViewById(R.id.go);
        sp = findViewById(R.id.name);

        update();

        //обработка нажатия на кнопку go
        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = Integer.parseInt(user.getBalance()) - Integer.parseInt(count.getText().toString());
                if(n < 0){

                }else{
                    String id_to = "";
                    for(User ur: users){
                        if(ur.getLabel().contains(sp.getSelectedItem().toString())){
                            id_to = ur.getId();
                        }
                    }
                    goTransfer(user.getId().toString(), id_to, count.getText().toString());
                    update();
                }
            }
        });

        final Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Logout(v.getContext());
            }
        });
    }

    //Завершение сеанса пользователя
    private void Logout(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_FORM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //выполнение трансфера
    private void goTransfer(String from, String to, String amt){
        if(!TextUtils.isEmpty(count.getText())){
            DataBase dataBase = new DataBase(this);
            String check = "";
            String type = "insert_data";
            try {
                check = dataBase.execute(type, from, to, amt).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    //получение данных
    private String getData(String user_id) {
        DataBase dataBase = new DataBase(this);
        String check = "";
        String type = "get_data";
        try {
            check = dataBase.execute(type, user_id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return check;
    }
    //обновление данных на форме
    private void update(){
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        String data = getData(user_id);

        String[] str2 = data.split("#ar#");
        String[] str = str2[0].split("#dat#");

        Gson json = new Gson();
        transfers = new ArrayList<>();
        user = new User();
        users = new ArrayList<>();

        TextView tx = findViewById(R.id.title);

        for(int i = 0; i < str.length; i++){
            if(i == 0){
                user = json.fromJson(str[0], User.class);
            }else{
                transfers.add(json.fromJson(str[i], Transfer.class));
            }
        }

        String[] str3 = str2[1].split("#dat#");
        for(String n: str3){
            if(!TextUtils.isEmpty(n)) {
                users.add(json.fromJson(n, User.class));
            }
        }

        List<String> names = new ArrayList<>();
        for(User ur: users){
            if(!ur.getLabel().equals(user.getLabel())){
                names.add(ur.getLabel());
            }

        }

        adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, names);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        tx.setText(user.getLabel() + " : " + user.getBalance());

        Map<String, String> map;
        // коллекция для групп
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        // заполняем коллекцию групп из массива с названиями групп

        for (String group : mGroupsArray) {
            // заполняем список атрибутов для каждой группы
            map = new HashMap<>();
            map.put("groupName", group); // время года
            groupDataList.add(map);
        }

        // список атрибутов групп для чтения
        String groupFrom[] = new String[] { "groupName" };
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[] { android.R.id.text1 };

        // создаем общую коллекцию для коллекций элементов
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();

        // в итоге получится сhildDataList = ArrayList<сhildDataItemList>

        // создаем коллекцию элементов для первой группы
        ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
        // заполняем список атрибутов для каждого элемента

        for (int i = 0; i < transfers.size(); i++) {
            if(transfers.get(i).getId_from().contains(user_id)){
                map = new HashMap<>();
                map.put("monthName", ":" + transfers.get(i).getName_to() + ":  -" + transfers.get(i).getCount()); // название месяца
                сhildDataItemList.add(map);
            }

        }
        // добавляем в коллекцию коллекций
        сhildDataList.add(сhildDataItemList);

        сhildDataItemList = new ArrayList<>();
        // создаем коллекцию элементов для второй группы
        for (int i = 0; i < transfers.size(); i++) {
            if(transfers.get(i).getId_to().contains(user_id)){
                map = new HashMap<>();
                map.put("monthName", ":" + transfers.get(i).getName_from() + ":  +" + transfers.get(i).getCount());
                сhildDataItemList.add(map);
            }

        }
        сhildDataList.add(сhildDataItemList);


        // список атрибутов элементов для чтения
        String childFrom[] = new String[] { "monthName" };
        // список ID view-элементов, в которые будет помещены атрибуты
        // элементов
        int childTo[] = new int[] { android.R.id.text1 };

        final SimpleExpandableListAdapter adapter2 = new SimpleExpandableListAdapter(
                this, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        final ExpandableListView expandableListView = findViewById(R.id.expListView);

        //обработка нажатия на элемент ExpandableListView
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(
                    ExpandableListView parent, View v,
                    int groupPosition, int childPosition,
                    long id) {
                final String selected = adapter2.getChild(
                        groupPosition, childPosition).toString();
                String[] data = selected.split(":");
                sp.setSelection(adapter.getPosition(data[1]));
                String number  = data[2].replaceAll("\\D+","");
                count.setText(number);
                return false;
            }
        });
        expandableListView.setAdapter(adapter2);
    }

}
