package com.loftschool.ozaharenko.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.loftschool.ozaharenko.loftmoney.MainActivity.TOKEN;

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

        mAdapter.addItem(new Item("Молоко", 70));
        mAdapter.addItem(new Item("Зубная щетка", 70));
        mAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670));

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

            Call<Status> call = mApi.addItem(new AddItemRequest(name, getArguments().getString(TYPE), price), TOKEN);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body().getStatus().equals("success")) {
                        mAdapter.addItem(new Item(name, realPrice));
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            mAdapter.addItem(new Item(data != null ? name : null, price));
        }
    }

}
