package actors;

import akka.actor.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pac4j.core.profile.CommonProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    private Map<String, User> users = new HashMap<>();

    public void join(User user) {
        users.put(user.getProfile().getEmail(), user);
    }

    private List<String> getUserEmailList() {
        List<String> userList = new ArrayList<>();
        for (User user : users.values()) {
            userList.add(user.getProfile().getEmail());
        }
        return userList;
    }

    public List<ActorRef> getAllUserActors() {
        List<ActorRef> userActorList = new ArrayList<>();
        for (User user: users.values()) {
            userActorList.add(user.getUserActor());
        }
        return userActorList;
    }

    public String toJson() {
        String result = "";

        Map<String, Object> lobbyJson = new HashMap<>();
        lobbyJson.put("users", getUserEmailList());

        lobbyJson.put("openGames", "test");

        ObjectMapper mapper = new ObjectMapper();

        try {
            result = mapper.writeValueAsString(lobbyJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
