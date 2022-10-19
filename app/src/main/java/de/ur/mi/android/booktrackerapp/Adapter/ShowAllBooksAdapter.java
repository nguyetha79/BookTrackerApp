package de.ur.mi.android.booktrackerapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;

public class ShowAllBooksAdapter extends RecyclerView.Adapter<ShowAllBooksAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    ArrayList<BookItemModel> bookItemsList;

    public ShowAllBooksAdapter(Activity activity, Context context, ArrayList<BookItemModel> bookItemsList) {
        this.activity = activity;
        this.context = context;
        this.bookItemsList = bookItemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_item_model_show, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookItemModel currBookItemModel = bookItemsList.get(position);

        String title = currBookItemModel.getTitle();
        String author = currBookItemModel.getAuthor();
        String coverLink = currBookItemModel.getCover();

        holder.tvBookTitle.setText(title);
        holder.tvBookAuthors.setText(author);

        if (coverLink.equals("No cover")){
            String uri = "@drawable/no_book_cover_available";
            int imageResource = context.getResources()
                    .getIdentifier(uri, null, context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            holder.ivBookCover.setImageDrawable(res);
        } else {
            Picasso.get().load(coverLink).into(holder.ivBookCover);
        }

        holder.btnDetail.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowDetailBook3.class);

            intent.putExtra("id", currBookItemModel.getId());
            intent.putExtra("title", currBookItemModel.getTitle());
            intent.putExtra("author", currBookItemModel.getAuthor());
            intent.putExtra("cover", currBookItemModel.getCover());
            intent.putExtra("rating", currBookItemModel.getRating());
            intent.putExtra("numPages", currBookItemModel.getNumPages());
            intent.putExtra("language", currBookItemModel.getLanguage());
            intent.putExtra("status", currBookItemModel.getStatus());
            intent.putExtra("currPage", currBookItemModel.getLanguage());
            intent.putExtra("note", currBookItemModel.getNote());

            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return bookItemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBookTitle, tvBookAuthors;
        private ImageView ivBookCover;
        private Button btnDetail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookTitle = itemView.findViewById(R.id.tv_title_book_item_show);
            tvBookAuthors = itemView.findViewById(R.id.tv_author_book_item_show);
            ivBookCover = itemView.findViewById(R.id.iv_cover_book_item_show);

            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
