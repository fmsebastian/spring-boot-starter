package com.model;

public class RecordExample extends BaseRecord {

    private String id;
    private Double valuation;
    private Boolean load;

    public RecordExample() {
    }

    public RecordExample(String id, Double valuation) {
        this(id, valuation, new Boolean(true));
    }

    public RecordExample(String id, Double valuation, Boolean load) {
        this.id = id;
        this.valuation = valuation;
        this.load = load;
    }

    @Override
    public void parse() {
        System.out.println("record parse : " + this.toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValuation() {
        return valuation;
    }

    public void setValuation(Double valuation) {
        this.valuation = valuation;
    }

    public Boolean isLoad() {
        return load;
    }

    public void setLoad(Boolean load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "RecordExample{" +
                "id='" + id + '\'' +
                ", valuation=" + valuation +
                ", load=" + load +
                '}';
    }
}
