package com.example.clone_financeapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Accounts_adapter(private val accounts:List<Account>) :RecyclerView.Adapter<Accounts_adapter.AccountViewHolder>()
{
    inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewName:TextView = itemView.findViewById(R.id.account_name)
        val textViewtMoney:TextView = itemView.findViewById(R.id.account_money)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.account_item,parent,false)
        return AccountViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val currentAccount = accounts[position]

        holder.textViewName.text = currentAccount.name.toString()
        holder.textViewtMoney.text = currentAccount.Money_count.toString()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
