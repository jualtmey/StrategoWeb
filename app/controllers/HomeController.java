package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import de.htwg.stratego.StrategoApp;
import de.htwg.stratego.aview.tui.TextUI;
import de.htwg.stratego.controller.ISingelDeviceStrategoController;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.strategoIndex;
import views.html.strategoWui;

import javax.inject.Singleton;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {

    private ISingelDeviceStrategoController controller;

    public Result strategoTui(String command) {
    	TextUI tui = StrategoApp.getInstance().getTui();
    	tui.processInputLine(command);
    	return ok(tui.printTUI());
    }

    public Result strategoIndex() {
        return ok(strategoIndex.render());
    }

    public Result strategoWui() {
        controller = StrategoApp.getInstance().getIStrategoController();
        return ok(strategoWui.render(controller));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result add() {
        JsonNode json = request().body().asJson();
        controller.add(json.findPath("column").intValue(), json.findPath("row").intValue(), json.findPath("rank").intValue());
        return refresh();
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result swap() {
        JsonNode json = request().body().asJson();
        controller.swap(json.findPath("column1").intValue(), json.findPath("row1").intValue(),
                        json.findPath("column2").intValue(), json.findPath("row2").intValue());
        return refresh();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result remove() {
        JsonNode json = request().body().asJson();
        controller.remove(json.findPath("column").intValue(), json.findPath("row").intValue());
        return refresh();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result move() {
        JsonNode json = request().body().asJson();
        controller.move(json.findPath("columnFrom").intValue(), json.findPath("rowFrom").intValue(),
                json.findPath("columnTo").intValue(), json.findPath("rowTo").intValue());
        return refresh();
    }

    public Result finish() {
        controller.finish();
        return refresh();
    }

    public Result reset() {
        controller.reset();
        return refresh();
    }

    public Result refresh() {
        return ok(controller.toJson());
    }
    
}
