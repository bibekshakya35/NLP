package examples;

import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.skos.*;
import org.semanticweb.skosapibinding.SKOSFormatExt;
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
 * Date: Aug 28, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class ReadInferredSKOS {

    public static void main(String[] args) {

        try {
            SKOSManager manager = new SKOSManager();

            SKOSDataset dataSet = manager.loadDatasetFromPhysicalURI(URI.create("file:/Users/simon/ontologies/skos/apitest.owl"));

            OWLReasoner owlReasoner;// = new OWLReasoner(manager.getOWLManger());
            SKOSReasoner reasoner;// = new SKOSReasoner(manager, owlReasoner);

//            reasoner.loadDataset(dataSet);

            manager.save(dataSet, SKOSFormatExt.RDFXML, URI.create("file:/Users/simon/importtest.owl"));
//
//            for (SKOSConcept con : reasoner.getSKOSConcepts()) {
//
//                for (SKOSLiteral literal : con.getSKOSRelatedConstantByProperty(dataSet, manager.getSKOSDataFactory().getSKOSPrefLabelProperty())) {
//                    System.err.println("Concept: " + literal.getLiteral());
//                }
//
//                for (SKOSConcept broaderCon : reasoner.getSKOSNarrowerTransitiveConcepts(con)) {
//                    for (SKOSLiteral literal : broaderCon.getSKOSRelatedConstantByProperty(dataSet, manager.getSKOSDataFactory().getSKOSPrefLabelProperty())) {
//                        System.err.println("\tConcept: " + literal.getLiteral());
//                    }
//                }
//
//            }

        } catch (SKOSCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SKOSStorageException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
