package nlp.opennlp;

import opennlp.tools.util.model.ArtifactSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

class PropertiesSerializer implements ArtifactSerializer<Properties> {
    PropertiesSerializer() {
    }

    public Properties create(InputStream in) throws IOException {
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }

    public void serialize(Properties properties, OutputStream out) throws IOException {
        properties.store(out, "");
    }

    static void register(Map<String, ArtifactSerializer> factories) {
        factories.put("properties", new PropertiesSerializer());
    }
}
