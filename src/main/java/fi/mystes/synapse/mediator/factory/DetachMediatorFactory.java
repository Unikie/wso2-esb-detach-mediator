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

import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.apache.synapse.config.xml.SynapseXPathFactory;
import org.jaxen.JaxenException;

import fi.mystes.synapse.mediator.DetachMediator;
import fi.mystes.synapse.mediator.config.DetachMediatorConfigConstants;

/**
 * Factory for {@link DetachMediator} instances.
 * 
 * <pre>
 * &lt;detach xpath="expression" /&gt;
 * </pre>
 */
public class DetachMediatorFactory extends AbstractMediatorFactory {

    /**
     * The QName of detach mediator element in the XML config
     * 
     * @return QName of detach mediator
     */
    @Override
    public QName getTagQName() {
        return DetachMediatorConfigConstants.ROOT_TAG;
    }

    /**
     * Specific mediator factory implementation to build the
     * org.apache.synapse.Mediator by the given XML configuration
     * 
     * @param OMElement
     *            element configuration element describing the properties of the
     *            mediator
     * @param properties
     *            bag of properties to pass in any information to the factory
     * 
     * @return built detach mediator
     */
    @Override
    protected Mediator createSpecificMediator(OMElement element, Properties properties) {
        DetachMediator mediator = new DetachMediator();

        try {
            mediator.setXpath(SynapseXPathFactory.getSynapseXPath(element, ATT_XPATH));
        } catch (JaxenException e) {
            handleException("Invalid source XPath : " + element.getAttributeValue(ATT_XPATH));
        }

        return mediator;
    }
}
