package myview.com.rlv_grid_3_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import myview.com.myview.R;


/**
 * Crate by E470PD on 2019/11/11
 */
public class Rlv32Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<String> list;

    public Rlv32Adapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(context).inflate(R.layout.item_32, viewGroup, false);
        holder = new MViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder MViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder {

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
