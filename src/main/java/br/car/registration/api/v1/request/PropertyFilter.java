package br.car.registration.api.v1.request;

import java.util.List;

import lombok.Data;

@Data
public class PropertyFilter {
    private String sub;
    private String propertyName;
    private String stateDistrict;
    private String municipality;
    private List<String> code;
    private String ownersName;
    private String ownersIdentifier;

    public boolean isEmpty() {
        return (propertyName == null || propertyName.isEmpty())
            && (stateDistrict == null || stateDistrict.isEmpty())
            && (municipality == null || municipality.isEmpty())
            && (code == null || code.isEmpty())
            && (ownersName == null || ownersName.isEmpty())
            && (ownersIdentifier == null || ownersIdentifier.isEmpty());
    }
   

}
