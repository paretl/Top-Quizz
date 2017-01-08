package cpe.top.quizz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.jar.Attributes;

import cpe.top.quizz.asyncTask.StatisticTask;
import cpe.top.quizz.asyncTask.responses.AsyncStatisticResponse;
import cpe.top.quizz.beans.Quizz;
import cpe.top.quizz.beans.ReturnObject;
import cpe.top.quizz.beans.Statistic;
import cpe.top.quizz.beans.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class Chat extends AppCompatActivity {

    private static final String USER = "USER";

    private Bundle bundle;
    private User connectedUser;

    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private Activity activity = (Activity) ((Context) this);
    private ListView messagesView;

    private Socket mSocket; {
        try {
            mSocket = IO.socket("http://163.172.91.2:3000/");
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String name;
                    String text;
                    try {
                        name = data.getString("name");
                        text = data.getString("text");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(name, text);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        // Connection à la socket
        mSocket.on("chat", onNewMessage);
        mSocket.connect();

        Intent intent = getIntent();
        connectedUser = (User) getIntent().getSerializableExtra(USER);

        messageAdapter = new MessageAdapter(this, new ArrayList<Message>(), connectedUser.getPseudo());
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        //get our input field byits ID
        messageInput = (EditText)findViewById(R.id.message_input);

        //get our button bu its ID
        sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });


    }

    public void onBackPressed(){
        Intent intent = new Intent(Chat.this, Home.class);
        intent.putExtra(USER, connectedUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    private void attemptSend() {
        String message = messageInput.getText().toString().trim();
        //Remplacer pseudo par connectedUser
        String stringJsonObject = "{\"name\":\"" + connectedUser.getPseudo() + "\", \"text\":\"" + message + "\"}";
        if (TextUtils.isEmpty(message)) {
            return;
        }
        try {
            JSONObject messageJ = new JSONObject(stringJsonObject);
            messageInput.setText("");
            mSocket.emit("chat" , messageJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(String name, String text) {
        System.out.println("Message reçu de :" + name + "\tMessage :" + text);
        Message msg = new Message();

        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        String curTime = String.format("%02d:%02d", hours, minutes);

        msg.name = name + " (" + curTime + ")";
        msg.text = text;

        messageAdapter.add(msg);
        messagesView.setSelection(messageAdapter.getCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                // Destroy user and return to main activity
                connectedUser = null;
                Toast.makeText(this, "A bientôt !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Chat.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
