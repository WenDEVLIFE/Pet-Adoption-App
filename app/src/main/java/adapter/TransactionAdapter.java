package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pet_adoption_app.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.Notification_ViewHolder> {

    private List<Transactions> translist;

    private onCancelListener onCancelListener;


    public TransactionAdapter(List<Transactions> translist) {
        this.translist = translist;
    }


    public interface onCancelListener {
        void onCancel(int position);
    }


    public void setOnCancelListener(onCancelListener listener) {
        this.onCancelListener = listener;
    }

    @NonNull
    @Override
    public Notification_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionlist, parent, false);
        return new Notification_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_ViewHolder holder, int position) {
        Transactions info = translist.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return translist.size();
    }

    // ViewHolder class
    public class Notification_ViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView transactions;


        public Notification_ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            transactions = itemView.findViewById(R.id.transaction_history);




        }

        public void bind(Transactions info) {
            // Bind data to views

            transactions.setText(info.getTransactiondetails());

        }
    }
}

