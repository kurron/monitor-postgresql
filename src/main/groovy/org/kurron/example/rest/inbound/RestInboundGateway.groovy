/*
 * Copyright (c) 2015. Ronald D. Kurr kurr@jvmguy.com
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
package org.kurron.example.rest.inbound

import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.web.bind.annotation.RequestMethod.POST
import groovy.json.JsonSlurper
import org.kurron.example.rest.ApplicationProperties
import org.kurron.example.rest.feedback.ExampleFeedbackContext
import org.kurron.example.rest.outbound.Stuff
import org.kurron.example.rest.outbound.StuffRepository
import org.kurron.feedback.AbstractFeedbackAware
import org.kurron.stereotype.InboundRestGateway
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.metrics.CounterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Handles inbound REST requests.
 */
@InboundRestGateway
@RequestMapping( value = '/' )
class RestInboundGateway extends AbstractFeedbackAware {

    /**
     * Provides currently active property values.
     */
    private final ApplicationProperties configuration

    /**
     * Used to track counts.
     */
    private final CounterService counterService

    /**
     * Handles RabbitMQ interactions.
     **/
    private final RabbitOperations template

    private final StuffRepository theRepository

    @Autowired
    RestInboundGateway( final ApplicationProperties aConfiguration,
                        final CounterService aCounterService,
                        final StuffRepository aRepository,
                        final RabbitOperations aTemplate ) {
        configuration = aConfiguration
        counterService = aCounterService
        template = aTemplate
        theRepository = aRepository
    }

    @RequestMapping( method = POST, consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE] )
    ResponseEntity<Void> post( @RequestBody final String request ) {
        counterService.increment( 'gateway.post' )
        def parsed = new JsonSlurper().parseText( request ) as Map
        def command = parsed['command'] as String
        long delay
        switch( command ) {
            case 'normal': // introduce small latency
                delay = 1000 * 2
                break
            case 'slow': // introduce large latency
                delay = 1000 * 10
                break
            default: // go as fast as possible
                delay = 0
        }
        Thread.sleep( delay )

        def saved = theRepository.save( new Stuff( command: command ) )
        feedbackProvider.sendFeedback( ExampleFeedbackContext.DATA_STORED, saved.id )

        def message = newMessage( command )
        template.send( message )

        new ResponseEntity<Void>( HttpStatus.NO_CONTENT )
    }

    private static MessageProperties newProperties() {
        MessagePropertiesBuilder.newInstance().setAppId( 'monitor-mysql' )
                                              .setContentType( 'application/json' )
                                              .setMessageId( UUID.randomUUID().toString() )
                                              .setDeliveryMode( MessageDeliveryMode.NON_PERSISTENT )
                                              .setTimestamp( Calendar.instance.time )
                                              .build()
    }

    private static Message newMessage( String command ) {
        def properties = newProperties()
        MessageBuilder.withBody( command.getBytes( UTF_8  ) )
                      .andProperties( properties )
                      .build()
    }
}
