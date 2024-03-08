package it.polimi.ingsw.gc42.classes.cards;

public class ResourceCorner extends Corner{
    // Attributes
    private Resource resource;

    // Constructor Method
    public ResourceCorner(Resource resource) {
        this.resource = resource;
        this.isCovered = false;
    }

    // Getter and Setter
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
