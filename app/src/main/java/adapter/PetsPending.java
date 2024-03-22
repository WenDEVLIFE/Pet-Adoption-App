package adapter;


public class PetsPending {

    private String name;
    private String breed;
    private String Owner;
    private String Description;

    private String imageUrl; // Add this line

    private String adopt_requets;

    public PetsPending(String name, String breed, String owner, String description , String imageUrl, String adopt_requets) {
        this.name = name;
        this.breed = breed;
        Owner = owner;
        Description = description;
        this.imageUrl = imageUrl; // Add this line
        this.adopt_requets = adopt_requets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() { // Add this method
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { // Add this method
        this.imageUrl = imageUrl;
    }
    @Override
    public String toString() {
      return "Pets{" +
              "name='" + name + '\'' +
              ", breed='" + breed + '\'' +
              ", Owner='" + Owner + '\'' +
              ", Description='" + Description + '\'' +
              ", imageUrl='" + imageUrl + '\'' +
              ", adopt_requets='" + adopt_requets + '\'' +
              '}';
    }

    public String getDogname() {
        return name;
    }

    public String getAdopt_requets() {
        return adopt_requets;
    }

    public void setAdopt_requets(String adopt_requets) {
        this.adopt_requets = adopt_requets;
    }

    public String getAdoptRequest() {
        return adopt_requets;
    }
}
