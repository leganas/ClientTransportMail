package com.leganas.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leganas.mobile.clienttransport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndreyLS on 24.01.2017.
 */

public class NewsFragment extends Fragment {
    TextView mTextView;

    List<Item> itemsFromList;

    RecyclerView recycleView;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment,container,false);

        mTextView = (TextView) view.findViewById(R.id.textView);
        mTextView.setText("Новости компании");

        recycleView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void updateUI(){
        // Создаём адаптер для нашего recycleView
        if (listAdapter == null){
            listAdapter = new ListAdapter();
            listAdapter.setItems(itemsFromList);
            recycleView.setAdapter(listAdapter);
        } else {
            // если мы сюда попали после поворота то адаптор уже создан
            // сделаем привязку к значениям и обновим
            listAdapter.setItems(itemsFromList);
            listAdapter.notifyDataSetChanged();
        }
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        Item item;
        private TextView mText;

        public ListHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.textView2);
        }

        public void setItem(Item item){
            this.item = item;
            mText.setText(item.t);
        }
    }

    public class ListAdapter extends RecyclerView.Adapter<ListHolder>{
        List<Item> items;

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            Item item = items.get(position);
            holder.setItem(item);
        }

        public void setItems(List<Item> items){this.items = items;}

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsFromList = new ArrayList<>();
        for (int i=0;i<100;i++){
            itemsFromList.add(new Item(i));
        }
    }
}
