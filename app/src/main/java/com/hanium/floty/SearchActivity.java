package com.hanium.floty;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Vector;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vector<plant> vPlant = new Vector<plant>();

        String loadedXmlString = loadXML();


        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(loadedXmlString));

            int eventType = parser.getEventType();
            String tagName = "";
            plant p = null;

            while(eventType != XmlPullParser.END_DOCUMENT){

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if(parser.getName().equals("plant")){
                            p = new plant();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("plant")){
                            vPlant.add(p);
                            p=null;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        switch (tagName){
                            case "plantkor":
                                p.setPlantkor(parser.getText());
                                break;
                            case "planteng":
                                p.setPlanteng(parser.getText());
                                break;
                            case "temp":
                                p.setTemp(parser.getText());
                                break;
                            case "hum":
                                p.setHum(parser.getText());
                                break;
                            case "imgurl":
                                p.setImgurl(parser.getText());
                                break;
                        }
                        break;
                }

                eventType = parser.next();
            }

        }catch (Exception e){


        }


    }

    // load xml string to string
    private String loadXML() {
        AssetManager assetManager = getResources().getAssets();
        AssetManager.AssetInputStream ais = null;

        try{
            ais = (AssetManager.AssetInputStream)assetManager.open("plant.xml");

        }catch (Exception e){
            e.printStackTrace();
        }


        BufferedReader br = new BufferedReader(new InputStreamReader(ais));

        String line;
        StringBuilder sb = new StringBuilder();

        try{
            while((line = br.readLine()) != null) {
                sb.append(line);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return sb.toString();
    }
}