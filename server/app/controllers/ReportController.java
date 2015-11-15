package controllers;

import model.Report;
import model.external.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import services.ReportService;

@Controller
public class ReportController {

    @Autowired
    private ReportService service;

    public Result getPetsReport() {
        Http.Response response = play.mvc.Controller.response();
        response.setHeader(play.mvc.Controller.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        Form<ReportRequest> form = Form.form(ReportRequest.class).bindFromRequest();
        ReportRequest request = form.get();
        Report report = service.getPetsReport(request);
        return play.mvc.Controller.ok(Json.toJson(report));
    }

}
