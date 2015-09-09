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

package org.kurron.example.rest.outbound

import groovy.transform.Canonical
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Transient
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * This is an entity, meaning it can stand on its own, that can be associated to 1 or more children.
 */
@Canonical
@Entity
class Stuff {

    @Id
    @GeneratedValue( generator = 'ID_GENERATOR' )
    Long id

    @NotNull( message = 'Command cannot be null!' )
    @Size( min = 2, max = 255, message = 'Command is required, 255 character maximum')
    String command

    @Transient
    String doNotSaveMe = 'Do not store'

}
