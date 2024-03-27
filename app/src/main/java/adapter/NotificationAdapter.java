package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pet_adoption_app.R;

import java.util.List;

import ClassPackage.NotificationsInfo;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Notification_ViewHolder> {

    private List<NotificationsInfo> notflist;

    private onCancelListener onCancelListener;


    public NotificationAdapter(List<NotificationsInfo> notflist) {
        this.notflist = notflist;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifaction_list, parent, false);
        return new Notification_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_ViewHolder holder, int position) {
        NotificationsInfo info = notflist.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return notflist.size();
    }

    // ViewHolder class
    public class Notification_ViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView notifications;


        public Notification_ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            notifications = itemView.findViewById(R.id.notification);




        }

        public void bind(NotificationsInfo info) {
            // Bind data to views
            notifications.setText(info.getNotifationsDetails());

        }
    }
}

