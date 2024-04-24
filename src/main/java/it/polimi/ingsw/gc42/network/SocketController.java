package it.polimi.ingsw.gc42.network;

import java.io.IOException;

public class SocketController implements NetworkController{
    private Runnable onReady;
    @Override
    public String getIpAddress() {
        return null;
    }

    @Override
    public String getPort() {
        return null;
    }

    @Override
    public void setWhenReady(Runnable runnable) {
        this.onReady = runnable;
    }

    @Override
    public void start() throws IOException {

    }
}
