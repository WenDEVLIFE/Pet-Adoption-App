package ClassPackage;

public class DonationReceive {
    public String DonateItemName;

    public String DonateTo;

    public String DonateName;

    public String DonateDescription;

    private String imageUrl; // Add this line

    public DonationReceive(String donateItemName, String donateTo, String donateName, String donateDescription, String imageUrl) {
        DonateItemName = donateItemName;
        DonateTo = donateTo;
        DonateName = donateName;
        DonateDescription = donateDescription;
        this.imageUrl = imageUrl; // Add this line
    }

    public String getDonateItemName() {
        return DonateItemName;
    }

    public void setDonateItemName(String donateItemName) {
        DonateItemName = donateItemName;
    }

    public String getDonateTo() {
        return DonateTo;
    }

    public void setDonateTo(String donateTo) {
        DonateTo = donateTo;
    }

    public String getDonateName() {
        return DonateName;
    }

    public void setDonateName(String donateName) {
        DonateName = donateName;
    }

    public String getDonateDescription() {
        return DonateDescription;
    }

    public void setDonateDescription(String donateDescription) {
        DonateDescription = donateDescription;
    }

    public String getImageUrl() { // Add this method
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { // Add this method
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "DonationReceive{" +
                "DonateItemName='" + DonateItemName + '\'' +
                ", DonateTo='" + DonateTo + '\'' +
                ", DonateName='" + DonateName + '\'' +
                ", DonateDescription='" + DonateDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' + // Add this line
                '}';
    }



}

