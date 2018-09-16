package preprocess.client;

import scrape.bike.client.ClientIdentity;

/**
 * @author bibek on 12/24/17
 * @project compare&search
 */
public class ClientPreProcessing {
    private ClientIdentity clientIdentity;
    private ClientPreprocessing clientPreprocessing;

    public ClientPreProcessing(ClientIdentity clientIdentity, ClientPreprocessing clientPreprocessing) {
        this.clientIdentity = clientIdentity;
        this.clientPreprocessing = clientPreprocessing;
    }

    public ClientIdentity getClientIdentity() {
        return clientIdentity;
    }

    public ClientPreprocessing getClientPreprocessing() {
        return clientPreprocessing;
    }
}
