package com.prasnottar.indexing;

import com.prasnottar.indexgui.stop_pause;
import com.prasnottar.wikiclean.WikiClean;
import com.prasnottar.wikiclean.WikiCleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.benchmark.byTask.feeds.DocData;
import org.apache.lucene.benchmark.byTask.feeds.EnwikiContentSource;
import org.apache.lucene.benchmark.byTask.feeds.NoMoreDataException;
import org.apache.lucene.benchmark.byTask.utils.Config;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Slf4j
public class FeedData {
    public static String indexType = null;
    public static String url, filez, name = "";
    public static int skip = 0, back = 0;
    public static RandomAccessFile randomAccessFile = null;
    private static int i, numDocs, batchSize = 5;
    public static boolean running = false, isPaused = false;

    public static void reset() {
        indexType = null;
        filez = "";
        name = "";
        skip = 0;
        back = 0;
        running = false;
        isPaused = false;
    }

    public int index() throws IOException, SolrServerException {
        numDocs = Integer.MAX_VALUE;
        i = 0;
        SolrClient client = new HttpSolrClient.Builder(url)
                .build();
        int result = 0;
        long id = this.readID();
        if (indexType.equals("others")) {
            ContentStreamUpdateRequest request
                    = new ContentStreamUpdateRequest("/update/extract");
            File file = new File(filez);
            if (file.isFile()) {
                request.setParam("literal.id", String.valueOf(id));
                request.addFile(file, "");
                log.info("Before action " + FeedData.filez);
                request.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
                log.info("After action " + FeedData.filez);
                client.request(request);
                log.info("prasnottar learn about " + file.getName() + " and got information");
            }
        } else if (indexType.equals("wikipedia")) {
            log.info("Indexing wikipedia");
            File file = new File(filez);
            log.info("after list files");
            if (file.isFile() && file.getName().equals(".xml")) {
                log.info("in index .xml");
                EnwikiContentSource contentSource = new EnwikiContentSource();
                WikiClean cleaner = new WikiCleanBuilder().build();
                Properties properties = new Properties();
                String filePath = file.getAbsolutePath();
                properties.setProperty("docs.file", filePath);
                properties.setProperty("doc.maker.forever", "false");
                contentSource.setConfig(new Config(properties));
                contentSource.resetInputs();
                DocData docData = new DocData();
                for (int j = 0; j < FeedData.skip; j++) {
                    try {
                        docData = contentSource.getNextDocData(docData);
                    } catch (NoMoreDataException ne) {
                        log.warn("No More Data exeception " + ne.getMessage());
                    }
                    log.info("Title : " + docData.getTitle());
                }
                List<SolrInputDocument> docs = new ArrayList<>(1000);
                i = FeedData.skip;
                SolrInputDocument sDoc = null;
                long start = System.currentTimeMillis();
                try {
                    DocData docData1 = new DocData();
                    while ((docData1 = contentSource.getNextDocData(docData1)) != null &&
                            i < numDocs &&
                            isPaused == false &&
                            running == true) {
                        int mod = i % batchSize;
                        sDoc = new SolrInputDocument();
                        docs.add(sDoc);
                        id++;
                        sDoc.addField("id", "" + id);
                        sDoc.addField("content", cleaner.clean(docData.getBody()));
                        sDoc.addField("title", docData.getTitle());
                        sDoc.addField("author", docData.getName());
                        sDoc.addField("content_type", "Wikipedia Data");
                        String str = docData.getTitle();
                        stop_pause.area.append(str + "\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (mod == batchSize - 1) {
                            log.info("committing....");
                            log.info("Sending: " + docs.size() + " docs" + " total sent for this file: " + i);

                            client.add(docs);
                            client.commit();
                            FeedData.skip = i;
                            log.info("now skip is  " + FeedData.skip);
                            addData(FeedData.randomAccessFile.getFilePointer() - back - 1);
                            System.out.println("now seek is  " + (FeedData.randomAccessFile.getFilePointer() - back - 1));
//                           this.writeID(id);
                            docs.clear();
                        }
                        i++;
                    }
                    log.info("Value of IsPaused : " + FeedData.isPaused);
                } catch (NoMoreDataException ne) {
                }
                long finish = System.currentTimeMillis();
                if (log.isInfoEnabled()) {
                    log.info("Indexing took " + (finish - start) + " ms");
                }
                if (docs.size() > 0) {
                    // client.add(docs);
                }
                result = i + docs.size();
                log.info("finished" + result);
            }
            log.info("After for");
        }
        if (!isPaused) {
            log.info("finished");
            FeedData.skip = -1;
        } else {
            log.info("Indexing is paused");
            log.info("Value of skip : " + FeedData.skip);
        }
        addData(FeedData.randomAccessFile.getFilePointer() - back - 1);
        this.writeID(id);
        client.commit();
        FeedData.running = false;
        client.optimize();
        return result;
    }

    public static void addData(long size) throws IOException {
        //seeking position at end of file
        log.info("The seek is : " + size);
        randomAccessFile.seek(size);
        String s = String.valueOf(FeedData.skip);
        int l = s.length(), i;
        for (i = 0; i < 7 - l; i++) {
            s += " ";
        }
        log.info(s);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(FeedData.name).append(":")
                .append(s);
        back = stringBuffer.length();
        randomAccessFile.writeBytes(stringBuffer.toString());
        randomAccessFile.writeBytes(System.getProperty("line.separator"));
    }

    public long readID() {
        // function to open and read unique id from text file
        long id = 0;
        Scanner s = null;
        try {
            s = new Scanner(new File("toIndex/settings/uniqueId.txt"));
            id = s.nextLong();

        } catch (FileNotFoundException e) {
            System.out.println("Can't find uniqueId.txt , It must contain starting point of doc Id as integer in ./toIndex/settings filez");
        } finally {
            s.close();
        }
        return id;
    }

    public void writeID(long id) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("toIndex/settings/uniqueId.txt"), "utf-8"));
            writer.write("" + id);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }

    public static int readSkip() throws IOException {
        try {
            randomAccessFile = new RandomAccessFile("toIndex/settings/" + FeedData.indexType + ".txt", "rw");
        } catch (IOException e) {
            System.out.println("Can't find one of the files\nUsage MakeCorpus <sentence templates> <names> <output File>");
        }
        String value = null;
        long size = randomAccessFile.length();
        if (size == 0)
            addData(size);
        //Finding position of File Pointer
        long positionOfFilePointer = randomAccessFile.getFilePointer();
        value = searchData();
        if (value == null)
            return -2;
        else
            return Integer.parseInt(value);
    }
    public static String searchData() throws IOException {
        //Setting file pointer to start of file
        randomAccessFile.seek(0);
        System.out.println("in search data ");
        String skip=null;
        String data = randomAccessFile.readLine();
        while (data != null ){
            String[] recordToBeSearched = data.split(":");
            String name = recordToBeSearched[0];
            if(name != null && name.equals(FeedData.name)){
                skip=recordToBeSearched[1].trim();
                back = recordToBeSearched[0].length()+recordToBeSearched[1].length()+1;
                break ;
            }
            data = randomAccessFile.readLine();
        }
        log.info("in search data "+skip);
        return skip;
    }

}
