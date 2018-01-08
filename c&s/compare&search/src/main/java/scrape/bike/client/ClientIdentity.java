package scrape.bike.client;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
public enum ClientIdentity {
    AUTOLIFE("autolife"),
    NEWPRICENEPAL("newpricenepal");
    private final String identity;
     ClientIdentity(String identity){
        this.identity=identity;
    }

    public String getIdentity() {
        return identity;
    }
}
