package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExpenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewExpenses;
    private FloatingActionButton fabAddExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.expense_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set up the toolbar with back navigation
        Toolbar toolbar = findViewById(R.id.toolbarExpenseList);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        // TODO: Set layout manager and adapter for recyclerViewExpenses

        fabAddExpense = findViewById(R.id.fabAddExpense);
        fabAddExpense.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseListActivity.this, ExpenseDetailActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}