package com.example.ezev.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezev.R;

import java.util.ArrayList;

public class TransactionAdaptor extends RecyclerView.Adapter<TransactionAdaptor.TransactionViewHolder>{
    private Context context;
    ArrayList<TransactionDetails> transactionDetailsList;

    public TransactionAdaptor(Context context, ArrayList<TransactionDetails> transactionDetailsList){
        this.context=context;
        this.transactionDetailsList=transactionDetailsList;
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.transactions_row,parent,false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdaptor.TransactionViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return transactionDetailsList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView date,amount,station;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.textViewDate);
            amount=itemView.findViewById(R.id.textViewAmount);
            station=itemView.findViewById(R.id.textViewStationName);
        }
    }
}
