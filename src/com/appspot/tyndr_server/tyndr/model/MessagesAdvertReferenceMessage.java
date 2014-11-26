/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-11-17 18:43:33 UTC)
 * on 2014-11-26 at 22:20:38 UTC 
 * Modify at your own risk.
 */

package com.appspot.tyndr_server.tyndr.model;

/**
 * Passes an advert's reference number and image upload url to the endpoint. Author: Kristjan
 * Eldjarn Hjorleifsson, keh4@hi.is
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the tyndr. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesAdvertReferenceMessage extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String reference;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getReference() {
    return reference;
  }

  /**
   * @param reference reference or {@code null} for none
   */
  public MessagesAdvertReferenceMessage setReference(java.lang.String reference) {
    this.reference = reference;
    return this;
  }

  @Override
  public MessagesAdvertReferenceMessage set(String fieldName, Object value) {
    return (MessagesAdvertReferenceMessage) super.set(fieldName, value);
  }

  @Override
  public MessagesAdvertReferenceMessage clone() {
    return (MessagesAdvertReferenceMessage) super.clone();
  }

}
