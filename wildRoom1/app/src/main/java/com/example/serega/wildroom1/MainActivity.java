package com.example.serega.wildroom1;

        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    protected String getQuery(String workline){
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(workline);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    protected void updateView(String value){
        value = value.replaceAll("SPACE"," ");
        value = value.replaceAll("<br>","\n");
        _view.setText(value);
    }



    EditText _input;
    EditText _user;
    TextView _view;
    Button _send;
    Button _drop;
    public static int last;
    public static String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        last =0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _input = (EditText) findViewById(R.id.input);
        _view = (TextView) findViewById(R.id.view);
        _send = (Button) findViewById(R.id.send);
        _user = (EditText) findViewById(R.id._user);
        _drop = (Button) findViewById(R.id.drop);

        _send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "http://chat-razumov.rhcloud.com";
                msg+="/?usr=";
                //msg+=_user.getText().toString();
                String tmpusr = _user.getText().toString();
                tmpusr = tmpusr.replaceAll(" ","SPACE");
                msg+=tmpusr;
                msg+="&msg=";
                //msg+=_input.getText().toString();
                String tmpline = _input.getText().toString();
                tmpline = tmpline.replaceAll(" ","SPACE");
                msg+=tmpline;
                src=msg;

                new internetTask().execute();

            }
        });

        _drop.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                new drop().execute();
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new update().execute();
            }
        }, 0, 1000);
    }

    private class internetTask extends AsyncTask<Void,Void,Void> {

        String textResult;

        @Override
        protected Void doInBackground(Void... params){
            textResult = getQuery(src);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            updateView(textResult);
            super.onPostExecute(result);
        }


    }

    private class drop extends AsyncTask<Void, Void, Void>{

        String q = "http://chat-razumov.rhcloud.com/index.php?drop=true";

        @Override
        protected Void doInBackground(Void... params){
            getQuery(q);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class update extends AsyncTask<Void, Void, Void>{

        String u ="http://chat-razumov.rhcloud.com";
        String text;

        @Override
        protected Void doInBackground(Void... params){
            text = getQuery(u);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            updateView(text);
            super.onPostExecute(result);
        }
    }

}
