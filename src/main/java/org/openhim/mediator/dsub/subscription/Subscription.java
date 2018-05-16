package org.openhim.mediator.dsub.subscription;

import java.util.Date;

public class Subscription {

    private String url;
    private String uuid;
    private Date terminateAt;
    private String facilityQuery;

    public Subscription(String url, Date terminateAt, String facilityQuery) {
        this.url = url;
        this.terminateAt = terminateAt;
        this.facilityQuery = facilityQuery;
    }

    public Subscription(String url, Date terminateAt, String facilityQuery,
                        String uuid) {
        this(url, terminateAt, facilityQuery);
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getTerminateAt() {
        return terminateAt;
    }

    public void setTerminateAt(Date terminateAt) {
        this.terminateAt = terminateAt;
    }

    public String getFacilityQuery() {
        return facilityQuery;
    }

    public void setFacilityQuery(String facilityQuery) {
        this.facilityQuery = facilityQuery;
    }
}
