package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private List<Expense> expenseList;

    // Constructor
    public ExpenseAdapter(Context context) {
        this.context = context;
        this.expenseList = new ArrayList<>();
    }

    // Method to update data in the adapter
    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the expense_item layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        // Bind the data for each expense item
        Expense expense = expenseList.get(position);
        holder.tvDescription.setText(expense.getDescription());
        holder.tvAmount.setText(String.format("$%.2f", expense.getAmount()));
        holder.tvDate.setText(expense.getDate());

        // Create an intent to launch ExpenseDetailActivity in edit mode
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExpenseDetailActivity.class);
            intent.putExtra("expense_id", expense.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder class that holds references to each item's views
    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvAmount, tvDate;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvExpenseDescription);
            tvAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvDate = itemView.findViewById(R.id.tvExpenseDate);
        }
    }
}
