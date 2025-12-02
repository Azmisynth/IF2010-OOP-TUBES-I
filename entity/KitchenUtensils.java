import java.util.HashSet;
import java.util.Set;

public abstract class KitchenUtensils extends Item {

    protected Set<Preparable> contents;

    public KitchenUtensils(String name) {
        super(name);
        this.contents = new HashSet<>();
    }

    public Set<Preparable> getContents() {
        return contents;
    }
}
