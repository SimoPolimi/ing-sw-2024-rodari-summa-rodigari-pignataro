package it.polimi.ingsw.gc42.network;

public class KeepAliveTimer implements Runnable {
    private ClientState isAlive;

    public KeepAliveTimer () {
        isAlive = ClientState.ALIVE;
    }

    public ClientState isAlive() {
        return isAlive;
    }

    public void setAlive(ClientState state) {
        isAlive = state;
    }

    //TODO: remove
    @Override
    public void run() {
        switch (isAlive) {
            case UNRESPONSIVE -> System.out.println("UNRESPONSIVE");
            case DEAD -> System.out.println("DEAD");
            case EXPECT_ALIVE -> isAlive = ClientState.DEAD;
            default -> isAlive = ClientState.EXPECT_ALIVE;
        }
    }
}
