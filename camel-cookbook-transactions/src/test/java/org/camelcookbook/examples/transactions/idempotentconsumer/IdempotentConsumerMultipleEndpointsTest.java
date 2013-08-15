package org.camelcookbook.examples.transactions.idempotentconsumer;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Tests that demonstrate the behavior of idempotent consumption.
 */
public class IdempotentConsumerMultipleEndpointsTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new IdempotentConsumerMultipleEndpointsRouteBuilder();
    }

    @Test
    public void testReplayOfSameMessageWillNotTriggerCall() throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testErrorAfterBlockWillMeanBlockNotReentered()  throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        // the web service should be invoked once only
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.whenExchangeReceived(1, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                throw new IllegalStateException("Out system is down");
            }
        });
        mockOut.setExpectedMessageCount(2);

        try {
            template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
            fail("No exception thrown");
        } catch (CamelExecutionException cee) {
            assertEquals("Out system is down", cee.getCause().getMessage());
        }
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

}