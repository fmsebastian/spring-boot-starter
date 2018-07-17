package com.poc.initializer.web.data;

import lombok.*;

@Builder
@Data
public class LombokDataAnnotation {

    @NonNull
    private final String name;
    @NonNull
    private final String id;

    private Integer creditCard;

/*    // LombokDataAnnotationBuilder()).setCreditCard().build()
    private LombokDataAnnotation(String name, String id){
        this.name = name;
        this.id = id;
    }

    public static LombokDataAnnotation build(String name, String id){
        return new LombokDataAnnotation(name, id);
    }*/

/*    // Builder pattern constructor
    public static LombokDataAnnotation.build(String name, String id). {
        return new LombokDataAnnotation(name, id);
    }
    // Allows constructs like this: LombokDataAnnotation.build(name, id).setCreditCard(creditCard);
    public LombokDataAnnotation setCreditCard(Integer creditCard) {
        this.creditCard = creditCard;
        return this;
    }
    public LombokDataAnnotation(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getCreditCard() {
        return creditCard;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LombokDataAnnotation that = (LombokDataAnnotation) o;

        if (!name.equals(that.name)) return false;
        if (!id.equals(that.id)) return false;
        return creditCard != null ? creditCard.equals(that.creditCard) : that.creditCard == null;
    }
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (creditCard != null ? creditCard.hashCode() : 0);
        return result;
    }*/
}
