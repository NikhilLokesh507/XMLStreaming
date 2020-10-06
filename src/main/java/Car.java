public class Car {
    private int number;
    private String model;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Car(int number, String model) {
        this.number = number;
        this.model = model;
    }
}
