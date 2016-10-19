package controllers;

import java.util.List;

import de.htwg.stratego.StrategoApp;
import de.htwg.stratego.aview.tui.TextUI;
import de.htwg.stratego.controller.IStrategoController;
import de.htwg.stratego.model.ICell;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result strategoWui(String command) {
    	IStrategoController controller = StrategoApp.getInstance().getIStrategoController();
        return ok(strategoWui.render(controller));
    }
    
    public Result strategoTui(String command) {
    	TextUI tui = StrategoApp.getInstance().getTui();
    	tui.processInputLine(command);
    	return ok(tui.printTUI());
    }
    
    public Result strategoIndex() {
        return ok(strategoIndex.render());
    }

}
