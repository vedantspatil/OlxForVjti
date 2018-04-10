package com.example.vedan.olxforvjti;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vedan on 21-03-2018.
 */

public class recyclerviewadapter_homescreen extends RecyclerView.Adapter<recyclerviewadapter_homescreen.MyViewHolder>{

    private Context mContext;
    private List<Allbooks> mData;
    private static String link = "https://firebasestorage.googleapis.com/v0/b/olxforvjti.appspot.com/o/Photos%2Fbookimagenotavailable.png?alt=media&token=58ed9e71-3018-4e92-9514-d14ef948b038";


    public recyclerviewadapter_homescreen(Context mContext, List<Allbooks> mData) {
        this.mContext = mContext;
        this.mData = mData;
        Log.d("Size of mData",this.mData.size()+"");
        for (int i = 0; i < mData.size(); i++) {
            Log.d("mdata" + i, mData.get(i).getBook_Name());
        }
    }

    @Override
    public recyclerviewadapter_homescreen.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.cardview_books ,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(recyclerviewadapter_homescreen.MyViewHolder holder, final int position) {

        holder.setBookname(mData.get(position).getBook_Name().toString());
        holder.setBooksem(mData.get(position).getBook_Semester().toString());
        holder.setBookprice(mData.get(position).getBook_Price().toString());
        holder.setBookimage(mContext,mData.get(position).getBook_Image().toString());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AboutBookActivity.class);
                intent.putExtra("Name", mData.get(position).getBook_Name());
                intent.putExtra("Desc", mData.get(position).getBook_Description());
                intent.putExtra("Author", mData.get(position).getBook_Author());
                intent.putExtra("Subject", mData.get(position).getBook_Subject());
                intent.putExtra("Edition", mData.get(position).getBook_Edition());
                intent.putExtra("Price", mData.get(position).getBook_Price());
                intent.putExtra("Condition", mData.get(position).getBook_Condition());
                intent.putExtra("Sem", mData.get(position).getBook_Semester());
                intent.putExtra("Imagelink", mData.get(position).getBook_Image());
                intent.putExtra("User UID", mData.get(position).getUSER_UID());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView bookname , booksem, bookprice;
        ImageView bookimage;
        View myView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview_books);
            myView = itemView;

        }
        public void setBookname(String name){

            bookname =myView.findViewById(R.id.book_name);
            bookname.setText(name);

        }
        public void setBooksem(String sem){

            booksem = myView.findViewById(R.id.book_sem);
            booksem.setText("Sem: "+sem);
        }
        public void setBookprice(String price){

            bookprice = myView.findViewById(R.id.book_price);
            bookprice.setText("Price: "+price);

        }
        public void setBookimage(Context ctx,String imagelink){

            bookimage = myView.findViewById(R.id.book_image);
            if(!imagelink.equals("null"))Picasso.get().load(imagelink).fit().centerCrop().into(bookimage);
        }
    }
}
