package preprocess.client;

import scrape.bike.client.ClientIdentity;

/**
 * @author bibek on 12/24/17
 * @project compare&search
 */
public class ClientPreprocessingFactory {
    private ClientPreprocessingFactory(){}
    public static ClientPreProcessing clientPreprocessing(ClientIdentity clientIdentity){
        switch (clientIdentity){
            case AUTOLIFE:
                return new ClientPreProcessing(clientIdentity,new AutoLifePreProcessing());
            case NEWPRICENEPAL:
                return new ClientPreProcessing(clientIdentity,new NepalPricePreProcessing());
        }
        return null;
    }
}
