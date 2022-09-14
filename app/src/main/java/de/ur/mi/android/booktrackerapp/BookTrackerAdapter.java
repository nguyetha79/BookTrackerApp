package de.ur.mi.android.booktrackerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookTrackerAdapter extends RecyclerView.Adapter<BookTrackerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BookItemModel> bookItemsList;

    public BookTrackerAdapter(Context context, ArrayList<BookItemModel> bookItemsList) {
        this.context = context;
        this.bookItemsList = bookItemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_item_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookItemModel currBookItemModel = bookItemsList.get(position);

        String title = currBookItemModel.getTitle();
        String authors = currBookItemModel.getAuthors();
        String cover = currBookItemModel.getCover();

        holder.tvBookTitle.setText(title);
        holder.tvBookAuthors.setText( authors);

        Picasso.get().load(currBookItemModel.getCover()).into(holder.ivBookCover);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(context, ShowDetailBook.class);
              intent.putExtra("title", currBookItemModel.getTitle());
              intent.putExtra("authors", currBookItemModel.getTitle());
              intent.putExtra("cover", currBookItemModel.getTitle());
              intent.putExtra("numPages", currBookItemModel.getTitle());
              intent.putExtra("rating", currBookItemModel.getTitle());
              intent.putExtra("language", currBookItemModel.getTitle());

              context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookItemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookTitle, tvBookAuthors;
        private ImageView ivBookCover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_title_book_item);
            tvBookAuthors = itemView.findViewById(R.id.tv_author_book_item);
            ivBookCover = itemView.findViewById(R.id.iv_cover_book_item);
        }
    }
}
