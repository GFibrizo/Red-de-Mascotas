package controllers;

import model.PublicationDenunciation;
import model.UserDenunciation;
import model.external.AcceptPublicationReportRequest;
import model.external.ReportPublicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import services.DenunciationService;

import java.util.List;

@Controller
public class DenunciationController {

    @Autowired
    private DenunciationService service;

    public Result getPetPublicationDenunciations() {
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        List<PublicationDenunciation> denunciations = service.getPetPublicationDenunciations();
        Logger.info("Number of pet publication denunciations: " + denunciations.size());
        return play.mvc.Controller.ok(Json.toJson(denunciations));
    }

    public Result reportPublication(String petId) {
        Form<ReportPublicationRequest> form = Form.form(ReportPublicationRequest.class).bindFromRequest();
        ReportPublicationRequest request = form.get();
        service.reportPublication(request);
        return play.mvc.Controller.ok();
    }

    public Result acceptPublicationReport(String petId) {
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        Form<AcceptPublicationReportRequest> form = Form.form(AcceptPublicationReportRequest.class).bindFromRequest();
        AcceptPublicationReportRequest request = form.get();
        service.acceptPublicationReport(request);
        return play.mvc.Controller.ok();
    }

    public Result rejectPublicationReport(String petId) {
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        Form<AcceptPublicationReportRequest> form = Form.form(AcceptPublicationReportRequest.class).bindFromRequest();
        AcceptPublicationReportRequest request = form.get();
        service.rejectPublicationReport(request);
        return play.mvc.Controller.ok();
    }

    public Result getUserDenunciations() {
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        List<UserDenunciation> denunciations = service.getUserDenunciations();
        Logger.info("Number of user denunciations: " + denunciations.size());
        return play.mvc.Controller.ok(Json.toJson(denunciations));
    }

}
