/*
 * Copyright (C) Scott Cranton, Jakub Korab, and Christian Posta
 * https://github.com/CamelCookbook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camelcookbook.examples.testing.dataset;

import org.apache.camel.component.dataset.DataSetSupport;

/**
 * Data set used to generate expected messages coming out of the route.
 */
public class ExpectedOutputDataSet extends DataSetSupport {
    @Override
    protected Object createMessageBody(long messageIndex) {
        return "Modified: message " + messageIndex;
    }
}
