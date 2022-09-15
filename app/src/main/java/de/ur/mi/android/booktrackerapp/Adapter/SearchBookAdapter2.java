package de.ur.mi.android.booktrackerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.ur.mi.android.booktrackerapp.Activity.AddBookActivity;
import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;

public class SearchBookAdapter2 extends RecyclerView.Adapter<SearchBookAdapter2.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private ArrayList<BookItemModel> bookItemsList;

    public SearchBookAdapter2(Context context, ArrayList<BookItemModel> bookItemsList,
                              RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.bookItemsList = bookItemsList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_item_model, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookItemModel currBookItemModel = bookItemsList.get(position);

        String title = currBookItemModel.getTitle();
        String author = currBookItemModel.getAuthor();
        String coverLink = currBookItemModel.getCover();

        holder.tvBookTitle.setText(title);
        holder.tvBookAuthors.setText(author);

        if (coverLink =="No cover"){
            String uri = "@drawable/no_book_cover_available";
            int imageResource = context.getResources()
                    .getIdentifier(uri, null, context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            holder.ivBookCover.setImageDrawable(res);
        } else {
            Picasso.get().load(coverLink).into(holder.ivBookCover);
        }

//        holder.mainLayoutBookItem.setOnClickListener(view -> {
//
//              Intent intent = new Intent(context, AddBookActivity.class);
//              intent.putExtra("title", currBookItemModel.getTitle());
//              intent.putExtra("author", currBookItemModel.getAuthor());
//              intent.putExtra("cover", currBookItemModel.getCover());
//              intent.putExtra("numPages", currBookItemModel.getNumPages());
//              intent.putExtra("rating", currBookItemModel.getRating());
//              intent.putExtra("language", currBookItemModel.getLanguage());
//
//              context.startActivity(intent);
//        });

    }

    @Override
    public int getItemCount() {
        return bookItemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookTitle, tvBookAuthors;
        private ImageView ivBookCover;
        private LinearLayout mainLayoutBookItem;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_title_book_item);
            tvBookAuthors = itemView.findViewById(R.id.tv_author_book_item);
            ivBookCover = itemView.findViewById(R.id.iv_cover_book_item);

            mainLayoutBookItem = itemView.findViewById(R.id.main_layout_book_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
