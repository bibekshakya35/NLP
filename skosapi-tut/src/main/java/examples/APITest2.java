package examples;

import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;
import org.semanticweb.skosapibinding.SKOSReasoner;

import java.net.URI;
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
 * Date: Dec 11, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class APITest2 {

    public static void main(String[] args) {

        try {
            // file:/Users/simon/ontologies/skos/large/cleanrdf/mesh.rdf
            SKOSManager manager = new SKOSManager();
            URI uri = URI.create(args[0]);
            SKOSDataset dataset = manager.loadDatasetFromPhysicalURI(uri);
            SKOSDataset skosSet = manager.loadDatasetFromPhysicalURI(URI.create("file:/Users/simon/skos-core.rdf"));

//            FaCTPlusPlusReasonerFactory reasonerFactory = new FaCTPlusPlusReasonerFactory();
//            PelletReasonerFactory pelletFact = new PelletReasonerFactory();
//            HermiTReasonerFactory hermitFact = new HermiTReasonerFactory();

            OWLReasoner reasoner = null;
            SKOSReasoner skosreasoner = null;

//            reasoner = reasonerFactory.createReasoner(manager.getOWLManger());
//            SKOSReasoner skosreasoner = new SKOSReasoner(manager, reasoner);
//
//            long t0 = System.currentTimeMillis();
//            skosreasoner.loadDataset(dataset);
//            skosreasoner.loadDataset(skosSet);
//            skosreasoner.getSKOSConcepts();
////            skosreasoner.classify();
//            long t1 = System.currentTimeMillis();
//
//            System.out.println("Fact++ Time to classify: " + (t1 - t0)/1000);
//
//            System.gc();


            //////////////////////////////////////////////////////////////////

//            reasoner = pelletFact.createReasoner(manager.getOWLManger());
//            skosreasoner = new SKOSReasoner(manager, reasoner);
//            System.err.println("Started..");
//            long t0 = System.currentTimeMillis();
//            skosreasoner.loadDataset(dataset);
//            skosreasoner.loadDataset(skosSet);
//            skosreasoner.getSKOSConcepts();
////            skosreasoner.classify();
//            long t1 = System.currentTimeMillis();
//
//            System.out.println("Pellet Time to classify: " + (t1 - t0)/1000);
//
//            System.gc();

//            //////////////////////////////////////////////////////////////////

//            reasoner = hermitFact.createReasoner(manager.getOWLManger());
//            skosreasoner = new SKOSReasoner(manager, reasoner);
            System.out.println("loaded");
            long t0 = System.currentTimeMillis();
//            skosreasoner.getSKOSConcepts();
//            skosreasoner.loadDataset(dataset);
//            skosreasoner.loadDataset(skosSet);
            skosreasoner.classify();
            long t1 = System.currentTimeMillis();

            System.out.println("HermiT Time to classify: " + (t1 - t0)/1000);

//            System.gc();

        } catch (SKOSCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
