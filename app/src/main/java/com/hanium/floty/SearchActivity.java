package com.hanium.floty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class SearchActivity extends AppCompatActivity {

    EditText edit;
    TextView text;

    String key="7g8DMDPXcjtzoLwfDH0delE%2B5LC6oQ9Q0NZT3O2STlgzydgE0BftsVOXt3TumvVVzbG0P8Y1ShlsCfsPkjweRw%3D%3D";

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit= (EditText)findViewById(R.id.edit);
        text= (TextView)findViewById(R.id.text);
    }


    public void mOnClick(View v){

        switch( v.getId() ){
            case R.id.button:

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data= getXmlData();


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data);
                            }
                        });

                    }
                }).start();

                break;
        }

    }
    String getXmlData(){

        StringBuffer buffer=new StringBuffer();

        String str= edit.getText().toString();
        String search = URLEncoder.encode(str);


        String queryUrl="http://openapi.nature.go.kr/openapi/service/rest/PlantService/plntIlstrSearch?serviceKey="//요청 URL
                +key+"&st=1&sw="+ search
                +"&numOfRows=10&pageNo=1";


        try {
            URL url= new URL(queryUrl);
            InputStream is= url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") );

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("시작\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) ;
                        else if(tag.equals("familyKorNm")){
                            buffer.append("과국명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("familyNm")){
                            buffer.append("과명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("genusKorNm")){
                            buffer.append("속국명  :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("genusNm")){
                            buffer.append("속명  :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("imgUrl")){
                            buffer.append("이미지url :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("lastUpdtDtm")){
                            buffer.append("최종수정일시 :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("  ,  ");
                        }
                        else if(tag.equals("+notRcmmGnrlNm")){
                            buffer.append("비추천명  :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("plantGnrlNm")){
                            buffer.append("국명  :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("plantPilbkNo")){
                            buffer.append("도감번호  :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("+plantSpecsScnm ")){
                            buffer.append("정명학명  :");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) buffer.append("\n");

                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("끝\n");

        return buffer.toString();

    }
}