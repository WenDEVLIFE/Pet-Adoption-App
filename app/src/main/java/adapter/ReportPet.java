package adapter;

import androidx.annotation.NonNull;

public class ReportPet {

    public String phone;

    public String email;

    public String owner;

    private String Description;

    private String imageUrl; // Add this line

    public ReportPet(String phone, String email, String owner, String description, String imageUrl) {
        this.phone = phone;
        this.email = email;
        this.owner = owner;
        Description = description;
        this.imageUrl = imageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReportPet{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", owner='" + owner + '\'' +
                ", Description='" + Description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
