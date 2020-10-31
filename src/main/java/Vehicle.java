public class Vehicle implements Comparable {
    private String make;
    private String model;
    private Integer year;
    private String color;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int compareTo(Object o) {
        Vehicle other = (Vehicle)o;
        if (make.compareTo(other.getMake()) == 0) {
            return model.compareTo(other.getModel());
        }
        return make.compareTo(other.getMake());
    }
}
