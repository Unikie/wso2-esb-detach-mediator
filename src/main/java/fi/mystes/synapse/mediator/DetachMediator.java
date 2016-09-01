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

import java.text.MessageFormat;
import java.util.List;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

/**
 * Custom mediator to detach part(s) of payload with given XPath expression.
 * 
 * <detach xpath="expression"/>
 */
public class DetachMediator extends AbstractMediator {

    /**
     * XPath evaluator
     */
    private SynapseXPath xpath;

    /**
     * Invokes the mediator passing the current message for mediation. Each
     * mediator performs its mediation action, and returns true if mediation
     * should continue, or false if further mediation should be aborted.
     *
     * @param context
     *            Current message context for mediation
     * @return true if further mediation should continue, otherwise false
     */
    @Override
    public boolean mediate(MessageContext context) {
        try {
            Object evaluationResult = xpath.evaluate(context);

            validateEvaluationResult(context, evaluationResult);

            detachResultFromPayload(evaluationResult);
        } catch (JaxenException e) {
            handleException(MessageFormat.format("Encountered an exception while evaluating the XPath expression: {0}",
                    xpath.toString()), e, context);
        }

        return true;
    }

    /**
     * Getter for XPath.
     * 
     * @return XPath for result to be detached from payload
     */
    public SynapseXPath getXpath() {
        return xpath;
    }

    /**
     * Setter for XPath.
     * 
     * @param xpath
     *            XPath to be set for part(s) to be detached from payload
     */
    public void setXpath(SynapseXPath xpath) {
        this.xpath = xpath;
    }

    /**
     * Helper method to validate given XPath evaluation result.
     * 
     * @param context
     *            Contains payload
     * @param evaluationResult
     *            Evaluation result to be validated
     */
    private void validateEvaluationResult(MessageContext context, Object evaluationResult) {
        if (!(evaluationResult instanceof List)) {
            handleException(MessageFormat.format(
                    "The XPath expression {0} did not yield a node (or list thereof). The return type was {1}.",
                    xpath.toString(), evaluationResult.getClass().getCanonicalName()), context);
        }
    }

    /**
     * Helper method to detach given evaluated result from payload.
     * 
     * @param evaluationResult
     *            Result to be detached
     */
    private void detachResultFromPayload(Object evaluationResult) {
        @SuppressWarnings("unchecked")
        List<Object> results = (List<Object>) evaluationResult;
        for (Object result : results) {
            if (result instanceof OMNode) {
                ((OMNode) result).detach();
            } else if (result instanceof OMAttribute) {
                OMAttribute resultAttribute = (OMAttribute) result;
                OMElement attributeOwner = resultAttribute.getOwner();
                attributeOwner.removeAttribute(attributeOwner.getAttribute(resultAttribute.getQName()));
            }
        }
    }
}
