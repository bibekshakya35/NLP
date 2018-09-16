package nlp.qa;

import opennlp.model.Event;
import opennlp.model.EventStream;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;

import java.io.*;

public class AnswerTypeEventStream implements EventStream {
    protected BufferedReader reader;
    protected String line;
    protected AnswerTypeContextGenerator atcg;
    protected Parser parser;

    public AnswerTypeEventStream(String fileName, String encoding, AnswerTypeContextGenerator atcg, Parser parser) throws IOException {
        if (encoding == null) {
            reader = new BufferedReader(new FileReader(fileName));
        } else {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
        }
        this.atcg = atcg;
        this.parser = parser;
    }

    public AnswerTypeEventStream(String fileName, AnswerTypeContextGenerator atcg, Parser parser) throws IOException {
        this(fileName, null, atcg, parser);
    }

    /**
     * Creates a new file event stream from the specified file.
     *
     * @param file the file containing the events.
     * @throws IOException When the specified file can not be read.
     */
    public AnswerTypeEventStream(File file) throws IOException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
    }

    public boolean hasNext() {
        try {
            return (null != (line = reader.readLine()));
        } catch (IOException e) {
            System.err.println(e);
            return (false);
        }
    }

    public Event next() {
        int split = line.indexOf(' ');
        String outcome = line.substring(0, split);
        String question = line.substring(split + 1);
        Parse query = ParserTool.parseLine(question, parser, 1)[0];
        return (new Event(outcome, atcg.getContext(query)));
    }



    /**
     * Generates a string representing the specified event.
     *
     * @param event The event for which a string representation is needed.
     * @return A string representing the specified event.
     */
    public static String toLine(Event event) {
        StringBuffer sb = new StringBuffer();
        sb.append(event.getOutcome());
        String[] context = event.getContext();
        for (int ci = 0, cl = context.length; ci < cl; ci++) {
            sb.append(" " + context[ci]);
        }
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
