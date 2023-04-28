package com.example.realtimechat.chat;

import static com.example.realtimechat.App.CHANNEL_1_ID;
import static com.example.realtimechat.chat.Config.IP_CONFIG;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.realtimechat.R;
import com.example.realtimechat.utils.SharedPreferenceUtil;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    final String URL_SERVER = Config.URL_SERVER_CHAT;

    private NotificationManagerCompat notificationManager;

    TextView txtFullName;
    EditText edtMessage;
    ImageView btnSend;
    ImageView back;
    ListView listView;
    TextView txtNumOfRoom;

    String roomID;

    Socket socket;

    {
        try {
            socket = IO.socket(URL_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    List<Message> list = new ArrayList<>();
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        socket.connect();

        notificationManager = NotificationManagerCompat.from(this);

        txtFullName = findViewById(R.id.txtFullName);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        listView = findViewById(R.id.msgShow);
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomID");
        String userId = intent.getStringExtra("userId");
        String email = intent.getStringExtra("email_friend");

        txtFullName.setText(email);

        chatAdapter = new ChatAdapter(Chat.this, list);
        listView.setAdapter(chatAdapter);

        socket.on("server-send-message", onNewMessage);
        socket.on("socket-send-room", onNumOfRoom);
        socket.on("server-send-num-in-room", onUpdateNumOfRoom);
        socket.on("socket-typing", onSbIsTyping);
        socket.on("socket-stop-typing", onSbStopTyping);

        edtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    socket.emit("typing");
                } else {
                    socket.emit("stop-typing");
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMessage.getText().length() <= 0){
                    Toast.makeText(Chat.this, "Input a message", Toast.LENGTH_LONG).show();
                } else {
                    Gson gson = new Gson();
                    Message message = new Message ("", edtMessage.getText().toString(), roomID, SharedPreferenceUtil.getUserId(),true);
                    System.out.println(gson.toJson(message));
                    socket.emit("client-send-message", gson.toJson(message));
                    list.add(new Message("", edtMessage.getText().toString(), roomID, SharedPreferenceUtil.getUserId(), true));
                    listView.setSelection(list.size() - 1);
                    edtMessage.setText("");
                    chatAdapter.notifyDataSetChanged();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        callDataChatOld(Chat.this);
    }

    public void sendOnChannel1(String name, String content) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), uri);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.android_chat);

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.android_chat)
                    .setLargeIcon(bitmap)
                    .setContentTitle(name)
                    .setContentText(content)
                    .setAutoCancel(true)
                     .build();

        r.play();
        notificationManager.notify(1, notification);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(args[0].toString());

                    Gson gson = new Gson();
                    Message message = gson.fromJson(args[0].toString(), Message.class);

                    message.setMe(SharedPreferenceUtil.getUserId() == message.getUserId());
                    if (SharedPreferenceUtil.getUserId() != message.getUserId()) {
                        list.add(message);
                        listView.setSelection(list.size() - 1);
                    }

                    chatAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    private void callDataChatOld(Context context) {
        Intent intent = getIntent();
        String roomID = intent.getStringExtra("roomID");

        String url = IP_CONFIG+"/messages?roomId=" + roomID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int MyID = SharedPreferenceUtil.getUserId();
                            for (int i = 0; i< response.length(); i++) {
                                JSONObject main = response.getJSONObject(i);
                                Long id = main.getLong("id");
                                String name = main.getString("email");
                                String content = main.getString("content");
                                Integer userId = main.getInt("userId");
                                boolean me =  userId == MyID ;
                                list.add(new Message(name, content, roomID, userId, me));
                            }
                            chatAdapter.notifyDataSetChanged();
                            listView.setSelection(list.size() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private Emitter.Listener onNumOfRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    String numOfOnline;
                    try {
                        numOfOnline = object.getString("numOfOnline");
                        txtNumOfRoom.setText(numOfOnline);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onUpdateNumOfRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = args[0].toString();
                    txtNumOfRoom.setText(data);
                }
            });
        }
    };

    private Emitter.Listener onSbIsTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edtMessage.setHint("Someone is typing...");
                }
            });
        }
    };

    private Emitter.Listener onSbStopTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edtMessage.setHint("Write a message...");
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
