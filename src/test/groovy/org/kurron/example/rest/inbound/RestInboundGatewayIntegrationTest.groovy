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

import groovy.json.JsonBuilder
import java.util.concurrent.Future
import org.kurron.example.rest.Application
import org.kurron.traits.GenerationAbility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.AsyncRestOperations
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

/**
 * Integration test for the RestInboundGateway object.
 **/
@ContextConfiguration( loader = SpringApplicationContextLoader, classes = [Application] )
@WebIntegrationTest( randomPort = true )
class RestInboundGatewayIntegrationTest extends Specification implements GenerationAbility {

    @Autowired
    AsyncRestOperations theTemplate

    @Value( '${local.server.port}' )
    int port

    def possibleCommands = ['fast', 'normal', 'slow', 'dead']
    def expectations = ['command': randomElement( possibleCommands )]

    def 'exercise happy path'() {

        given: 'a valid environment'
        assert theTemplate
        assert port

        def builder = new JsonBuilder( expectations )
        def command = builder.toPrettyString()

        and: 'the POST request is made'
        def uri = UriComponentsBuilder.newInstance().scheme( 'http' ).host( 'localhost' ).port( port ).path( '/' ).build().toUri()
        def headers = new HttpHeaders()
        headers.setContentType( MediaType.APPLICATION_JSON )
        headers.set( 'X-Correlation-Id', randomHexString() )
        HttpEntity<String> request = new HttpEntity<>( command, headers )
        Future<ResponseEntity<Void>> future = theTemplate.postForEntity( uri, request, Void )

        when: 'the answer comes back'
        def response = future.get()

        then: 'the endpoint returns with 204'
        response.statusCode == HttpStatus.NO_CONTENT
    }
}
