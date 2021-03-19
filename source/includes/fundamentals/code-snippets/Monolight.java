package fundamentals.monolightcodec;

// begin Monolight class
public class Monolight {
    private String powerStatus;
    private Integer colorTemperature;

    public Monolight() {}

    // ...
    // end Monolight class

    public String getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(String powerStatus) {
        this.powerStatus = powerStatus;
    }

    public Integer getColorTemperature() {
        return colorTemperature;
    }

    public void setColorTemperature(Integer colorTemperature) {
        this.colorTemperature = colorTemperature;
    }


    @Override
    public String toString() {
        return "Monolight [powerStatus=" + powerStatus + ", colorTemperature=" + colorTemperature + "]";
    }

}
