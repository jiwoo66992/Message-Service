package com.example.realtimechat.home;


import static com.example.realtimechat.chat.Config.IP_CONFIG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.realtimechat.R;
import com.example.realtimechat.adapter.AccountAdapter;
import com.example.realtimechat.adapter.ChatAdapter;
import com.example.realtimechat.adapter.ClickAccount;
import com.example.realtimechat.adapter.ClickChat;
import com.example.realtimechat.chat.Chat;
import com.example.realtimechat.chat.Config;
import com.example.realtimechat.infor.InformationActivity;
import com.example.realtimechat.login.LoginActivity;
import com.example.realtimechat.model.Account;
import com.example.realtimechat.model.RoomChat;
import com.example.realtimechat.model.User;
import com.example.realtimechat.server.ApiService;
import com.example.realtimechat.utils.DataUtil;
import com.example.realtimechat.utils.SharedPreferenceUtil;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeActivity extends AppCompatActivity implements ClickAccount, ClickChat {

    EditText edtTimKiem;
    ImageView imgMore, imgAvt;
    RecyclerView rcySearch, rcyChat;

    TextView txtStatusNew, txtName;

    AccountAdapter accountAdapter;
    ChatAdapter chatAdapter;

    View viewNull;

    final String URL_SERVER = Config.URL_SERVER_CHAT;

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    Socket socket;

    {
        try {
            socket = IO.socket(URL_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        socket.connect();

        accountAdapter = new AccountAdapter(HomeActivity.this, new ArrayList<>(), this);
        chatAdapter = new ChatAdapter(HomeActivity.this, new ArrayList<>(), this);
        initViewById();

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callDataAccount(HomeActivity.this, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rcySearch.setAdapter(accountAdapter);

        rcyChat.setAdapter(chatAdapter);

        viewNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountAdapter.setData(new ArrayList<>());
            }
        });


        final WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
        }

        boolean checkOnline = isOnline();
        if (!checkOnline) {
            Toast.makeText(HomeActivity.this, getText(R.string.requires_network), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(HomeActivity.this, getText(R.string.connect_network_success), Toast.LENGTH_SHORT).show();
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuMore(view);
            }
        });

        imgAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, InformationActivity.class));
            }
        });

        callUser();
    }

    @Override
    public void clickAccount(Account account) {
        onChat(HomeActivity.this, account.getId(), account.getEmail());
    }

    private void popupMenuMore(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });

        popupMenu.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        onRoomChat(this);
        callDataAccount(this, edtTimKiem.getText().toString().trim());
        if (DataUtil.user != new User()) {
            txtName.setText(DataUtil.user.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void clickChat(RoomChat roomChat) {
        socket.emit("client-send-roomId", roomChat.getRoomId());
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("roomID", roomChat.getRoomId());
        intent.putExtra("email_friend", roomChat.getEmail());
        startActivity(intent);
    }

    private void initViewById() {
        edtTimKiem = findViewById(R.id.edtTimKiem);
        imgMore = findViewById(R.id.imgMore);
        rcySearch = findViewById(R.id.rcySearch);
        rcyChat = findViewById(R.id.rcyChat);
        viewNull = findViewById(R.id.viewNull);
        txtStatusNew = findViewById(R.id.txtStatusNew);
        txtName = findViewById(R.id.txtName);
        imgAvt = findViewById(R.id.imgAvt);
    }

    private boolean clickBack = false;

    @Override
    public void onBackPressed() {
        if (clickBack) {
            finish();
        } else {
            clickBack = true;
            Toast.makeText(this, getText(R.string.click_back), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickBack = false;
                }
            }, 1000L);
        }
    }

    private void callDataAccount(Context context, String key) {
        ArrayList<Account> list = new ArrayList<>();

        String url = IP_CONFIG + "/users/email/" + SharedPreferenceUtil.getUserId() + "?key=" + key.trim();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject main = response.getJSONObject(i);
                                int id = main.getInt("id");
                                String email = main.getString("email");
                                String password = main.getString("password");
                                list.add(new Account(id, email, password));
                            }
                            if (list.size() > 0) {
                                txtStatusNew.setVisibility(View.VISIBLE);
                            }
                            accountAdapter.setData(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            accountAdapter.setData(new ArrayList<>());
                            txtStatusNew.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                accountAdapter.setData(new ArrayList<>());
                txtStatusNew.setVisibility(View.GONE);
            }
        });
        requestQueue.add(request);
    }

    private void onChat(Context context, int friendId, String email_friend) {
        String url = IP_CONFIG + "/rooms?userId=" + SharedPreferenceUtil.getUserId()
                + "&friendId=" + friendId;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String roomId = response.getString("convertId");
                            socket.emit("client-send-roomId", roomId);
                            Intent intent = new Intent(context, Chat.class);
                            intent.putExtra("roomID", roomId);
                            intent.putExtra("email_friend", email_friend);
                            accountAdapter.setData(new ArrayList<>());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, getText(R.string.connection_errors), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    private void onRoomChat(Context context) {
        String url = IP_CONFIG + "/rooms/" + SharedPreferenceUtil.getUserId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<RoomChat> list = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String roomId = object.getString("convertId");
                                String email = object.getString("email");
                                String lastMessage = object.getString("lastMessage");

                                list.add(new RoomChat(roomId, email, lastMessage));

                            }

                            chatAdapter.setData(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            chatAdapter.setData(new ArrayList<>());
                            Toast.makeText(context, getText(R.string.connection_errors), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                chatAdapter.setData(new ArrayList<>());
            }
        });
        requestQueue.add(request);
    }

    private void callUser() {
        ApiService.apiService.getUser(SharedPreferenceUtil.getAccount()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                DataUtil.user = response.body();
                txtName.setText(DataUtil.user.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }

}