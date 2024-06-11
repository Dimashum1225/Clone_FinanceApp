package com.example.clone_financeapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clone_financeapp.model.Group
import com.example.clone_financeapp.model.Item
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddIncomesActivity : AppCompatActivity() {

    lateinit var pickDateBtn: Button
    lateinit var pickCategoryBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_incomes)

        pickDateBtn = findViewById(R.id.choose_date)
        pickCategoryBtn = findViewById(R.id.choose_category)




        pickCategoryBtn.setOnClickListener{
            showChooseIncomesCategoryDialog(this){ category ->
                pickCategoryBtn.text = category
            }

        }
        pickDateBtn.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    pickDateBtn.text = dateFormat.format(selectedDate.time)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


    }
}
    private fun showChooseIncomesCategoryDialog(context: Context,onCategorySelected:(String) -> Unit){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choose_incomes_category)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewIncomes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val groups = listOf(
            Group("Group 1", listOf(Item("Item 1.1"), Item("Item 1.2"))),
            Group("Group 2", listOf(Item("Item 2.1"), Item("Item 2.2")))
        )
        recyclerView.adapter = ExpandableAdapter(groups){item,position ->
            Toast.makeText(context, "Был выбран пункт ${item.name}", Toast.LENGTH_SHORT).show()
            onCategorySelected(item.name)
            dialog.dismiss()
        }
        dialog.show()
    }


