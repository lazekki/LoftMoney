package com.loftschool.ozaharenko.loftmoney;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private Api mApi;
    private TextView myExpences;
    private TextView myIncome;
    private TextView totalFinances;
    private DiagramView mDiagramView;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi =((LoftApp)getActivity().getApplication()).getApi();
        loadBalance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_balance, null);

        myExpences = view.findViewById(R.id.my_expenses);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.total_finances);
        mDiagramView = view.findViewById(R.id.diagram_view);

        return view;
    }

    private void loadBalance() {
        String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.TOKEN, "");
        Call<BalanceResponce> responceCall = mApi.getBalance(token);
        responceCall.enqueue(new Callback<BalanceResponce>() {
            @Override
            public void onResponse(Call<BalanceResponce> call, Response<BalanceResponce> response) {

                final float totalExpenses = response.body().getTotal_expenses();
                final float totalIncome = response.body().getTotal_income();

                myExpences.setText(String.valueOf(totalExpenses));
                myIncome.setText(String.valueOf(totalIncome));
                totalFinances.setText(String.valueOf(totalIncome - totalExpenses));
                mDiagramView.update(totalExpenses, totalIncome);
            }

            @Override
            public void onFailure(Call<BalanceResponce> call, Throwable t) {

            }
        });
    }

}
