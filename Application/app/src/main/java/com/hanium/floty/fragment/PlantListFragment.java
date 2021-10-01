package com.example.srh7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PlantListFragment extends Activity implements View.OnClickListener{

    private ArrayList<Plant> data = null;
    ListView listview = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friends_list);

        ListView listView = (ListView) findViewById(R.id.friends_listview);

        data = new ArrayList<>();

        Plant plantds1= new Plant(R.drawable.tulip2, "튤립", "Tulip");

        Plant plantds2 = new Plant(R.drawable.rose2, "장미", "Rose");

        Plant plantds3 = new Plant(R.drawable.peppermint2, "페머민트", "Peppermint");

        //리스트에 추가
        data.add(plantds1);
        data.add(plantds2);
        data.add(plantds3);


        /* 리스트 속의 아이템 연결
        FriendsAdapter adapter = new FriendsAdapter(this, R.layout.friends_item, data);
        listView.setAdapter(adapter);
*/
        PlantsAdapter adapter;
        adapter = new PlantsAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.friends_listview);
        listview.setAdapter(adapter);


        EditText editTextFilter = (EditText)findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText) ;
                } else {
                    listview.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;


        /* 아이템 클릭시 작동 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlantClicked.class);
                /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                intent.putExtra("profile", Integer.toString(data.get(position).getImgurl()));
                intent.putExtra("plantkor", data.get(position).getPlantkor());
                intent.putExtra("palnteng", data.get(position).getPlanteng());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
    }

}
