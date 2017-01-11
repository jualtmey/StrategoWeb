package actors;

import akka.actor.ActorRef;
import org.pac4j.core.profile.CommonProfile;

public class User {

    private CommonProfile profile;
    private ActorRef userActor;
    // TODO: states

    public User(CommonProfile profile, ActorRef userActor) {
        this.profile = profile;
        this.userActor = userActor;
    }

    public CommonProfile getProfile() {
        return profile;
    }

    public ActorRef getUserActor() {
        return userActor;
    }

}
