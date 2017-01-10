package controllers.authentication;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.http.DefaultHttpActionAdapter;
import play.mvc.Result;
import play.mvc.Results;

public class HttpActionAdapter extends DefaultHttpActionAdapter {

    @Override
    public Result adapt(int code, PlayWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            return Results.unauthorized(views.html.error401.render().toString()).as((HttpConstants.HTML_CONTENT_TYPE));
        } else if (code == HttpConstants.FORBIDDEN) {
            return Results.forbidden(views.html.error403.render().toString()).as((HttpConstants.HTML_CONTENT_TYPE));
        } else {
            return super.adapt(code, context);
        }
    }
}
