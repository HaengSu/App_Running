package org.techtown.app_running.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.app_running.databinding.RecyclerviewWeatherItemBinding
import org.techtown.app_running.model.ModelWeather
import org.techtown.app_running.view.ViewHolderWeather

class AdapterWeather(var items : Array<ModelWeather>) : RecyclerView.Adapter<ViewHolderWeather>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWeather {
        val binding = RecyclerviewWeatherItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolderWeather(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderWeather, position: Int) {
        val item = items[position]
        holder.setItems(item)
    }

    override fun getItemCount() = items.count()
}