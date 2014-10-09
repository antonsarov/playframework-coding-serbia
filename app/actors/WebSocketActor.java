package actors;

import akka.actor.*;
import play.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Anton Sarov
 */
public class WebSocketActor extends UntypedActor {

    private static final Set<ActorRef> channels = new HashSet<>();

    public static Props props(ActorRef out) {
        return Props.create(WebSocketActor.class, out);
    }

    private ActorRef out;

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