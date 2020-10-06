import jdk.nashorn.internal.objects.annotations.Getter;

import java.util.Date;
import java.util.Objects;


public class Customer {
    private String firstName,lastName;
    private Car Car;
    public Customer(String firstName, String lastName, Car car) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.Car = car;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Car getCar() {
        return Car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(Car, customer.Car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, Car);
    }

    public void setCar(Car car) {
        this.Car = car;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Customer() {}


}
