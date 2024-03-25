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

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.PetViewHolder> {

    private List<Pets> petList;

    private onCancelListener onCancelListener;



    public LostAdapter(List<Pets> petList) {
        this.petList = petList;
    }

    public void searchPets(List<Pets> newList) {
        petList = newList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lostpetslist, parent, false);
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

        private final Button report;

        private final ImageView dogImage; // Add this line


        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            dogname = itemView.findViewById(R.id.dogname);
            dogbreed = itemView.findViewById(R.id.dogbreed);
            dogowner = itemView.findViewById(R.id.dogowner);
            description = itemView.findViewById(R.id.description);
            description.setEnabled(false);
            dogImage = itemView.findViewById(R.id.dogimage); // Add this line
            report = itemView.findViewById(R.id.reportbutton);




            // Set click listener for cancel button
            report.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onCancelListener != null) {
                    onCancelListener.onCancel(position);
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

