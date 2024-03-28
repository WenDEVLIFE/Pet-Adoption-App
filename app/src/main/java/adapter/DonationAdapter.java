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

import ClassPackage.Donation;
import ClassPackage.PetsPending;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationView> {

    private List<Donation> donationList;

    private onCancelListener onCancelListener;



    public DonationAdapter(List<Donation> donationList) {
        this.donationList = donationList;
    }

    public void searchPets(List<Donation> newList) {
        donationList = newList;
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
    public DonationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donationlist, parent, false);
        return new DonationView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationView holder, int position) {
        Donation info = donationList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    // ViewHolder class
    public class DonationView extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView DonateName;
        private final TextView DoneOwner;

        private final EditText Donatedescription;



        private final Button Cancel;



        public DonationView(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            DonateName = itemView.findViewById(R.id.donatename);
            DoneOwner = itemView.findViewById(R.id.donateowner);
            Donatedescription = itemView.findViewById(R.id.petdescription);
            Cancel = itemView.findViewById(R.id.donate);




            // Set click listener for cancel button
            Cancel.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onCancelListener != null) {
                    onCancelListener.onCancel(position);
                }
            });


        }

        public void bind(Donation info) {
            // Bind data to views
            DonateName.setText(info.getDonationName());
            DoneOwner.setText(info.getDogOwner());
            Donatedescription.setText(info.getDescription());



        }

    }
}

