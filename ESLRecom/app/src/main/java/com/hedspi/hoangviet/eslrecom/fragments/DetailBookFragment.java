package com.hedspi.hoangviet.eslrecom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.squareup.picasso.Picasso;

/**
 * Created by hoangviet on 11/20/16.
 */

public class DetailBookFragment extends Fragment {
    private View view;
    private Material book;

    public static DetailBookFragment newInstance(Material book){
        DetailBookFragment fragment = new DetailBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_book_detail, viewGroup, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        book = (Material) getArguments().getSerializable("book");
        ImageView bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
        Picasso.with(getActivity()).load(book.getCoverLink())
                .fit()
                .into(bookIcon);
        TextView title = (TextView) view.findViewById(R.id.title);
        if (book.getName() == null || book.getName().equals("")){
            title.setText("");
        }else{
            title.setText(book.getName());
        }
        TextView author = (TextView) view.findViewById(R.id.author);
        if (book.getAuthor() == null || book.getAuthor().equals("")){
            author.setText("");
        }else{
            author.setText(getResources().getString(R.string.by)+ book.getAuthor());
        }
        TextView publisher = (TextView) view.findViewById(R.id.publisher);
        if (book.getPublisher() == null || book.getPublisher().equals("")){
            publisher.setText("");
        }else{
            publisher.setText(book.getPublisher());
        }
        TextView editionformat= (TextView) view.findViewById(R.id.editionformat);
        if (book.getEdition_format() == null || book.getEdition_format().equals("")){
            editionformat.setText("");
        }else{
            editionformat.setText(book.getEdition_format());
        }
        TextView genreform = (TextView) view.findViewById(R.id.genreform);
        if (book.getGenre_form() == null || book.getGenre_form().equals("")){
            genreform.setText("");
        }else{
            genreform.setText(getResources().getString(R.string.genreform)+ book.getGenre_form());
        }
        TextView docType= (TextView) view.findViewById(R.id.docType);
        if (book.getDocumentType() == null || book.getDocumentType().equals("")){
            docType.setText("");
        }else{
            docType.setText(getResources().getString(R.string.docType)+ book.getDocumentType());
        }
        TextView content = (TextView) view.findViewById(R.id.content);
        View contentLayout = view.findViewById(R.id.contentLayout);
        if (book.getContent() == null || book.getContent().equals("")){
            contentLayout.setVisibility(View.GONE);
        }else{
            contentLayout.setVisibility(View.VISIBLE);
            content.setText(book.getContent());
        }
        TextView summary = (TextView) view.findViewById(R.id.summary);
        if (book.getSummary() == null || book.getSummary().equals("")){
            summary.setText("");
        }else{
            summary.setText(getResources().getString(R.string.summary)+ book.getSummary());
        }
        TextView subjects = (TextView) view.findViewById(R.id.subjects);
        View subjectLayout = view.findViewById(R.id.subjectLayout);

        if (book.getSubjects() == null || book.getSubjects().equals("")){
            subjectLayout.setVisibility(View.GONE);
        }else{
            subjectLayout.setVisibility(View.VISIBLE);
            subjects.setText(book.getSubjects());
        }

        View buyerLayout = view.findViewById(R.id.buyerLayout);
        TextView buyerName = (TextView) view.findViewById(R.id.buyerName);
        TextView buyerPrice = (TextView) view.findViewById(R.id.buyerPrice);
        TextView buyerLink = (TextView) view.findViewById(R.id.buyerLink);
        if (book.getSellerName() == null || book.getSellerName().equals("")){
            buyerLayout.setVisibility(View.GONE);
        }else{
            buyerLayout.setVisibility(View.VISIBLE);
            buyerName.setText(book.getSellerName());
            buyerPrice.setText(book.getSellerPrice());
            buyerLink.setText(book.getSellerLink());

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



}
