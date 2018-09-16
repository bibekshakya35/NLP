package cspersistence.document;

import com.fasterxml.jackson.annotation.*;
import preprocess.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecWithCategory {

    private List<String> variants = null;
    private ChasisBrakeSuspensions chasisBrakeSuspensions;
    private Performance performance;
    private Transmission transmission;
    private EngineSpecifications engineSpecifications;
    private BodyExteriorDesignDimensions bodyExteriorDesignDimensions;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<String> getVariants() {
        return variants;
    }

    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    public ChasisBrakeSuspensions getChasisBrakeSuspensions() {
        return chasisBrakeSuspensions;
    }

    public void setChasisBrakeSuspensions(ChasisBrakeSuspensions chasisBrakeSuspensions) {
        this.chasisBrakeSuspensions = chasisBrakeSuspensions;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public EngineSpecifications getEngineSpecifications() {
        return engineSpecifications;
    }

    public void setEngineSpecifications(EngineSpecifications engineSpecifications) {
        this.engineSpecifications = engineSpecifications;
    }

    public BodyExteriorDesignDimensions getBodyExteriorDesignDimensions() {
        return bodyExteriorDesignDimensions;
    }

    public void setBodyExteriorDesignDimensions(BodyExteriorDesignDimensions bodyExteriorDesignDimensions) {
        this.bodyExteriorDesignDimensions = bodyExteriorDesignDimensions;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}