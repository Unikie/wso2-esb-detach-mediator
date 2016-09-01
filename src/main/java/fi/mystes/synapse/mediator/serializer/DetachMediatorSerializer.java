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

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorSerializer;
import org.apache.synapse.config.xml.SynapseXPathSerializer;

import fi.mystes.synapse.mediator.DetachMediator;
import fi.mystes.synapse.mediator.config.DetachMediatorConfigConstants;

public class DetachMediatorSerializer extends AbstractMediatorSerializer {

    @Override
    public String getMediatorClassName() {
        return DetachMediator.class.getName();
    }

    @Override
    protected OMElement serializeSpecificMediator(Mediator mediator) {
        DetachMediator detachMediator = (DetachMediator) mediator;

        OMElement rootElement = fac.createOMElement(DetachMediatorConfigConstants.ROOT_TAG_NAME, synNS);

        SynapseXPathSerializer.serializeXPath(detachMediator.getXpath(), rootElement, "xpath");

        return rootElement;
    }
}
