package actors;

import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.websocket.WebSocket;
import play.Logger;
import play.libs.Akka;

import java.util.HashSet;
import java.util.Set;

public class WebSocketActor extends UntypedActor {

    private static final Set<ActorRef> channels = new HashSet<>();

    public static Props props(ActorRef out) {
        return Props.create(WebSocketActor.class, out);
    }

    private ActorRef out;

    private ActorSystem actorSystem = Akka.system();

    public WebSocketActor() {}

    public WebSocketActor(ActorRef out) {
        this.out = out;
        this.channels.add(out);
    }

    @Override
    public void postStop() throws Exception {
        this.channels.remove(out);
        super.postStop();
    }

    public void onReceive(Object message) throws Exception {
        if (sender().equals(self())) {
            Logger.debug("sender equals self... ignoring");
            return;
        }
        Logger.debug("Message: " + message);
        if (message instanceof String) {

            channels.stream().forEach(channel->channel.tell(message, self()));
        }
    }
}