package controllers;

import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.*;
import play.libs.F;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static WebSocket<String> ws() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            @Override
            public Props apply(ActorRef actorRef) throws Throwable {
                Logger.debug(actorRef.path().toString());
                return WebSocketActor.props(actorRef);
            }
        });
    }

    public static Result wsClient() {
        return ok(ws.render());
    }
}
