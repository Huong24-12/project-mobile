package com.huong.bpnhmnh.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.huong.bpnhmnh.R;
import com.huong.bpnhmnh.Search;
import com.huong.bpnhmnh.SearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    
    private View mView;
    private RecyclerView myrecyclerview;
    private ArrayAdapter<Search> mAdapter;
    private Context mContext;
    List<Search> lstSearch;

    public SearchFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        myrecyclerview = (RecyclerView) mView.findViewById(R.id.search_recyclerview);
        SearchAdapter searchAdapter = new SearchAdapter(getContext(),lstSearch);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(searchAdapter);
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInatanceState){
        super.onCreate(savedInatanceState);
        setHasOptionsMenu(true);
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
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, lstSearch);
        setListAdapter((ListAdapter) lstSearch);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        String item = (String) l.getAdapter().getItem(position);
        if (getActivity() instanceof OnItem1SelectedListener ){
            ((OnItem1SelectedListener) getActivity()).OnItem1SelectedListener(item);
        }
        getFragmentManager().popBackStack();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        searchView.setQueryHint("Type here to search");

        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {

        return true;
    }


    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if(s == null || s.trim().isEmpty()){
            resetSearch();
            return false;
        }

        List<Search> filteredValues = new ArrayList<Search>(lstSearch);
        for(Search value: lstSearch){
            if (!value.getName().toLowerCase().contains(s.toLowerCase())){
                filteredValues.remove(value);
            }
        }

        mAdapter = new ArrayAdapter<Search>(mContext, android.R.layout.simple_list_item_1, filteredValues);
        setListAdapter(mAdapter);
        return false;
    }

    public void resetSearch(){
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, lstSearch);
        setListAdapter(mAdapter);
    }
}