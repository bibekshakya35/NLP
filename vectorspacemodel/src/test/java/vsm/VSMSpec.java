package vsm;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author bibek on 1/7/18
 * @project vectorspacemodel
 */
public class VSMSpec {
    @Test
    public void shouldBuildDictionary() throws FileNotFoundException {
        VectorSpaceModel vectorSpaceModel = new VectorSpaceModel();
        Map<String,Long> dictionary = vectorSpaceModel.buildDictionary();
        Assert.assertNotNull(dictionary);
    }
}
