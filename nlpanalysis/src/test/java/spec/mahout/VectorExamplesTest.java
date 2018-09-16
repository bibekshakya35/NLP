package spec.mahout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.utils.vectors.io.SequenceFileVectorWriter;
import org.apache.mahout.utils.vectors.io.VectorWriter;
import org.junit.Test;
import spec.TamingTextTestJ4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VectorExamplesTest extends TamingTextTestJ4 {
    @Test
    public void testProgrammatic() throws Exception {
        double[] vals = new double[]{0.3, 1.8, 200.228};
        //Create DenseVector with label of my-dense and 3 values. The cardinality of this vector is 3
        Vector dense = new DenseVector(vals);
        assertTrue(dense.size() == 3);
        //create SparseVector with a label of my-sparse-same that has cardinality of 3
        Vector sparseSame = new SequentialAccessSparseVector(3);
        //create SparseVector with a label of my-sparse and a cardinality of 3000
        Vector sparse = new SequentialAccessSparseVector(3600);
        for (int i = 0; i < vals.length; i++) {
            sparseSame.set(i, vals[i]);
            sparse.set(i, vals[i]);
        }
        // the dense and sparse Vectors are not equal because they have different cardinality
        assertFalse(dense.equals(sparse));
        //the dense and sparse Vectors are equals because they have the same values
        assertEquals(dense, sparseSame);
        assertFalse(sparse.equals(sparseSame));
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File tmpLoc = new File(tmpDir, "sfvwt");
        tmpLoc.mkdir();
        File tmpFile = File.createTempFile("sfvwt", ".dat", tmpLoc);

        Path path = new Path(tmpFile.getAbsolutePath());
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(configuration);
        SequenceFile.Writer seqWriter = SequenceFile.createWriter(fs, configuration, path,
                LongWritable.class, VectorWritable.class);
        VectorWriter vectorWriter = new SequenceFileVectorWriter(seqWriter);
        List<Vector> vectorList = new ArrayList<>();
        vectorList.add(sparse);
        vectorList.add(sparseSame);
        vectorWriter.write(vectorList);
        vectorWriter.close();
    }


}
