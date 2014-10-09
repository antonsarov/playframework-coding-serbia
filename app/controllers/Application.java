package controllers;

import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.*;
import play.libs.F;
import play.mvc.*;

import views.html.*;

/**
 * @author Anton Sarov
 */
public class Application extends Controller {

    public static WebSocket<String> ws() {
        return WebSocket.withActor(actorRef -> WebSocketActor.props(actorRef));
    }

    public static Result index() {
        return ok(ws.render());
    }
}
