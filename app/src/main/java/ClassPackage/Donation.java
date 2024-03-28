package ClassPackage;

public class Donation {

    public String  DogOwner, DonationName, Description;

    public Donation(String DogOwner, String DonationName, String Description) {
        this.DogOwner = DogOwner;
        this.DonationName = DonationName;
        this.Description = Description;

    }

    public String getDogOwner() {
        return DogOwner;
    }

    public void setDogOwner(String DogOwner) {
        this.DogOwner = DogOwner;
    }

    public String getDonationName() {
        return DonationName;
    }

    public void setDonationName(String DonationName) {
        this.DonationName = DonationName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }


    @Override
    public String toString() {
        return "Donation{" + "DogOwner=" + DogOwner + ", DonationName=" + DonationName + ", Description=" + Description + '}';
    }

}
