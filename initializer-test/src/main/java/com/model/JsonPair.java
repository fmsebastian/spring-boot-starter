package com.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Value
@Builder
@Wither
public class JsonPair {

    private final Map<String, Object> original;

    private final Map<String, Object> produced;

    private final Integer result;

    private final List<String> errors;

/*    public JsonPair(Map<String, Object> original, Map<String, Object> produced) {
        this.original = original;
        this.produced = produced;
        result = null;
        errors = new ArrayList<String>();
    }*/
}
