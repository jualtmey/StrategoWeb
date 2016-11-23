package actors;

import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(WebSocketActor.class, out);
    }

    private final ActorRef out;
    private final ActorRef lobby;
    private ActorRef game;

    public WebSocketActor(ActorRef out) {
        this.out = out;
        this.lobby = controllers.WebSocketController.lobby;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonMessage = mapper.readTree((String) message);

            String command = jsonMessage.findPath("command").textValue();

            if ("lobby".equals(command)) {
                lobby.tell(new LobbyProtocol.Join("unnamed"), self());
            } else if ("add".equals(command)) {
                int row = jsonMessage.findPath("row").intValue();
                int column = jsonMessage.findPath("column").intValue();
                int rank = jsonMessage.findPath("rank").intValue();
                game.tell(new GameProtocol.Add(row, column, rank), self());
            } else if ("swap".equals(command)) {
                int fromRow = jsonMessage.findPath("fromRow").intValue();
                int fromColumn = jsonMessage.findPath("fromColumn").intValue();
                int toRow = jsonMessage.findPath("toRow").intValue();
                int toColumn = jsonMessage.findPath("toColumn").intValue();
                game.tell(new GameProtocol.Swap(fromRow, fromColumn, toRow, toColumn), self());
            } else if ("remove".equals(command)) {
                int row = jsonMessage.findPath("row").intValue();
                int column = jsonMessage.findPath("column").intValue();
                game.tell(new GameProtocol.Remove(row, column), self());
            } else if ("move".equals(command)) {
                int fromRow = jsonMessage.findPath("fromRow").intValue();
                int fromColumn = jsonMessage.findPath("fromColumn").intValue();
                int toRow = jsonMessage.findPath("toRow").intValue();
                int toColumn = jsonMessage.findPath("toColumn").intValue();
                game.tell(new GameProtocol.Move(fromRow, fromColumn, toRow, toColumn), self());
            } else if ("finish".equals(command)) {
                game.tell(new GameProtocol.Finish(), self());
            }

//            if (message.equals("lobby")) {
//
//            } else if (message.equals("entered")) {
//                //out.tell("entered lobby", self());
//            } else if (message.equals("new")) {
//                //out.tell("new client in lobby", self());
//            }
        } else if (message instanceof LobbyProtocol.NewGame) {
            LobbyProtocol.NewGame newGame = (LobbyProtocol.NewGame) message;
            game = newGame.game;
        } else if (message instanceof GameProtocol.Refresh) {
            GameProtocol.Refresh refresh = (GameProtocol.Refresh) message;
            out.tell(refresh.json, self());
        }
        
    }
    
    public void postStop() {
        lobby.tell(new LobbyProtocol.Leave(), self());
    }

}