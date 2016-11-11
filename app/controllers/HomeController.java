package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import de.htwg.stratego.StrategoApp;
import de.htwg.stratego.aview.tui.TextUI;
import de.htwg.stratego.controller.IStrategoController;
import de.htwg.stratego.model.ICell;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private IStrategoController controller;

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

    public Result refresh() {
        return ok(controller.toJson());
    }

}
