/*--

 Copyright (C) 2000-2007 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

package org.jdom2.filter;

import java.io.*;
import org.jdom2.*;

/**
 * A Filter that only matches {@link org.jdom2.Attribute} objects.
 *
 * @author  Rolf Lear
 */
public class AttributeFilter extends AbstractFilter<Attribute> {

	/** The element name */
	private String name;

	/** The element namespace */
	private transient Namespace namespace;

	/**
	 * Select only the Elements.
	 */
	public AttributeFilter() {}

	/**
	 * Select only the Elements with the supplied name in any Namespace.
	 *
	 * @param name   The name of the Element.
	 */
	public AttributeFilter(String name) {
		this.name   = name;
	}

	/**
	 * Select only the Attributes with the supplied Namespace.
	 *
	 * @param namespace The namespace the Attribute lives in.
	 */
	public AttributeFilter(Namespace namespace) {
		this.namespace = namespace;
	}

	/**
	 * Select only the Attributes with the supplied name and Namespace.
	 *
	 * @param name   The name of the Attribute.
	 * @param namespace The namespace the Attribute lives in.
	 */
	public AttributeFilter(String name, Namespace namespace) {
		this.name   = name;
		this.namespace = namespace;
	}

	/**
	 * Check to see if the Content matches a predefined set of rules.
	 *
	 * @param content The Content to verify.
	 * @return <code>true</code> if the objected matched a predfined
	 *           set of rules.
	 */
	@Override
	public Attribute filter(Object content) {
		if (content instanceof Attribute) {
			Attribute att = (Attribute) content;
			if (name == null) {
				if (namespace == null) {
					return att;
				}
				return namespace.equals(att.getNamespace()) ? att : null;
			}
			if (!name.equals(att.getName())) {
				return null;
			}
			if (namespace == null) {
				return att;
			}
			return namespace.equals(att.getNamespace()) ? att : null;
		}
		return null;
	}

	/**
	 * Returns whether the two filters are equivalent (i&#46;e&#46; the
	 * matching names and namespace are equivalent).
	 *
	 * @param  obj                   the object to compare against
	 * @return                     whether the two filters are equal
	 */
	@Override
	public boolean equals(Object obj) {
		// Generated by IntelliJ
		if (this == obj) return true;
		if (!(obj instanceof AttributeFilter)) return false;

		final AttributeFilter filter = (AttributeFilter) obj;

		if (name != null ? !name.equals(filter.name) : filter.name != null) return false;
		if (namespace != null ? !namespace.equals(filter.namespace) : filter.namespace != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (name != null ? name.hashCode() : 0);
		result = 29 * result + (namespace != null ? namespace.hashCode() : 0);
		return result;
	}

	// Support a custom Namespace serialization so no two namespace
	// object instances may exist for the same prefix/uri pair
	private void writeObject(ObjectOutputStream out) throws IOException {

		out.defaultWriteObject();

		// We use writeObject() and not writeUTF() to minimize space
		// This allows for writing pointers to already written strings
		if (namespace != null) {
			out.writeObject(namespace.getPrefix());
			out.writeObject(namespace.getURI());
		}
		else {
			out.writeObject(null);
			out.writeObject(null);
		}
	}

	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException {

		in.defaultReadObject();

		Object prefix = in.readObject();
		Object uri = in.readObject();

		if (prefix != null) {  // else leave namespace null here
			namespace = Namespace.getNamespace((String) prefix, (String) uri);
		}
	}
}
