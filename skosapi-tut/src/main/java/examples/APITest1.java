package examples;

import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skos.SKOSStorageException;
import org.semanticweb.skosapibinding.SKOSFormatExt;
import org.semanticweb.skosapibinding.SKOSManager;
import org.semanticweb.skosapibinding.SKOStoOWLConverter;

import java.net.URI;/*
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
public class APITest1 {

    // test the load time and memory consumption.


    public static void main(String[] args) {

//\\        final String GTT = "file:/Users/simon/ontologies/skos/large/meshAll_2008.owl";

        try {
            SKOSManager manager = new SKOSManager();
            
            URI uri = URI.create(args[0]);

            long t0 = System.currentTimeMillis();
            SKOSDataset dataset = manager.loadDatasetFromPhysicalURI(uri);
            long t1 = System.currentTimeMillis();
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            System.gc();

            Runtime r = Runtime.getRuntime();
            long mem = (r.totalMemory() - r.freeMemory()) / 1048576;

            System.out.println("Mem: " + mem);
            System.out.println("Time to load (seconds): " + (t1 -t0)/1000 );

            int count = dataset.getSKOSConcepts().size() + dataset.getSKOSConceptSchemes().size();
            System.out.println("Number of Entities: " + count);

            SKOStoOWLConverter converter = new SKOStoOWLConverter();

            OWLOntology onto = converter.getAsOWLOntology(dataset);

            OWLOntologyManager owlmanager  = manager.getOWLManger();

            RDFXMLOntologyFormat rdfF = (RDFXMLOntologyFormat) owlmanager.getOntologyFormat(onto);
//             System.out.println("Number of triples: " + rdfF.getNumberOfTriplesProcessedDuringLoading());

            manager.save(dataset, SKOSFormatExt.RDFXML, URI.create("file:/Users/simon/ontologies/skos/large/cleanrdf/agrovoc.rdf"));

        } catch (SKOSCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SKOSStorageException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
