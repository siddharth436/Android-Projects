package com.mobileApplicationDevelopment.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileApplicationDevelopment.stockwatch.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    List<Stock> stockList = new ArrayList<>();
    HashMap<String, String> symbolMap;
    RecyclerView recyclerView;
    StocksAdapter stocksAdapter;
    SwipeRefreshLayout refreshLayout;
    MySQLDatabase sqlDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.swipelayout);

        stocksAdapter = new StocksAdapter(stockList, this);
        Log.d(TAG, "onCreate: "+stocksAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stocksAdapter);

        refreshLayout.setProgressViewOffset(true, 0, 200);
        refreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {
                    refreshLayout.setRefreshing(false);
                    errorUpdateDialog();
                } else {
                    refreshData();
                }
            }
        });

        sqlDatabase = new MySQLDatabase(this);
        new Nameloader(MainActivity.this).execute();
        ArrayList<Stock> tempList = sqlDatabase.loadStocks();
        if (!isNetworkAvailable()) {
            stockList.addAll(tempList);
            Collections.sort(stockList, new Comparator<Stock>() {
                @Override
                public int compare(Stock o1, Stock o2) {
                    return o1.getCompanySymbol().compareTo(o2.getCompanySymbol());
                }
            });
            stocksAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < tempList.size(); i++) {
                String symbol = tempList.get(i).getCompanySymbol();
                new Stockloader(MainActivity.this).execute(symbol);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+stockList.size());
        stocksAdapter.notifyDataSetChanged();
    }

    private void refreshData() {
        refreshLayout.setRefreshing(false);
        ArrayList<Stock> tempList = sqlDatabase.loadStocks();
        for (int i = 0; i < tempList.size(); i++) {
            String symbol = tempList.get(i).getCompanySymbol();
            new Stockloader(MainActivity.this).execute(symbol);
        }
        Snackbar.make(findViewById(R.id.constraintlayout), "Data Refreshed", Snackbar.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Snackbar.make(findViewById(R.id.constraintlayout), "An Error Occurred, Please restart the app", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot be Added without A Network Connection");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void errorUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot be Updated without A Network Connection");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: start");
        if (!isNetworkAvailable()) {
            errorDialog();
            return false;
        } else {
            switch (id) {
                case R.id.add_stock:
                    Log.d(TAG, "onOptionsItemSelected: start");
                    openDialogForStock();
                    Log.d(TAG, "onOptionsItemSelected: end/dialog");
                    return true;
                default:
                    Log.d(TAG, "onOptionsItemSelected: end/default");
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    private void openDialogForStock() {
        if(symbolMap == null){
            new Nameloader(MainActivity.this).execute();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stock Selection");
        builder.setMessage("Please enter a Stock Symbol:");
        builder.setCancelable(false);

        final EditText editText = new EditText(this);


        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        editText.setGravity(Gravity.CENTER_HORIZONTAL);




        builder.setView(editText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!isNetworkAvailable()){          //network not available
                    errorDialog();
                    return;
                }
                else if(editText.getText().toString().isEmpty()){           //empty search
                    Toast.makeText(MainActivity.this, "Please Enter Valid Input", Toast.LENGTH_SHORT).show();
                    return;
                }else {                     //search
                    ArrayList<String> tempList = searchStock(editText.getText().toString());
                    if(!tempList.isEmpty()){
                        ArrayList<String> stockOptions = new ArrayList<>(tempList);
                        if(stockOptions.size() == 1){
                            if(isDuplicateStock(stockOptions.get(0))){
                                duplicateItemDialog(editText.getText().toString());
                            }
                            else {
                                saveStock(stockOptions.get(0));
                            }
                        }
                        else {              //Multiple Stocks Exist
                            multipleStocksExistDialog(editText.getText().toString(),stockOptions,stockOptions.size());
                        }
                    }
                    else {
                        notSuchDataDialog(editText.getText().toString());
                    }

                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void notSuchDataDialog(String toString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Symbol Not Found: "+toString.toUpperCase() );
        builder.setMessage("Data for stock symbol");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void multipleStocksExistDialog(final String s, ArrayList<String> stockOptions, int size) {
        final String[] strings = new String[size];
        for(int i=0;i<strings.length;i++){
            strings[i]=stockOptions.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a Selection");
        builder.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isDuplicateStock(strings[which])){
                    duplicateItemDialog(s);
                }
                else {
                    saveStock(strings[which]);
                }
            }
        });
        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveStock(String s) {
        String symbol = s.split("-")[0].trim();
        new Stockloader(MainActivity.this).execute(symbol);
        Stock temp_stock = new Stock();
        temp_stock.setCompanySymbol(symbol);
        temp_stock.setCompanyName(symbolMap.get(symbol));
        sqlDatabase.addStock(temp_stock);

    }

    private void duplicateItemDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Duplicate Stock");
        builder.setIcon(R.drawable.baseline_warning_black_48);

        builder.setMessage("Stock symbol "+s.toUpperCase()+" already displayed");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isDuplicateStock(String s) {
        Log.d(TAG, "isDuplicateStock: ");
        String symbol = s.split("-")[0].trim();
        Stock temp = new Stock();
        temp.setCompanySymbol(symbol);
        return stockList.contains(temp);
    }

    private ArrayList<String> searchStock(String s) {
        ArrayList<String> stockOption = new ArrayList<>();
        if(symbolMap != null && !symbolMap.isEmpty()) {
            for (String symbol : symbolMap.keySet()) {
                String name = symbolMap.get(symbol);
                if (symbol.toUpperCase().contains(s.toUpperCase())) {
                    stockOption.add(symbol + " - " + name);
                } else if (name.toUpperCase().contains(s.toUpperCase())) {
                    stockOption.add(symbol + " - " + name);
                }

            }
        }
        return stockOption;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.menu_items, menu);
        Log.d(TAG, "onCreateOptionsMenu: end");
        return true;

    }

    @Override
    public void onClick(View v) {
        int i = recyclerView.getChildLayoutPosition(v);
        String marketPlaceURL = "http://www.marketwatch.com/investing/stock/" + stockList.get(i).getCompanySymbol();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(marketPlaceURL));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        final int id = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Stock");
        builder.setIcon(R.drawable.baseline_delete_black_48);
        builder.setMessage("Delete Stock Symbol "+((TextView)v.findViewById(R.id.companysymbolTextView)).getText().toString().toUpperCase()+" ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ///TODO remove stock and notify all
                sqlDatabase.deleteStock(stockList.get(id).getCompanySymbol());
                stockList.remove(id);
                stocksAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    public void setData(HashMap<String, String> hashMap) {
        if (hashMap != null && !hashMap.isEmpty()) {
            this.symbolMap = hashMap;
        }
    }

    public void setStock(Stock stock) {
        if (stock != null) {
            Log.d(TAG, "setStock: In Stock !=null condition");
                int index = stockList.indexOf(stock);
            Log.d(TAG, "setStock: The index "+index);
            if (index > -1) {
                Log.d(TAG, "setStock: In Stock index");
                stockList.remove(index);
            }
            stockList.add(stock);
            Collections.sort(stockList, new Comparator<Stock>() {
                @Override
                public int compare(Stock o1, Stock o2) {
                    return o1.getCompanySymbol().compareTo(o2.getCompanySymbol());
                }
            });
            stocksAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqlDatabase.shutDown();
    }
}
