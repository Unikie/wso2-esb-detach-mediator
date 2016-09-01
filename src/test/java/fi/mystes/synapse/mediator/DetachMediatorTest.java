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
package fi.mystes.synapse.mediator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.OMNamespaceImpl;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.llom.SOAPEnvelopeImpl;
import org.apache.axiom.soap.impl.llom.soap11.SOAP11BodyImpl;
import org.apache.axiom.soap.impl.llom.soap11.SOAP11Factory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.xml.SynapseXPathFactory;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fi.mystes.synapse.mediator.config.DetachMediatorConfigConstants;

public class DetachMediatorTest {
    private DetachMediator mediator;

    @Mock
    private MessageContext context;

    @Mock
    private SynapseXPath synapseXPathMock;

    @Mock
    private Object emptyEvaluationResult;

    private OMFactory omFactory = OMAbstractFactory.getOMFactory();

    private OMElement payloadElement;

    private OMElement detachElement;

    private SOAPFactory soapFactory;

    private SOAPEnvelope envelope;

    @Before
    public void setUp() throws JaxenException {
        MockitoAnnotations.initMocks(this);
        mediator = new DetachMediator();
        payloadElement = omFactory.createOMElement(new QName(DetachMediatorConfigConstants.NAMESPACE_STRING, "root"));

        detachElement = omFactory.createOMElement(DetachMediatorConfigConstants.ROOT_TAG);
        detachElement.addAttribute(omFactory.createOMAttribute("xpath", null, "//detachable"));
        mediator.setXpath(SynapseXPathFactory.getSynapseXPath(detachElement, new QName("xpath")));

        OMElement detachable = omFactory.createOMElement(new QName("detachable"));
        detachable.addAttribute(omFactory.createOMAttribute("removable", null, "attribute"));
        payloadElement.addChild(detachable);

        soapFactory = new SOAP11Factory();
        envelope = new SOAPEnvelopeImpl(new OMNamespaceImpl("http://schemas.xmlsoap.org/soap/envelope/", "env"),
                soapFactory);

        SOAP11BodyImpl body = new SOAP11BodyImpl(envelope, soapFactory);

        body.addChild(payloadElement);

        when(context.getEnvelope()).thenReturn(envelope);

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWithNullMessageContext() {
        context = null;
        assertTrue("Should not throw NullPointerException neither return false", mediator.mediate(context));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionDueToNullXPathEvaluationResult() throws JaxenException {
        mediator.setXpath(synapseXPathMock);
        when(synapseXPathMock.evaluate(context)).thenReturn(null);
        mediator.mediate(context);
    }

    @Test(expected = SynapseException.class)
    public void shouldThrowSynapseExceptionDueToNoListXPathEvaluationResult() throws JaxenException {
        mediator.setXpath(synapseXPathMock);
        when(synapseXPathMock.evaluate(context)).thenReturn(emptyEvaluationResult);
        mediator.mediate(context);
    }

    @Test
    public void shouldDetachElementFromPayload() {
        mediator.mediate(context);

        assertTrue("\"detachable\" element should be removed from \"root\" element",
                payloadElement.getFirstElement() == null);
    }

    @Test
    public void shouldDetachAttributeFromElementInPayload() throws JaxenException {
        detachElement.addAttribute(omFactory.createOMAttribute("xpath", null, "//detachable/@removable"));
        mediator.setXpath(SynapseXPathFactory.getSynapseXPath(detachElement, new QName("xpath")));
        mediator.mediate(context);

        assertTrue("\"removable\" attribute should be removed from \"detachable\" element",
                payloadElement.getFirstElement().getAttribute(new QName("removable")) == null);
    }

    @Test
    public void shouldDetachManyElementsFromPayload() throws JaxenException {
        // add three children to be detached
        payloadElement.getFirstElement().addChild(omFactory.createOMElement(new QName("detachableChildren")));
        payloadElement.getFirstElement().addChild(omFactory.createOMElement(new QName("detachableChildren")));
        payloadElement.getFirstElement().addChild(omFactory.createOMElement(new QName("detachableChildren")));

        detachElement.addAttribute(omFactory.createOMAttribute("xpath", null, "//detachable/detachableChildren"));
        mediator.setXpath(SynapseXPathFactory.getSynapseXPath(detachElement, new QName("xpath")));
        mediator.mediate(context);

        assertTrue("\"detachableChildren\" elements should be removed from \"detachable\" element",
            payloadElement.getFirstElement().getFirstChildWithName(new QName("detachableChildren")) == null);
    }
}
