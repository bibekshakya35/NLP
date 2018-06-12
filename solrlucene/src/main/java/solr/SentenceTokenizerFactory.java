/*
 * Copyright 2008-2011 Grant Ingersoll, Thomas Morton and Drew Farris
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * -------------------
 * To purchase or learn more about Taming Text, by Grant Ingersoll, Thomas Morton and Drew Farris, visit
 * http://www.manning.com/ingersoll
 */

package solr;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import utils.SentenceDetectorFactory;

import java.io.IOException;
import java.util.Map;

public class SentenceTokenizerFactory extends TokenizerFactory {

  SentenceDetectorFactory sentenceDetectorFactory;

  protected SentenceTokenizerFactory(Map<String, String> args) {
    super(args);

    try {
      sentenceDetectorFactory = new SentenceDetectorFactory(args);
    }
    catch (IOException e) {
      throw (RuntimeException) new RuntimeException().initCause(e);
    }
  }

  @Override
  public Tokenizer create(AttributeFactory factory) {
    return new SentenceTokenizer(factory, sentenceDetectorFactory.getSentenceDetector());
  }
}