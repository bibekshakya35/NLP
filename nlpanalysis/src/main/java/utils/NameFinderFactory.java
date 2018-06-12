package utils;

import opennlp.PooledTokenNameFinderModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * Encapsulates OpenNLP's NameFiner by providing a mechanism to load
 * all the name finder models files found in a single directory into memory
 * and instantiating an array of NameFinderME objects
 */
public class NameFinderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(NameFinderFactory.class);
    NameFinderME[] nameFinderMES;
    String[] modelNames;

    /**
     * Create a NameFinderEngine that loads models from the directory specified
     * in the system property <code>model.dir</code> system property for the
     * english language
     *
     * @param modelDirectory the directory containing the model files, can be null to force
     *                       use of the model.dir system property.
     * @throws IOException
     */
    public NameFinderFactory() {
    }

    public NameFinderFactory(Map<String, String> param) throws IOException {
        String language = OpenNLPUtil.getModelLanguage(param);
        String modelDirectory = OpenNLPUtil.getModelDirectory(param);
        loadNameFinders(language, modelDirectory);
    }

    /**
     * Create a NameFinderEngine that loads models from the specified directory,
     * or, reads the <code>model.dir</code> system property in order to determine
     * if the <code>modelDirectory</code> is <code>null</code> or empty.
     *
     * @param language       two letter language prefix from the model file names.
     * @param modelDirectory the directory containing the model files, can be null to force
     *                       use of the model.dir system property.
     * @throws IOException
     */
    public NameFinderFactory(String language, String modelDirectory) throws IOException {
        loadNameFinders(language, modelDirectory);
    }

    /**
     * Load name finder models based upon models for the specified language
     * in the specified model directory.
     *
     * @param language
     * @param modelDirectory can be null to use the value of the system property model.dir
     * @throws IOException
     */
    private void loadNameFinders(String language, String modelDirectory) throws IOException {
        File modelFile;
        File[] models = findNameFinderModels(language, modelDirectory);
        modelNames = new String[models.length];
        nameFinderMES = new NameFinderME[models.length];
        for (int i = 0; i < models.length; i++) {
            modelFile = models[i];
            modelNames[i] = modeNameFromFile(language, modelFile);
            LOG.info("Loading model {} ", modelFile);
            InputStream modelStream = new FileInputStream(modelFile);
            TokenNameFinderModel model =
                    new PooledTokenNameFinderModel(modelStream);
            nameFinderMES[i] = new NameFinderME(model);

        }
    }

    /**
     * Extract the model name from the model file,
     * this is used to display
     * the type of named entity found
     *
     * @param language
     * @param modelFile
     * @return
     */
    private String modeNameFromFile(String language, File modelFile) {
        String modelName = modelFile.getName();
        return modelName.replace(language + "-ner-", "").replace(".bin", "");
    }

    /**
     * Load the name finder models. Currently any file in the model directory
     * that starts with (lang)-ner
     *
     * @param language
     * @param modelDirectory can be null to use the value of the system property model.dir
     * @return
     */
    private File[] findNameFinderModels(String language, String modelDirectory) {
        final String modelPrefix = language + "-ner";
        LOG.info("Loading name finder models form {} using prefix {}",
                new Object[]{modelDirectory, modelPrefix});
        File[] models = new File(modelDirectory).listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                if (name.startsWith(modelPrefix)) {
                    return true;
                }
                return false;
            }
        });
        if (models == null || models.length < 1) {
            throw new RuntimeException("Configuration Error: No models in " + modelDirectory);
        }
        return models;
    }

    public NameFinderME[] getNameFinderMES() {
        return nameFinderMES;
    }

    public String[] getModelNames() {
        return modelNames;
    }
}
