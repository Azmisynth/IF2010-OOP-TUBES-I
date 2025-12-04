package main.java.model.item;

public class Ingredient extends Item implements Preparable {
    private ItemState state = ItemState.RAW;

    public Ingredient(String name, ItemState intitialState){
        super(name);
        this.state = intitialState;;
    }

    public ItemState getState(){
        return state;
    }

    public void setState(ItemState newState){
        this.state = newState;
    }

    @Override
    public boolean canBeChopped(){
        return state == ItemState.RAW;
    }

    public boolean canBeCooked(){
        return state == ItemState.RAW || state == ItemState.CHOPPED;
    }

    public boolean canBePlacedOnPlate(){
        return state == ItemState.CHOPPED || state == ItemState.COOKED;
    }

    public void chop(){
        if(canBeChopped()){
            state = ItemState.CHOPPED;
        } else {
            throw new IllegalStateException(getName() + " cannot be chopped.");
        }
    }

    public void cook(){
        if(canBeCooked()){
            state = ItemState.COOKED;
        } else {
            throw new IllegalStateException(getName() + " cannot be cooked.");
        }
    }
}