package preprocess;

import java.util.List;

/**
 * @author bibek on 12/21/17
 * @project compare&search
 * The {@code BikePreProcessing} contains algorithm to
 * <ol>
 * <li>Data Cleaning : {@code
 * {@link BikePreProcessing#cleaning()}} :</br>
 * Clean duplicate bike from different client
 * retrify major field value of {@link BikeProduct}
 * </li>
 * </ol>
 */
public interface BikePreProcessing<T> {
    void init();
    void cleaning();
    //Instance Selection
    List<String> nameOfBike();

    List<String> distinctBikes();

    List<String> duplicateBikes();

    List<T> normalized();
    List<T> transformation();
    void download();
}
