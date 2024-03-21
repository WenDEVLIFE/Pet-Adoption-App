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

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<Pets> petList;
    private onAdoptListener onAdoptListener;



    public PetAdapter(List<Pets> petList) {
        this.petList = petList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petlist, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pets info = petList.get(position);
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
        private final Button ADOPTButton;

        private final ImageView dogImage; // Add this line


        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            dogname = itemView.findViewById(R.id.dogname);
            dogbreed = itemView.findViewById(R.id.dogbreed);
            dogowner = itemView.findViewById(R.id.dogowner);
            description = itemView.findViewById(R.id.description);
            description.setEnabled(false);
            ADOPTButton = itemView.findViewById(R.id.button2);
            dogImage = itemView.findViewById(R.id.dogimage); // Add this line


            // Set click listener for delete button
            ADOPTButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onAdoptListener != null) {
                    onAdoptListener.onAdopt(position);
                }
            });

        }

        public void bind(Pets info) {
            // Bind data to views
            dogname.setText(info.getDogname());
            dogbreed.setText(info.getBreed());
            dogowner.setText(info.getOwner());
            description.setText(info.getDescription());


            // Use Glide to load the image from the URL into the ImageView
            Glide.with(itemView.getContext())
                    .load(info.getImageUrl())
                    .into(dogImage);

        }

    }
}

