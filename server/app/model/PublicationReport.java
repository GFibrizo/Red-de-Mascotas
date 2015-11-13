package model;

public class PublicationReport {

    public String informer;

    public String reason;

    public String status;

    public String reportDate;

    public PublicationReport() { }

    public PublicationReport(String informer, String reason, String status, String reportDate) {
        this.informer = informer;
        this.reason = reason;
        this.status = status;
        this.reportDate = reportDate;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

}
