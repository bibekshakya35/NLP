package preprocess.client;

import preprocess.*;
import preprocess.constant.BodyExterior;
import preprocess.constant.ChasisBrakeSuspension;
import preprocess.constant.EngineSpecification;
import preprocess.constant.SpecCategory;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.reflect.TypeToken;
import scrape.bike.BikeBrand;
import util.JsonUtils;
import util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public class AutoLifePreProcessing implements ClientPreprocessing{

    public List<BikeBrand> clean(List<BikeBrand> autoLifeList) throws IOException {
        Type bikeBrandClazz = new TypeToken<ArrayList<BikeBrand>>() {
        }.getType();
        String bikeBrand = org.apache.commons.io.FileUtils.readFileToString(new File("/home/bibek/bs-workspace/bike/preprocess/scrape/autolife.json"), StandardCharsets.UTF_8);
        autoLifeList = JsonUtils.fromJsonToList(bikeBrand,
                bikeBrandClazz);
        autoLifeList.forEach(autoLifeBikeBrand -> {
            BikeCleaner.processCleansing(autoLifeBikeBrand);
        });
        org.apache.commons.io.FileUtils.writeStringToFile(new File("/home/bibek/bs-workspace/bike/preprocess/clean/autolife.json"), JsonUtils.toJsonList(autoLifeList), StandardCharsets.UTF_8);
        return autoLifeList;
    }
    public List<BikeProduct> normalized(List<BikeBrand> autoLifeList){
        List<BikeProduct> normalizedBikeProducts = new ArrayList<>();
        autoLifeList.forEach(
                b -> {
                    BikeProduct bikeProduct = new BikeProduct();
                    bikeProduct.setCompanyLogo(b.getLogo());
                    bikeProduct.setCompanyUrl(b.getOfficialUrl());
                    bikeProduct.setCompanyName(b.getName());
                    bikeProduct.setShowRooms(new ArrayList<>(b.getShowRooms()));
                    b.getBikes().forEach(bike -> {
                        bikeProduct.setName(bike.getName());
                        bikeProduct.setPrice(bike.getPrice());
                        bikeProduct.setImageUrls(bike.getImageUrls());

                        bikeProduct.setBikeIdentity(new BikeIdentity(b.getName(),bike.getName()));
                        SpecWithCategory specWithCategory = new SpecWithCategory();
                        String[] value = SpecWithCategory.class.getAnnotation(JsonPropertyOrder.class).value();
                        Map<String, SpecCategory> specCategoryMap = new HashMap<>();
                        for (String v : value) {
                            specCategoryMap.put(StringUtils.lowerCaseWithNoWhiteSpace(v), SpecCategory.valueOf(v));
                        }
                        bike.getSpecWithCategory().forEach((k, v) -> {;
                            String key = StringUtils.lowerCaseWithNoWhiteSpace(k);
                            List<String> values = v;
                            switch (specCategoryMap.get(key)) {
                                case Variants:
                                    specWithCategory.setVariants(values);
                                    break;
                                case ChassisBrakesSuspensions:
                                    String[] chasisBrakeSus = ChasisBrakeSuspensions.class.getAnnotation(JsonPropertyOrder.class).value();
                                    Map<ChasisBrakeSuspension, String> chasisBrakeSusMap = new HashMap<>();
                                    v.forEach(val -> {
                                        String[] splitter = val.split(":");
                                        if (splitter.length == 2) {
                                            if (StringUtils.isNotBlank(splitter[0])) {
                                                String keyC = splitter[0];
                                                chasisBrakeSusMap.put(ChasisBrakeSuspension.getType(keyC.trim()), splitter[1]);
                                            }
                                        }
                                    });
                                    ChasisBrakeSuspensions chasisBrakeSuspension = this.chasisBrakeSuspension(chasisBrakeSus, chasisBrakeSusMap);
                                    specWithCategory.setChasisBrakeSuspensions(chasisBrakeSuspension);
                                    break;
                                case Transmission:
                                    String[] transmissionValue = Transmission.class.getAnnotation(JsonPropertyOrder.class).value();
                                    Map<preprocess.constant.Transmission, String> transmissionMap = new HashMap<>();
                                    v.forEach(val -> {
                                        String[] splitter = val.split(":");
                                        if (splitter.length == 2) {
                                            if (StringUtils.isNotBlank(splitter[0])) {
                                                String keyC = splitter[0];
                                                transmissionMap.put(preprocess.constant.Transmission.getType(keyC), splitter[1]);
                                            }
                                        }
                                    });
                                    Transmission transmission = this.mutateTransmission(transmissionMap, transmissionValue);
                                    specWithCategory.setTransmission(transmission);
                                    break;
                                case Performance:
                                    String[] performanceValue = Performance.class.getAnnotation(JsonPropertyOrder.class)
                                            .value();
                                    Map<preprocess.constant.Performance, String> performanceMapper = new HashMap<>();
                                    v.forEach(val -> {
                                        String[] splitter = val.split(":");
                                        if (splitter.length == 2) {
                                            if (StringUtils.isNotBlank(splitter[0])) {
                                                String keyC = splitter[0];
                                                performanceMapper.put(preprocess.constant.Performance.getType(keyC), splitter[1]);
                                            }
                                        }
                                    });
                                    Performance performance = this.mutatePerformance(performanceMapper, performanceValue);
                                    specWithCategory.setPerformance(performance);
                                    break;
                                case EngineSpecifications:
                                    String[] engineValue = EngineSpecifications.class.getAnnotation(JsonPropertyOrder.class)
                                            .value();
                                    Map<EngineSpecification, String> engineMapper = new HashMap<>();
                                    v.forEach(val -> {
                                        String[] splitter = val.split(":");
                                        if (splitter.length == 2) {
                                            if (StringUtils.isNotBlank(splitter[0])) {
                                                String keyC = splitter[0];
                                                engineMapper.put(preprocess.constant.EngineSpecification.getType(keyC), splitter[1]);
                                            }
                                        }
                                    });
                                    EngineSpecifications engineSpecifications = this.mutateEngineSpecifications(engineMapper, engineValue);
                                    specWithCategory.setEngineSpecifications(engineSpecifications);
                                    break;
                                case BodyExteriorDesignDimensions:
                                    String[] bodyExterior = BodyExteriorDesignDimensions.class.getAnnotation(JsonPropertyOrder.class).value();
                                    Map<BodyExterior, String> bodyExteriorMapper = new HashMap<>();
                                    v.forEach(val -> {
                                        String[] splitter = val.split(":");
                                        if (splitter.length == 2) {
                                            if (StringUtils.isNotBlank(splitter[0])) {
                                                String keyC = splitter[0];
                                                bodyExteriorMapper.put(preprocess.constant.BodyExterior.getType(keyC), splitter[1]);
                                            }
                                        }
                                    });
                                    BodyExteriorDesignDimensions bodyExteriorDesignDimensions = this.mutateBodyExterior(bodyExterior, bodyExteriorMapper);
                                    specWithCategory.setBodyExteriorDesignDimensions(bodyExteriorDesignDimensions);
                                    break;
                                default:
                                    break;
                            }
                        });
                        bikeProduct.setSpecWithCategory(specWithCategory);
                        normalizedBikeProducts.add(bikeProduct);
                    });
                }
        );
        return normalizedBikeProducts;
    }
    private BodyExteriorDesignDimensions mutateBodyExterior(String[] bodyExterior, Map<BodyExterior, String> bodyExteriorMapper) {
        BodyExteriorDesignDimensions bodyExteriorDesignDimensions = new BodyExteriorDesignDimensions();
        for (String body : bodyExterior) {
            BodyExterior bodyExteriorEnum = BodyExterior.getType(body);
            switch (bodyExteriorEnum) {
                case Width:
                    bodyExteriorDesignDimensions.setWidth(bodyExteriorMapper.get(BodyExterior.Width));
                    break;
                case Height:
                    bodyExteriorDesignDimensions.setHeight(bodyExteriorMapper.get(BodyExterior.Height));
                    break;
                case Body_Type:
                    bodyExteriorDesignDimensions.setBodyType(bodyExteriorMapper.get(BodyExterior.Body_Type));
                    break;
                case Fuel_Capacity:
                    bodyExteriorDesignDimensions.setFuelCapacity(bodyExteriorMapper.get(BodyExterior.Fuel_Capacity));
                    break;
                case Wheelbase:
                    bodyExteriorDesignDimensions.setWheelbase(bodyExteriorMapper.get(BodyExterior.Wheelbase));
                    break;
                case Ground_Clearance:
                    bodyExteriorDesignDimensions.setGroundClearance(bodyExteriorMapper.get(BodyExterior.Ground_Clearance));
                    break;
                case Length:
                    bodyExteriorDesignDimensions.setLength(bodyExteriorMapper.get(BodyExterior.Length));
                    break;
                case Weight:
                    bodyExteriorDesignDimensions.setWeight(bodyExteriorMapper.get(BodyExterior.Weight));
                    break;
                default:
                    break;
            }

        }
        return bodyExteriorDesignDimensions;
    }

    private EngineSpecifications mutateEngineSpecifications(Map<EngineSpecification, String> engineMapper, String[] engineValue) {
        EngineSpecifications engineSpecification = new EngineSpecifications();
        for (String engine : engineValue) {
            EngineSpecification engineSpec = EngineSpecification.getType(engine);
            switch (engineSpec) {
                case POWER:
                    engineSpecification.setPower(engineMapper.get(EngineSpecification.POWER));
                    break;
                case BORE_X_STROKE:
                    engineSpecification.setBoreXStroke(engineMapper.get(EngineSpecification.BORE_X_STROKE));
                    break;
                case Idle_Speed:
                    engineSpecification.setIdleSpeed(engineMapper.get(EngineSpecification.Idle_Speed));
                    break;
                case Carburettor:
                    engineSpecification.setCarburettor(engineMapper.get(EngineSpecification.Carburettor));
                    break;
                case TORQUE:
                    engineSpecification.setTorque(engineMapper.get(EngineSpecification.TORQUE));
                    break;
                case DISPLACEMENT:
                    engineSpecification.setDisplacement(engineMapper.get(EngineSpecification.DISPLACEMENT));
                    break;
                case FINAL_DRIVE:
                    engineSpecification.setFinalDrive(engineMapper.get(EngineSpecification.FINAL_DRIVE));
                    break;
                case Compression_Ratio:
                    engineSpecification.setCompressionRatio(engineMapper.get(EngineSpecification.Compression_Ratio));
                    break;
                case Power_to_Weight_Ratio:
                    engineSpecification.setPowerToWeightRatio(engineMapper.get(EngineSpecification.Power_to_Weight_Ratio));
                    break;
                case ENGINE_TYPE:
                    engineSpecification.setEngineType(engineMapper.get(EngineSpecification.ENGINE_TYPE));
                    break;
                case Starting_System:
                    engineSpecification.setStartingSystem(engineMapper.get(EngineSpecification.Starting_System));
                    break;
                default:
                    break;
            }
        }
        return engineSpecification;
    }


    private Performance mutatePerformance(Map<preprocess.constant.Performance, String> performanceMapper, String[] performanceValue) {
        Performance performance = new Performance();
        for (String perf : performanceValue) {
            preprocess.constant.Performance performanceEnum = preprocess.constant.Performance.getType(perf);
            switch (performanceEnum) {
                case MAX_SPEED:
                    performance.setMaxSpeed(performanceMapper.get(preprocess.constant.Performance.MAX_SPEED));
                    break;
                case ACCELERATION:
                    performance.setAcceleration(performanceMapper.get(preprocess.constant.Performance.ACCELERATION));
                    break;
                case FUEL_MILEAGE:
                    performance.setFuelMileage(performanceMapper.get(preprocess.constant.Performance.FUEL_MILEAGE));
                    break;
                default:
                    break;
            }
        }
        return performance;
    }

    private Transmission mutateTransmission(Map<preprocess.constant.Transmission, String> transmissionMap, String[] transmissionValue) {
        Transmission transmission = new Transmission();
        for (String trans : transmissionValue) {
            preprocess.constant.Transmission transmissionEnum = preprocess.constant.Transmission.getType(trans);
            switch (transmissionEnum) {
                case CLUTCH:
                    transmission.setClutch(transmissionMap.get(preprocess.constant.Transmission.CLUTCH));
                    break;
                case TRANSMISSION:
                    transmission.setTransmission(transmissionMap.get(preprocess.constant.Transmission.TRANSMISSION));
                    break;
                case NUMBER_OF_GEAR:
                    transmission.setNumberOfGears(transmissionMap.get(preprocess.constant.Transmission.NUMBER_OF_GEAR));
                    break;
                default:
                    break;
            }
        }
        return transmission;
    }


    private ChasisBrakeSuspensions chasisBrakeSuspension(String[] chasisBrakeSus, Map<ChasisBrakeSuspension, String> chasisBrakeSusMap) {
        ChasisBrakeSuspensions chasisObj = new ChasisBrakeSuspensions();
        for (String c : chasisBrakeSus) {
            ChasisBrakeSuspension chasisBrakeSuspension = ChasisBrakeSuspension.getType(c);
            switch (chasisBrakeSuspension) {
                case Chassis: {
                    chasisObj.setChassis(chasisBrakeSusMap.get(ChasisBrakeSuspension.Chassis));
                    break;
                }
                case FrontSuspension: {
                    chasisObj.setFrontSuspension(chasisBrakeSusMap.get(ChasisBrakeSuspension.FrontSuspension));
                    break;
                }
                case FrontTyre: {
                    chasisObj.setFrontTyre(chasisBrakeSusMap.get(ChasisBrakeSuspension.FrontTyre));
                    break;
                }
                case RearSuspension:
                    chasisObj.setRearSuspension(chasisBrakeSusMap.get(ChasisBrakeSuspension.RearSuspension));
                    break;
                case FrontTyreBrake: {
                    chasisObj.setFrontTyreBrake(chasisBrakeSusMap.get(ChasisBrakeSuspension.FrontTyreBrake));
                    break;
                }
                case SeatType: {
                    chasisObj.setSeatType(chasisBrakeSusMap.get(ChasisBrakeSuspension.SeatType));
                    break;
                }
                case SpokeType: {
                    chasisObj.setSpokeType(chasisBrakeSusMap.get(ChasisBrakeSuspension.SpokeType));
                    break;
                }
                case RearTyre: {
                    chasisObj.setRearTyre(chasisBrakeSusMap.get(ChasisBrakeSuspension.RearTyre));
                    break;
                }
                case RearTyreBrake: {
                    chasisObj.setRearTyreBrake(chasisBrakeSusMap.get(ChasisBrakeSuspension.RearTyreBrake));
                    break;
                }
                default: {
                    break;
                }

            }
        }
        return chasisObj;
    }

}
