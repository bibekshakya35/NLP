package nlp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import nlp.opennlp.PooledTokenNameFinderModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;



/** Encapsulates OpenNLP's NameFinder by providing a mechanism to load
 *  all of the name finder models files found in a single directory into memory
 *  and instantiating an array of NameFinderME objects.
 */
@Slf4j
public class NameFinderFactory {

    NameFinderME[] finders;
    String[] modelNames;

    /** Create a NameFinderEngine that loads models from the directory specified
     *  in the system property <code>model.dir</code> system property for the
     *  english language
     *
     * @param modelDirectory
     *   the directory containing the model files, can be null to force
     *   use of the model.dir system property.
     * @throws IOException
     */
    public NameFinderFactory() throws IOException {
        this(null);
    }

    public NameFinderFactory(Map<String,String> param) throws IOException {

        String language = OpenNLPUtil.getModelLanguage(param);
        String modelDirectory = OpenNLPUtil.getModelDirectory(param);
        log.info("here is the language : {} and model directory : ",new Object[]{language,modelDirectory} );
        loadNameFinders(language, modelDirectory);
    }

    /** Create a NameFinderEngine that loads models from the specified directory,
     *  or, reads the <code>model.dir</code> system property in order to determine
     *  if the <code>modelDirectory</code> is <code>null</code> or empty.
     * @param language
     *   two letter language prefix from the model file names.
     * @param modelDirectory
     *   the directory containing the model files, can be null to force
     *   use of the model.dir system property.
     * @throws IOException
     */
    public NameFinderFactory(String language, String modelDirectory) throws IOException {
       loadNameFinders(language, modelDirectory);
    }

    /** Load the name finder models. Currently any file in the model directory
     *  that starts with (lang)-ner
     * @param language
     * @param modelDirectory
     *    can be null to use the value of the system property model.dir
     * @return
     */
    protected File[] findNameFinderModels(String language, String modelDirectory) {
        final String modelPrefix = language + "-ner";

        log.info("Loading name finder models from {} using prefix {} ",
                new Object[] { modelDirectory, modelPrefix } );

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

    /** Load name finder models based upon models for the specified language
     *  in the specified model directory.
     *
     * @param language
     * @param modelDirectory
     *      can be null to use the value of the system property model.dir
     * @throws IOException
     */
    protected void loadNameFinders(String language, String modelDirectory) throws IOException {

        File modelFile;

        File[] models
                = findNameFinderModels(language, modelDirectory);
        log.info("here is models length: {}",new Object[]{models.length});
        modelNames = new String[models.length];
        finders = new NameFinderME[models.length];

        for (int fi = 0; fi < models.length; fi++) {
            modelFile = models[fi];
            modelNames[fi] = modelNameFromFile(language, modelFile);

            log.info("Loading model {}", modelFile);
            InputStream modelStream = new FileInputStream(modelFile);
            TokenNameFinderModel model =
                    new PooledTokenNameFinderModel(modelStream);
            finders[fi] = new NameFinderME(model);

        }
    }

    /** Extract the model name from the model file, this is used to display
     *  the type of named entity found
     * @param language
     * @param modelFile
     * @return
     */
    protected String modelNameFromFile(String language, File modelFile) {
        String modelName = modelFile.getName();
        return modelName.replace(language + "-ner-", "").replace(".bin", "");
    }

    /** Obtain a reference to the array of NameFinderME's loaded by the engine.
     * @return
     */
    public NameFinderME[] getNameFinders() {
        return finders;
    }

    /** Returns the names of each of the models loaded by the engine, an array
     *  parallel with the array returned by {@link #getFinders()}
     * @return
     */
    public String[] getModelNames() {
        return modelNames;
    }

}