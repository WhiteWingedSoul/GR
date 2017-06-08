package com.hedspi.hoangviet.eslrecom.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hedspi.hoangviet.eslrecom.DataDownloadListener;
import com.hedspi.hoangviet.eslrecom.R;
import com.hedspi.hoangviet.eslrecom.commons.Common;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.hedspi.hoangviet.eslrecom.models.Material;
import com.hedspi.hoangviet.eslrecom.models.UsageLog;
import com.squareup.picasso.Picasso;

/**
 * Created by hoangviet on 11/20/16.
 */

public class DetailBookFragment extends Fragment implements DataDownloadListener{
    private View view;
    private Material book;
    private ProgressDialog progress;


    private ImageView bookIcon;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView editionformat;
    private TextView genreform;
    private TextView docType;
    private TextView content;
    private View contentLayout;

    private TextView summary;

    private TextView subjects;
    private View subjectLayout;
    private View buyerLayout;
    private TextView buyerName;
    private TextView buyerPrice;
    private TextView buyerLink;


    public static DetailBookFragment newInstance(String key){
        DetailBookFragment fragment = new DetailBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", key);
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
    public void onPause() {
        if (progress!=null)
            progress.cancel();
        super.onPause();
    }

    @Override
    public void onDataDownloaded(String result) {

        if (progress!=null)
            progress.cancel();
        if (result == Common.SUCCESS) {
            String key = (String) getArguments().getSerializable("key");
            book = DatabaseManager.getSingleMaterialFromServer(key);
            if (book!=null) {
                Picasso.with(getActivity()).load(book.getCoverLink())
                        .fit()
                        .into(bookIcon);
                if (book.getName() == null || book.getName().equals("")) {
                    title.setText("");
                } else {
                    title.setText(book.getName());
                }
                if (book.getAuthor() == null || book.getAuthor().equals("")) {
                    author.setText("");
                } else {
                    author.setText(getResources().getString(R.string.by) + book.getAuthor());
                }
                if (book.getPublisher() == null || book.getPublisher().equals("")) {
                    publisher.setText("");
                } else {
                    publisher.setText(book.getPublisher());
                }
                if (book.getEdition_format() == null || book.getEdition_format().equals("")) {
                    editionformat.setText("");
                } else {
                    editionformat.setText(book.getEdition_format());
                }
                if (book.getGenre_form() == null || book.getGenre_form().equals("")) {
                    genreform.setText("");
                } else {
                    genreform.setText(getResources().getString(R.string.genreform) + book.getGenre_form());
                }
                if (book.getDocumentType() == null || book.getDocumentType().equals("")) {
                    docType.setText("");
                } else {
                    docType.setText(getResources().getString(R.string.docType) + book.getDocumentType());
                }
                if (book.getContent() == null || book.getContent().equals("")) {
                    contentLayout.setVisibility(View.GONE);
                } else {
                    contentLayout.setVisibility(View.VISIBLE);
                    content.setText(book.getContent());
                }
                if (book.getSummary() == null || book.getSummary().equals("")) {
                    summary.setText("");
                } else {
                    summary.setText(getResources().getString(R.string.summary) + book.getSummary());
                }

                if (book.getSubjects() == null || book.getSubjects().equals("")) {
                    subjectLayout.setVisibility(View.GONE);
                } else {
                    subjectLayout.setVisibility(View.VISIBLE);
                    subjects.setText(book.getSubjects());
                }

                if (book.getSellerName() == null || book.getSellerName().equals("")) {
                    buyerLayout.setVisibility(View.GONE);
                } else {
                    buyerLayout.setVisibility(View.VISIBLE);
                    buyerName.setText(book.getSellerName());
                    buyerPrice.setText(book.getSellerPrice());
                    buyerLink.setText(book.getSellerLink());

                }
            }
        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
        title = (TextView) view.findViewById(R.id.title);
        author = (TextView) view.findViewById(R.id.author);
        publisher = (TextView) view.findViewById(R.id.publisher);
        editionformat = (TextView) view.findViewById(R.id.editionformat);
        genreform = (TextView) view.findViewById(R.id.genreform);
        docType = (TextView) view.findViewById(R.id.docType);
        content = (TextView) view.findViewById(R.id.content);
        contentLayout = view.findViewById(R.id.contentLayout);

        summary = (TextView) view.findViewById(R.id.summary);

        subjects = (TextView) view.findViewById(R.id.subjects);
        subjectLayout = view.findViewById(R.id.subjectLayout);
        buyerLayout = view.findViewById(R.id.buyerLayout);
        buyerName = (TextView) view.findViewById(R.id.buyerName);
        buyerPrice = (TextView) view.findViewById(R.id.buyerPrice);
        buyerLink = (TextView) view.findViewById(R.id.buyerLink);


        try {
            progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading), false);
            progress.show();
        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseManager.getMaterialListFromServer(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



}
