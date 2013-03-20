/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	IBM Corporation - initial API and implementation
 * 
 * Source:
 * 	https://github.com/eclipse/eclipse.pde.ui/blob/master/ui/org.eclipse.pde.ui/src/org/eclipse/pde/internal/ui/parts/IFormEntryListener.java
 *******************************************************************************/
package com.dubture.composer.ui.parts;

import org.eclipse.ui.forms.events.IHyperlinkListener;

public interface IFormEntryListener extends IHyperlinkListener {
	/**
	 * The user clicked on the text control and focus was
	 * transfered to it.
	 * @param entry
	 */
	void focusGained(FormEntry entry);
	
	/**
	 * The focus on the control was lost.
	 * @param entry
	 */
	void focusLost(FormEntry entry);
	
	/**
	 * The user changed the text in the text control of the entry.
	 * @param entry
	 */
	void textDirty(FormEntry entry);
	
	/**
	 * The value of the entry has been changed to be the text
	 * in the text control (as a result of 'commit' action).
	 * @param entry
	 */
	void textValueChanged(FormEntry entry);
	
	/**
	 * The user pressed the 'Browse' button for the entry.
	 * @param entry
	 */
	void browseButtonSelected(FormEntry entry);
	
	void selectionChanged(FormEntry entry);
}