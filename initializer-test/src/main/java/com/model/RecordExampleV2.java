package com.model;

public class RecordExampleV2 extends BaseRecord {

    private String id;
    private Double valuation;
    private Boolean load;
    private String text;

    public RecordExampleV2() {
    }

    public RecordExampleV2(String id, Double valuation, String text) {
        this(id, valuation, text, new Boolean(true));
    }

    public RecordExampleV2(String id, Double valuation, String text, Boolean load) {
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

    public Boolean getLoad() {
        return load;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "RecordExampleV2{" +
                "id='" + id + '\'' +
                ", valuation=" + valuation +
                ", load=" + load +
                ", text='" + text + '\'' +
                '}';
    }
}
