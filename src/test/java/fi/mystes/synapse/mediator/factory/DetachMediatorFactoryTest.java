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
package fi.mystes.synapse.mediator.factory;

import static org.junit.Assert.assertTrue;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.junit.Test;

import fi.mystes.synapse.mediator.DetachMediator;
import fi.mystes.synapse.mediator.config.DetachMediatorConfigConstants;

public class DetachMediatorFactoryTest {

    @Test(expected = SynapseException.class)
    public void shouldThrowSynapseExceptionDueToMissingXPathAttributeFromElement() {
        DetachMediatorFactory dmFactory = new DetachMediatorFactory();
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        dmFactory.createMediator(omFactory.createOMElement(DetachMediatorConfigConstants.ROOT_TAG), null);
    }

    @Test
    public void shouldCreateDetachMediator() {
        DetachMediatorFactory dmFactory = new DetachMediatorFactory();
        OMFactory omFactory = OMAbstractFactory.getOMFactory();

        OMElement detachElement = omFactory.createOMElement(DetachMediatorConfigConstants.ROOT_TAG);
        detachElement.addAttribute(omFactory.createOMAttribute("xpath", null, "//detachable"));
        dmFactory.createMediator(detachElement, null);

        Mediator mediator = dmFactory.createMediator(detachElement, null);
        assertTrue("Created mediator should be instance of DetachMediator", mediator instanceof DetachMediator);
    }
}
