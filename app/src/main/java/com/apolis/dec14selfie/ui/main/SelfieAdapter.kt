package com.apolis.dec14selfie.ui.main

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.apolis.dec14selfie.R
import com.apolis.dec14selfie.data.models.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_adapter_selfie.view.*

class SelfieAdapter(var mContext:Context,var mList:ArrayList<Image> = ArrayList()):
        RecyclerView.Adapter<SelfieAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(image:Image){
            Picasso.get()
                .load(image.uri)
                .error(R.drawable.ic_launcher_background)
                .into(itemView.display_selfie)


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfieAdapter.MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_selfie, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var image=mList[position]
        holder.bind(image)

    }
    fun setData(imageList:ArrayList<Image>?){

        if(imageList!=null){
            mList=imageList
            notifyDataSetChanged()
        }
    }


}