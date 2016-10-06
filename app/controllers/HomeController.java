package controllers;

import play.mvc.*;

import views.html.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.htwg.stratego.StrategoApp;
import de.htwg.stratego.aview.tui.TextUI;
import de.htwg.stratego.controller.IStrategoController;

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
    public Result index() {
        return ok(index.render("Your new application is ready33."));
    }
    
    public Result stratego(String command) {
    	TextUI tui = StrategoApp.getInstance().getTui();
    	tui.processInputLine(command);
    	return ok(tui.printTUI());
    }

}
