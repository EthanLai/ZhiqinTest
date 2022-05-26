package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.net.URI;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int MAX_SIZE = 40;

    private RecyclerView mRecyclerView = null;
    private ListAdapter mListAdapter = null;
    private WebSocket mWebSocket = null;
    private BinanceApiService binanceApiService;
    private PriorityQueue<Entity> mItemQueue = new PriorityQueue<>();
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.item_list);
        getSupportActionBar().hide();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        initializeComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateAdapter();
            }
        }, 1500, 5000);
        initRestfulApi();
        mListAdapter = new ListAdapter(getApplication());
        mListAdapter.updateList(mItemQueue);
        mRecyclerView.setAdapter(mListAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebSocket != null) {
            mWebSocket.close();
        }
    }

    private void initializeComponents() {
        mTimer = new Timer();
        mItemQueue = new PriorityQueue<>(MAX_SIZE, new Comparator<Entity>() {
            @Override
            public int compare(Entity entity1, Entity entity2) {
                if ((entity2.mTimeL - entity1.mTimeL) > 0) {
                    return 1;
                }
                return -1;
            }
        });
    }

    private void initRestfulApi() {
        binanceApiService = RetrofitManager.getInstance().getAPI();
        Call<List<AggTrade>> call = binanceApiService.getTrades("BTCUSDT", 1);
//        Call<List<AggTrade>> call = binanceApiService.getAggTrades("BTCUSDT", null, 500, null, null);
        call.enqueue(new Callback<List<AggTrade>>() {
            @Override
            public void onResponse(Call<List<AggTrade>> call, Response<List<AggTrade>> response) {
                if (response.isSuccessful()) {
                    for (AggTrade trade : response.body()) {
                        mItemQueue.offer(new Entity(Long.MAX_VALUE, new Date(trade.getTradeTime()), trade.getPrice(), trade.getQuantity() == null ? "xxxxxxxxxxx" : trade.getQuantity()));
                        while (mItemQueue.size() > MAX_SIZE) {
                            mItemQueue.poll();
                        }
                        initOkHttp();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AggTrade>> call, Throwable t) {
                Log.e("MainActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initOkHttp() {
        URI uri = URI.create("wss://stream.yshyqxx.com/ws/btcusdt@aggTrade");
        mWebSocket = new WebSocket(uri) {
            @Override
            public void onMessage(String message) {
                Date date = new Date();
                String time = "", price = "", quantity = "";
                for (String str : message.split(",")) {
                    String key = str.split(":")[0].replace("\"", "");
                    String value = str.split(":")[1];
                    if (key.equals("T")) {
                        time = value;
                        date = new Date(Long.valueOf(value));
                    }
                    if (key.equals("p")) {
                        price = value.replace("\"", "");
                    }
                    if (key.equals("q")) {
                        quantity = value.replace("\"", "");
                    }
                }
                mItemQueue.offer(new Entity(Long.valueOf(time), date, price, quantity));
                while (mItemQueue.size() > MAX_SIZE) {
                    mItemQueue.poll();
                }
            }
        };
        try {
            mWebSocket.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateAdapter() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                if (mListAdapter != null) {
                    mListAdapter.updateList(mItemQueue);
                    mListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}