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
import com.example.pet_adoption_app.DonationReceived;
import com.example.pet_adoption_app.R;

import java.util.List;

import ClassPackage.DonationReceive;
import ClassPackage.Pets;

public class DonationReceivedAdapter extends RecyclerView.Adapter<DonationReceivedAdapter.PetViewHolder> {

    private List<DonationReceive> donationReceiveList;
    private onAdoptListener onAdoptListener;



    public DonationReceivedAdapter(List<DonationReceive> donationReceiveList) {
        this.donationReceiveList = donationReceiveList;
    }

    public void searchDonation(List<DonationReceive> newList) {
        donationReceiveList = newList;
        notifyDataSetChanged();
    }

    public interface onAdoptListener {
        void onAdopt(int position);
    }



    public void setOnAdoptListener(onAdoptListener listener) {
        this.onAdoptListener = listener;
    }



    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_receive_list, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        DonationReceive info = donationReceiveList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return donationReceiveList.size();
    }

    // ViewHolder class
    public class PetViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView DonateName;
        private final TextView DonateTo;
        private final TextView DonatedBy;

        private final EditText description;
        private final Button ReceiveButton;

        private final ImageView donateimage; // Add this line


        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            DonateName = itemView.findViewById(R.id.donatename);
            DonateTo = itemView.findViewById(R.id.donateowner);
            DonatedBy = itemView.findViewById(R.id.donateby);
            description = itemView.findViewById(R.id.petdescription);
            description.setEnabled(false);
            ReceiveButton = itemView.findViewById(R.id.deletebutton);
            donateimage = itemView.findViewById(R.id.imageView6); // Add this line


            // Set click listener for delete button
            ReceiveButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onAdoptListener != null) {
                    onAdoptListener.onAdopt(position);
                }
            });

        }

        public void bind(DonationReceive info) {
            // Bind data to views
            DonateName.setText(info.getDonateItemName());
            DonateTo.setText(info.getDonateTo());
            DonatedBy.setText(info.getDonateName());
            description.setText(info.getDonateDescription());


            // Use Glide to load the image from the URL into the ImageView
            Glide.with(itemView.getContext())
                    .load(info.getImageUrl())
                    .into(donateimage);

        }

    }
}

