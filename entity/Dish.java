import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Dish extends Item{

    private Set<Preparable> components = new HashSet<Preparable>();
    private int count = 0;

    public Dish(String name) {
        super(name);
        this.components = new HashSet<>();
    }

    public void addComponent(Preparable item){
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        components.add(item);
    }

    public Set<Preparable> getComponents(){
        return components;
    }
}