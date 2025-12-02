public abstract class Item {
    private String name;
    private boolean portable = true;

    public Item(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public boolean isPortable(){
        return portable;
    }

    public void setPortable(boolean portable){
        this.portable = portable;
    }
}
