package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.htwg.stratego.StrategoModule;
import de.htwg.stratego.controller.IMultiDeviceStrategoController;
import de.htwg.stratego.model.IPlayer;

public class GameActor extends UntypedActor {

    public static Props props(ActorRef playerOne, ActorRef playerTwo) {
        return Props.create(GameActor.class, playerOne, playerTwo);
    }

    private ActorRef playerOne;
    private ActorRef playerTwo;

    private IMultiDeviceStrategoController strategoController;

    public GameActor(ActorRef playerOne, ActorRef playerTwo) {
        Injector injector = Guice.createInjector(new StrategoModule());
		strategoController = injector.getInstance(IMultiDeviceStrategoController.class);
		
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
    }

    public void onReceive(Object message) throws Exception {
        IPlayer player;
        if (playerOne.equals(sender())) {
            player = strategoController.getPlayerOne();
        } else {
            player = strategoController.getPlayerTwo();
        }

        System.out.println(strategoController.getStatusString());

        if (message instanceof GameProtocol.Add) {
            GameProtocol.Add add = (GameProtocol.Add) message;
            strategoController.add(add.column, add.row, add.rank, player);
            notifyPlayer();
        } else if (message instanceof GameProtocol.Swap) {
            GameProtocol.Swap swap = (GameProtocol.Swap) message;
            strategoController.swap(swap.fromColumn, swap.fromRow, swap.toColumn, swap.toRow, player);
            notifyPlayer();
        } else if (message instanceof GameProtocol.Remove) {
            GameProtocol.Remove remove = (GameProtocol.Remove) message;
            strategoController.remove(remove.column, remove.row, player);
            notifyPlayer();
        } else if (message instanceof GameProtocol.Move) {
            GameProtocol.Move move = (GameProtocol.Move) message;
            strategoController.move(move.fromColumn, move.fromRow, move.toColumn, move.toRow, player);
            notifyPlayer();
        } else if (message instanceof GameProtocol.Finish) {
            strategoController.finish(player);
            notifyPlayer();
        }

    }

    private void notifyPlayer() {
        playerOne.tell(new GameProtocol.Refresh(strategoController.toJson(strategoController.getPlayerOne())), self());
        playerTwo.tell(new GameProtocol.Refresh(strategoController.toJson(strategoController.getPlayerTwo())), self());
    }

}