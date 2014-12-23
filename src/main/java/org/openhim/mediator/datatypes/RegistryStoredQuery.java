package org.openhim.mediator.datatypes;

/**
 * Registry stored query data. Note that only a subset of elements will be pulled, only what the HIM needs.
 *
 * @see org.openhim.mediator.normalization.ParseRegistryStoredQueryActor
 */
public class RegistryStoredQuery {
    private String id;
    private String patientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
