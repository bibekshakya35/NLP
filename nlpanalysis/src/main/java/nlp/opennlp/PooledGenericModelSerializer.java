

package nlp.opennlp;

import opennlp.model.AbstractModel;
import opennlp.model.BinaryFileDataReader;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.model.ArtifactSerializer;
import opennlp.tools.util.model.GenericModelSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/** A variant of {@link opennlp.tools.util.model.GenericModelSerializer} that
 *  conserves memory by interning the strings read as a part of a model
 *  by using a {@link PooledGenericModelReader} to read the model.
 */
public class PooledGenericModelSerializer extends GenericModelSerializer {

  @Override
  public AbstractModel create(InputStream in) throws IOException,
          InvalidFormatException {
    return new PooledGenericModelReader(new BinaryFileDataReader(in)).getModel();
  }

  @SuppressWarnings("rawtypes")
  public static void register(Map<String, ArtifactSerializer> factories) {
    factories.put("model", new PooledGenericModelSerializer());
  }
}
