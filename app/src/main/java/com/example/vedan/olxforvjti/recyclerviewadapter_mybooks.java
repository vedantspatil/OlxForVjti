package com.example.vedan.olxforvjti;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vedan on 24-03-2018.
 */

public class recyclerviewadapter_mybooks extends RecyclerView.Adapter<recyclerviewadapter_mybooks.MyViewHolder>{


    private Context mContext;
    private List<book_details> mData;
    private static String link = "https://firebasestorage.googleapis.com/v0/b/olxforvjti.appspot.com/o/Photos%2Fbookimagenotavailable.png?alt=media&token=58ed9e71-3018-4e92-9514-d14ef948b038";

    public recyclerviewadapter_mybooks(Context mContext, List<book_details> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public recyclerviewadapter_mybooks.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.cardview_mybooks ,parent,false);

        return new recyclerviewadapter_mybooks.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(recyclerviewadapter_mybooks.MyViewHolder holder, final int position) {

        holder.bookname.setText(mData.get(position).getBook_Name());
        holder.booksem.setText(mData.get(position).getBook_Semester());
        holder.bookprice.setText(mData.get(position).getBook_Price());

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
            cardView = itemView.findViewById(R.id.cardview_mybooks);
            myView = itemView;

        }
        public void setBookname(String name){

            bookname = itemView.findViewById(R.id.book_name);
            bookname.setText(name);

        }
        public void setBooksem(String sem){

            booksem = itemView.findViewById(R.id.book_sem);
            booksem.setText("Sem: "+sem);
        }
        public void setBookprice(String price){

            bookprice = itemView.findViewById(R.id.book_price);
            bookprice.setText("Price: "+price);

        }
        public void setBookimage(Context ctx,String imagelink){

            bookimage = itemView.findViewById(R.id.book_image);
            if(!imagelink.equals("null")) Picasso.get().load(imagelink).fit().centerCrop().into(bookimage);
        }
    }


}
