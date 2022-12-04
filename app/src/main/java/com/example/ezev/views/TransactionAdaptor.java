package com.example.ezev.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezev.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        TransactionDetails transactionDetails=transactionDetailsList.get(position);
        //holder.date.setText(transactionDetails.getDate());
        //Timestamp timestamp=obj.getTimestamp();
        Timestamp timestamp=transactionDetails.getTimestamp();
        Date ds = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        String trans_time= dateFormat.format(ds);
        holder.date.setText(trans_time);
        holder.amount.setText(Long.toString(transactionDetails.getAmount()));
        holder.station.setText(transactionDetails.getStation_name());
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
