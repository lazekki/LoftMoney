package com.loftschool.ozaharenko.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Query;

public class BudgetFragment extends Fragment {

    private static final int REQUEST_CODE = 100;
    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    private ItemsAdapter mAdapter;

    private Api mApi;

    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftApp)getActivity().getApplication()).getApi();
        loadItems();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_budget, null);

        Button callAddButton = view.findViewById(R.id.call_add_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(getActivity(), AddItemActivity.class), REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        mAdapter = new ItemsAdapter(getArguments().getInt(COLOR_ID));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityResult(
            final int requestCode, final int resultCode, @Nullable final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;

        try {
            price = Integer.parseInt(data != null ? data.getStringExtra("price") : "");
        } catch (NumberFormatException e) {
            price = 0;
        }

        final int realPrice = price;

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final String name = data.getStringExtra("name");

            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");

            Call<Status> call = mApi.addItem(new AddItemRequest(name, getArguments().getString(TYPE), price), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(final Call<Status> call, final Response<Status> response) {
                    if (response.body().getStatus().equals("success")) {
                        mAdapter.addItem(new Item(name, realPrice));
                    }
                }

                @Override
                public void onFailure(final Call<Status> call, final Throwable t) {

                    t.printStackTrace();

                }
            });

        }
    }

    public void loadItems() {

        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");

        Call<List<Item>> items = mApi.getItems(getArguments().getString(TYPE), token);

        items.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call < List < Item >> call, Response<List<Item>> response) {

                List<Item> items = response.body();

                for (Item item : items) {
                    mAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure (Call < List < Item >> call, Throwable t){

            }

        });
    }
}
