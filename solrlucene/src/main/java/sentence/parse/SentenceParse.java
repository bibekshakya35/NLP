package sentence.parse;

import common.CommonText;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bibek on 1/10/18
 * @project tamingtext
 */
public class SentenceParse {
    public void parseOPENNLP() throws FileNotFoundException,IOException{
        File parserFile = new File(getModelDir(),"en-parser-chunking.bin");
        FileInputStream fileInputStream = new FileInputStream(parserFile);
        ParserModel parserModel = new ParserModel(parserFile);
        Parser parser = ParserFactory.create(parserModel,
                20,//bean size
                0.95 //advance Percentage
        );
        List<String> datas = CommonText.provideSentenceParser();
        List<Parse[]> parseList = new ArrayList<>();
        datas.forEach(data->parseList.add(ParserTool.parseLine(data,parser,3)));
        parseList.forEach(parses -> {
            for (int i = 0; i < parses.length; i++) {
                parses[i].show();
            }
        });

    }

    private String getModelDir() {
        return "/home/bibek/bs-workspace/tamingtext_resources/opennlp-models";
    }
}
