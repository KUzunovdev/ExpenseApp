package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.models.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    Expense getExpenseById(int id);

    @Query("SELECT SUM(amount) FROM expenses")
    Double getTotalExpenses();

}
