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

import ClassPackage.PetsPending;

public class PendingPetsAdapter extends RecyclerView.Adapter<PendingPetsAdapter.PetViewHolder> {

    private List<PetsPending> petList;
    private onAdoptListener onAdoptListener;

    private onCancelListener onCancelListener;



    public PendingPetsAdapter(List<PetsPending> petList) {
        this.petList = petList;
    }

    public void searchPets(List<PetsPending> newList) {
        petList = newList;
        notifyDataSetChanged();
    }

    public interface onAdoptListener {
        void onAdopt(int position);
    }

    public interface onCancelListener {
        void onCancel(int position);
    }



    public void setOnAdoptListener(onAdoptListener listener) {
        this.onAdoptListener = listener;
    }

    public void setOnCancelListener(onCancelListener listener) {
        this.onCancelListener = listener;
    }



    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pendinglist, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetsPending info = petList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    // ViewHolder class
    public class PetViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView dogname;
        private final TextView dogbreed;
        private final TextView dogowner;

        private final EditText description;

        private final TextView Adoptrequest;
        private final Button ADOPTButton;

        private final Button Cancel;

        private final ImageView dogImage; // Add this line


        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            dogname = itemView.findViewById(R.id.dogname);
            dogbreed = itemView.findViewById(R.id.dogbreed);
            dogowner = itemView.findViewById(R.id.dogowner);
            description = itemView.findViewById(R.id.description);
            description.setEnabled(false);
            Adoptrequest = itemView.findViewById(R.id.adoptowner);
            ADOPTButton = itemView.findViewById(R.id.acceptbutton);
            dogImage = itemView.findViewById(R.id.dogimage); // Add this line
            Cancel = itemView.findViewById(R.id.cancelbutton);


            // Set click listener for adoptbutton button

                ADOPTButton.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onAdoptListener != null) {
                        onAdoptListener.onAdopt(position);
                    }
                });


            // Set click listener for cancel button

                Cancel.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onCancelListener != null) {
                        onCancelListener.onCancel(position);
                    }
                });


        }

        public void bind(PetsPending info) {
            // Bind data to views
            dogname.setText(info.getDogname());
            dogbreed.setText(info.getBreed());
            dogowner.setText(info.getOwner());
            description.setText(info.getDescription());
            Adoptrequest.setText(info.getAdopt_requets());


            // Use Glide to load the image from the URL into the ImageView
            Glide.with(itemView.getContext())
                    .load(info.getImageUrl())
                    .into(dogImage);

        }

    }
}

