package it.polimi.ingsw.gc42.model.classes.cards;

/**
 * Implementation of a Corner containing a Resource item for Model
 */
public class ResourceCorner extends Corner{
    // Attributes
    private Resource resource;

    // Constructor Method
    /**
     * Constructor Method
     * @param resource: the Resource Type to assign to the Resource the Corner contains.
     *               All ResourceCorners are uncovered by default.
     */
    public ResourceCorner(Resource resource) {
        this.resource = resource;
        this.isCovered = false;
    }

    // Getter and Setter

    /**
     * Getter Method for resource.
     * @return the Resource Type to assign to the Resource the Corner contains.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Setter Method for resource.
     * @param resource: the Resource Type to assign to the Resource the Corner contains.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
