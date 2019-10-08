package com.loftschool.ozaharenko.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;
    private ItemsAdapter mAdapter;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callAddButton = findViewById(R.id.call_add_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(MainActivity.this, AddItemActivity.class), REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.budget_item_list);

        mAdapter = new ItemsAdapter();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mAdapter.addItem(new Item("Молоко", 70));
        mAdapter.addItem(new Item("Зубная щетка", 70));
        mAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670));
    }

    @Override
    protected void onActivityResult(
            final int requestCode, final int resultCode, @Nullable final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;

        try {
            price = Integer.parseInt(data != null ? data.getStringExtra("price") : "0");
        } catch (NumberFormatException e) {
            price = 0;
        }
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mAdapter.addItem(new Item(data != null ? data.getStringExtra("name") : null, price));
        }
    }
}
