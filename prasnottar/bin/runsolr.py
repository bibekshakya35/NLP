#!/usr/bin/python3
import os
os.system('./solr start -s /home/bibek/bs-workspace/NLP/prasnottar/server/solr -m "2G" -f -a " $DEBUG_OPTS -Dmodel.dir=../opennlp-models -Dsolr.data.dir=solr/WiseOwl/data -Dwordnet.dir=../WordNet-3.0/dict/"');

