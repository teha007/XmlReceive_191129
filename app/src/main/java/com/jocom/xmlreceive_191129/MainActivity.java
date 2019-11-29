// 닷홈에 영화 xml 올리고 파싱 받아오기

package com.jocom.xmlreceive_191129;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button btn;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
    }

    public void clickBtn(View view) {

        final String abc = et.getText().toString();
//        btn.setEnabled(false);

        Thread t = new Thread() {
            @Override
            public void run() {
                String address = "http://chosangho1234.dothome.co.kr/searchDailyBoxOfficeList.xml";

                try {
                    //해임달객체 생성
                    URL url = new URL(address);

                    //무지개로드 열기
                    InputStream is = url.openStream(); //바이트스트림
                    //문자스트림으로 변환
                    InputStreamReader isr = new InputStreamReader(is);

                    //읽어들인 XML문서를 분석(parse)해주는 객체 생성
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(isr);

                    //xpp를 이용해서 xml문서를 분석
                    int eventType = xpp.getEventType();

                    String tagName;
                    StringBuffer buffer = null;

                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast t = Toast.makeText(MainActivity.this, "파싱 시작!", Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.TOP, 0, 200);
                                        t.show();
                                    }
                                });

                                break;

                            case XmlPullParser.START_TAG:
                                tagName = xpp.getName();

                                if (tagName.equals("dailyBoxOffice")) {
                                    buffer = new StringBuffer();
                                } else if (tagName.equals("rank")) {
                                    buffer.append("순위:");
                                    xpp.next();
                                    buffer.append(xpp.getText() + "\n");

                                } else if (tagName.equals("movieNm")) {
                                    buffer.append("제목:");
                                    xpp.next();
                                    buffer.append(xpp.getText() + "\n");

                                } else if (tagName.equals("openDt")) {
                                    buffer.append("개봉일:");
                                    xpp.next();
                                    buffer.append(xpp.getText() + "\n");

                                } else if (tagName.equals("audiAcc")) {
                                    buffer.append("누적관객수:");
                                    xpp.next();
                                    buffer.append(xpp.getText() + "\n");
                                }

                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                tagName = xpp.getName();
                                if (tagName.equals("dailyBoxOffice")) {

                                    assert buffer != null;
                                    tv.append(buffer.toString());

                                }
                                break;
                        }

                        eventType = xpp.next();
                    }//while..

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "파싱 종료!!", Toast.LENGTH_SHORT).show();

                        }
                    });

//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         try {
//                             sleep(10000);
//                             btn.setEnabled(true);
//                         } catch (InterruptedException e) {
//                             e.printStackTrace();
//                         }
//                     }
//                 });

                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }

            }//run method...

        };
        t.start();

        try {
            t.join();
        } catch (
                InterruptedException e) {
            e.printStackTrace();

        }
    }

}
