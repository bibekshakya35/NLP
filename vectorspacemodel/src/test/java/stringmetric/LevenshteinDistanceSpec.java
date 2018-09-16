package stringmetric;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bibek on 1/14/18
 * @project vectorspacemodel
 */
public class LevenshteinDistanceSpec {
    @Test
    public void test(){
        int distance = LevenshteinDistance.editDistance("hello","hallo");
        Assert.assertEquals(" Edit distance failed ",1,distance);
    }
}
