package com.huong.bpnhmnh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.Search;
import com.huong.bpnhmnh.SearchAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {


    private RecyclerView myrecyclerview;
    private Context mContext;
    List<Search> lstSearch;
    SearchAdapter searchAdapter;
    EditText edtSearch;

    public SearchFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = requireContext();
        initList(mView);
        edtSearch = mView.findViewById(R.id.edtSearch);
        setupSeach();
        return mView;
    }

    private void setupSeach() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private List<Search> listResult = new ArrayList<>();

    private void search(String value) {

        if (value.isEmpty()) {
            listResult = lstSearch;
        } else {
            List<Search> list = new ArrayList<>();
            for (Search search : lstSearch) {
                if (search.getName().toLowerCase().contains(value.toLowerCase())) {
                    list.add(search);
                }
            }
            listResult = list;
        }
        searchAdapter.filterList(listResult);
    }


    private void initList(View v) {
        myrecyclerview = (RecyclerView) v.findViewById(R.id.search_recyclerview);
        lstSearch = new ArrayList<>();
        lstSearch.add(new Search("BÁNH FLAN", R.drawable.banh_flan));
        lstSearch.add(new Search("BÁNH QUY", R.drawable.banh_quy));
        lstSearch.add(new Search("BÁNH KHOAI MỲ", R.drawable.banh_khoai_mi));
        lstSearch.add(new Search("BÁNH TIÊU", R.drawable.banh_tieu));
        lstSearch.add(new Search("BÁNH BAO NHÂN THỊT", R.drawable.banh_bao));
        lstSearch.add(new Search("VỊT NƯỚNG", R.drawable.vit_nuong));
        lstSearch.add(new Search("CÁ NƯỚNG GIẤY BẠC", R.drawable.ca_nuong));
        lstSearch.add(new Search("BÒ BÍT TẾT", R.drawable.bo_bit_tet));
        lstSearch.add(new Search("CHÁO GÀ", R.drawable.chao_ga));
        lstSearch.add(new Search("THỊT XIÊN RAU CỦ", R.drawable.thit_xien));
        searchAdapter = new SearchAdapter(getContext(), lstSearch);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(searchAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInatanceState) {
        super.onCreate(savedInatanceState);

    }


}