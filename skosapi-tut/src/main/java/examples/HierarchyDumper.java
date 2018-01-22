package examples;

import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;
import org.semanticweb.skosapibinding.SKOStoOWLConverter;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Simon Jupp<br>
 * Date: Sep 5, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class HierarchyDumper {

    public HierarchyDumper () {

        try {
            SKOSManager manager = new SKOSManager();

            SKOSDataset dataset = manager.loadDatasetFromPhysicalURI(URI.create("file:/Users/simon/ontologies/skos/agrovoc_2007_SKOS/ag_skos_20070219.rdf"));

            //"file:/Users/simon/ontologies/skos/agrovoc_2007_SKOS/ag_skos_20070219.rdf"

            SKOSDataFactory df = manager.getSKOSDataFactory();

            // need to find the roots, i.e concepts with no parents;

            Set<SKOSConcept> roots = new HashSet<SKOSConcept>();

            SKOStoOWLConverter converter = new SKOStoOWLConverter();
            try {
//                OWLReasoner reasonerFact = new Reasoner(manager.getOWLManger());
//
//                OWLReasoner reasonerPellet = new Reasoner(manager.getOWLManger());
//
//                System.err.println("Loading ontology...");
//                reasonerFact.loadOntologies(Collections.singleton(converter.getAsOWLOntology(dataset)));
//                System.err.println("Loaded!");
//                long t0 = System.currentTimeMillis();
//                reasonerFact.classify();
//                long t1 = System.currentTimeMillis();
//                System.out.println("Time to classify: " + ((t1 - t0) / 1000)  + " s");
//                System.err.println("Classified!");

            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        } catch (SKOSCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public static void main(String[] args) {
        HierarchyDumper dump = new HierarchyDumper();
    }
}
