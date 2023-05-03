/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.epsilon.modiff.test.emfcompare;

import java.io.IOException;

public abstract class AbstractInputData {

	protected String getAbsolutePath(String string) throws IOException {
		return getClass().getResource(string).getPath();
	}
}
