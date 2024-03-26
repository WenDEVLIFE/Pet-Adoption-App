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

public class ReportAdapater extends RecyclerView.Adapter<ReportAdapater.PetViewHolder> {

    private List<ReportPet> reportlist;

    private onCancelListener onCancelListener;



    public ReportAdapater(List<ReportPet> reportlist) {
        this.reportlist = reportlist;
    }

    public void searchPets(List<ReportPet> reportlist) {
        reportlist = reportlist;
        notifyDataSetChanged();
    }


    public interface onCancelListener {
        void onCancel(int position);
    }



    public void setOnCancelListener(onCancelListener listener) {
        this.onCancelListener = listener;
    }



    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportlist1, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        ReportPet info = reportlist.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return reportlist.size();
    }

    // ViewHolder class
    public class PetViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView phone;
        private final TextView email;
        private final TextView dogowner;

        private final EditText description;

        private final Button Cancel;

        private final ImageView dogImage; // Add this line


        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            phone = itemView.findViewById(R.id.phonenum);
            email = itemView.findViewById(R.id.email);
            dogowner = itemView.findViewById(R.id.dogowner);
            description = itemView.findViewById(R.id.description);
            description.setEnabled(false);

            dogImage = itemView.findViewById(R.id.dogimage); // Add this line
            Cancel = itemView.findViewById(R.id.cancelbutton);




            // Set click listener for cancel button

            Cancel.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onCancelListener != null) {
                    onCancelListener.onCancel(position);
                }
            });


        }

        public void bind(ReportPet info) {
            // Bind data to views
            phone.setText(info.getPhone());
            email.setText(info.getEmail());
            dogowner.setText(info.getOwner());
            description.setText(info.getDescription());



            // Use Glide to load the image from the URL into the ImageView
            Glide.with(itemView.getContext())
                    .load(info.getImageUrl())
                    .into(dogImage);

        }

    }
}

