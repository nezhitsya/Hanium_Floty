package com.hanium.floty.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hanium.floty.R;
import com.hanium.floty.model.Plant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanxeState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        ArrayList<Plant> list = xmlParser();
        String[] data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i).getPlantkor()+" "+list.get(i).getPlanteng()
                    +" "+list.get(i).getTemp()+" "+list.get(i).getHum()+" "+list.get(i).getImgurl();

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                data);
        listView.setAdapter(adapter);
    }

    private ArrayList<Plant> xmlParser() {
        ArrayList<Plant> arrayList = new ArrayList<Plant>();
        InputStream is = getResources().openRawResource(R.raw.plant);

        // xmlPullParser
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();
            Plant plant = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("plant")) {
                            plant = new Plant();
                        }
                        if (startTag.equals("plantkor")) {
                            plant.setPlantkor(parser.nextText());
                        }
                        if (startTag.equals("planteng")) {
                            plant.setPlanteng(parser.nextText());
                        }
                        if (startTag.equals("temp")) {
                            plant.setTemp(parser.nextText());
                        }
                        if (startTag.equals("hum")) {
                            plant.setHum(parser.nextText());
                        }
                        if (startTag.equals("imgurl")) {
                            plant.setImgurl(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("plant")) {
                            arrayList.add(plant);
                        }
                        break;
                }
                eventType = parser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

}
}
