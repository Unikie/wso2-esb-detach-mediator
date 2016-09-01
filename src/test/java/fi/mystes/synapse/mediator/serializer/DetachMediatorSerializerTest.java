/**
 * Copyright 2016 Mystes Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.mystes.synapse.mediator.serializer;

import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.xml.SynapseXPathFactory;
import org.jaxen.JaxenException;
import org.junit.Test;

import fi.mystes.synapse.mediator.DetachMediator;
import fi.mystes.synapse.mediator.config.DetachMediatorConfigConstants;

public class DetachMediatorSerializerTest {

    @Test(expected = SynapseException.class)
    public void shouldThrowNullPointerExceptionDueToMissingSynapseXPathFromMediator() {
        DetachMediatorSerializer dmSerializer = new DetachMediatorSerializer();
        Mediator mediator = new DetachMediator();
        dmSerializer.serializeSpecificMediator(mediator);
        assertTrue("", true);
    }

    @Test
    public void shouldSerializeDetachMediator() throws JaxenException {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMElement detachElement = omFactory.createOMElement(DetachMediatorConfigConstants.ROOT_TAG);
        detachElement.addAttribute(omFactory.createOMAttribute("xpath", null, "//detachable"));

        DetachMediator mediator = new DetachMediator();
        mediator.setXpath(SynapseXPathFactory.getSynapseXPath(detachElement, new QName("xpath")));

        DetachMediatorSerializer dmSerializer = new DetachMediatorSerializer();
        OMElement mediatorElement = dmSerializer.serializeSpecificMediator(mediator);

        assertTrue("Serialized mediator element should be \"" + DetachMediatorConfigConstants.ROOT_TAG_NAME + "\"",
                mediatorElement.getLocalName().equals(DetachMediatorConfigConstants.ROOT_TAG_NAME));
        assertTrue("Mediator element should have attribute \"xpath\" with value \"//detachable\"",
                mediatorElement.getAttributeValue(new QName("xpath")).equals("//detachable"));
    }
}
