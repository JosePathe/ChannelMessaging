package berthet.johann.channelmessaging.network;

/**
 * Created by Johann on 02/02/2016.
 */
public interface onWSRequestListener {
    public void onWSRequestCompleted(String response);
    public void onWSRequestError(String exception);
}
