package preprocess;

import preprocess.client.ClientPreProcessing;
import preprocess.client.ClientPreprocessingFactory;
import scrape.bike.BikeBrand;
import scrape.bike.client.ClientIdentity;
import util.FileUtils;
import util.StringUtils;

import java.io.IOException;
import java.util.*;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
public class BikePreprocessingImpl implements BikePreProcessing<BikeProduct> {
    private List<BikeBrand> autoLifeList;
    private List<BikeBrand> newPriceList;
    private List<ClientPreProcessing> clientPreprocessings;
    private List<BikeProduct> normalizedBikeProducts;
    private List<BikeProduct> transformationBikeProducts;

    public BikePreprocessingImpl() {
        this.init();
    }

    @Override
    public void init() {
        autoLifeList = new ArrayList<>();
        newPriceList = new ArrayList<>();
        clientPreprocessings = new ArrayList<>();
        normalizedBikeProducts = new ArrayList<>();
        transformationBikeProducts = new ArrayList<>();
        for (ClientIdentity c :
                ClientIdentity.values()) {
            clientPreprocessings.add(ClientPreprocessingFactory.clientPreprocessing(c));
        }
    }

    @Override
    public void cleaning() {
        this.clean();
    }

    @Override
    public List<String> nameOfBike() {
        List<String> nameOfBikes = new ArrayList<>();
        autoLifeList.forEach(autoLifeBike -> {
            autoLifeBike.getBikes().forEach(b -> {
                String bikeName = StringUtils.lowerCase(b.getName());
                nameOfBikes.add(bikeName);
            });
        });
        newPriceList.forEach(newPriceBike -> {
            String bikeName = StringUtils.lowerCase(newPriceBike.getName());
            newPriceBike.getBikes().forEach(bike -> nameOfBikes.add(bikeName));
        });
        return nameOfBikes;
    }

    @Override
    public List<String> distinctBikes() {
        return this.nameOfBike().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> duplicateBikes() {
        List<String> nameOfBikes = this.nameOfBike();
        List<String> distinctBikes = this.distinctBikes();
        nameOfBikes.removeAll(distinctBikes);
        return nameOfBikes;
    }

    @Override
    public List<BikeProduct> normalized() {
        clientPreprocessings.forEach(c -> {
            switch (c.getClientIdentity()) {
                case NEWPRICENEPAL:
                    normalizedBikeProducts.addAll(c.getClientPreprocessing().normalized(newPriceList));
                    break;
                case AUTOLIFE:
                    normalizedBikeProducts.addAll(c.getClientPreprocessing().normalized(autoLifeList));
                    break;
            }
        });
        return normalizedBikeProducts;
    }


    private void clean() {
        clientPreprocessings.forEach(c -> {
            switch (c.getClientIdentity()) {
                case NEWPRICENEPAL:
                    try {
                        newPriceList = c.getClientPreprocessing().clean(newPriceList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case AUTOLIFE:
                    try {
                        autoLifeList = c.getClientPreprocessing().clean(autoLifeList);
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                    break;
            }
        });
    }

    @Override
    public List<BikeProduct> transformation() {
        List<BikeIdentity> bikeIdentities = new ArrayList<>();
        normalizedBikeProducts.forEach(bikeProduct -> bikeIdentities.add(bikeProduct.getBikeIdentity()));
        List<BikeIdentity> uniqueBikeIdentities = bikeIdentities.stream()
                .collect(Collectors.collectingAndThen(toCollection(() ->
                        new TreeSet<>(Comparator.comparing(BikeIdentity::getBikeName))
                ), ArrayList::new));
        uniqueBikeIdentities.forEach(uniqueBikeIdentity -> {
            Optional<BikeProduct> bikeProduct = normalizedBikeProducts.stream().filter(n -> n.getBikeIdentity().equals(uniqueBikeIdentity)).findFirst();
            transformationBikeProducts.add(bikeProduct.get());
        });
        return transformationBikeProducts;
    }

    @Override
    public void download() {
        BikePreProcessing bikePreProcessing = new BikePreprocessingImpl();
        bikePreProcessing.cleaning();
        List<BikeProduct> bikeProducts = bikePreProcessing.normalized();
        bikeProducts = bikePreProcessing.transformation();
        FileUtils.downloadFile(bikeProducts, "/home/bibek/bs-workspace/bike/preprocess/transform/bike_products.json");
    }
}
