/*************************************************************************
 *
 * $RCSfile: UnoidlEditorPage.java,v $
 *
 * $Revision: 1.3 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2006/08/20 11:55:55 $
 *
 * The Contents of this file are made available subject to the terms of
 * either of the GNU Lesser General Public License Version 2.1
 *
 * Sun Microsystems Inc., October, 2000
 *
 *
 * GNU Lesser General Public License Version 2.1
 * =============================================
 * Copyright 2000 by Sun Microsystems, Inc.
 * 901 San Antonio Road, Palo Alto, CA 94303, USA
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 * 
 * The Initial Developer of the Original Code is: Sun Microsystems, Inc..
 *
 * Copyright: 2002 by Sun Microsystems, Inc.
 *
 * All Rights Reserved.
 *
 * Contributor(s): Cedric Bosdonnat
 *
 *
 ************************************************************************/
package org.openoffice.ide.eclipse.core.preferences;


import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openoffice.ide.eclipse.core.OOEclipsePlugin;
import org.openoffice.ide.eclipse.core.editors.Colors;

/**
 * Preference page to change the UNO-IDL editor colors
 *  
 * @author cbosdonnat
 */
public class UnoidlEditorPage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	/**
	 * Default constructor loading the preferences
	 *
	 */
	public UnoidlEditorPage() {
		super(GRID);
		setPreferenceStore(OOEclipsePlugin.getDefault().getPreferenceStore());
		setDescription(Messages.getString("UnoidlEditorPage.Title")); //$NON-NLS-1$
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	public void createFieldEditors() {
		addField(new ColorFieldEditor(Colors.C_TEXT, 
				 Messages.getString("UnoidlEditorPage.Text"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new ColorFieldEditor(Colors.C_KEYWORD, 
				 Messages.getString("UnoidlEditorPage.Keyword"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new ColorFieldEditor(Colors.C_MODIFIER,
				 Messages.getString("UnoidlEditorPage.Modifier"), //$NON-NLS-1$
				 getFieldEditorParent()));
		addField(new ColorFieldEditor(Colors.C_STRING, 
				 Messages.getString("UnoidlEditorPage.String"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new ColorFieldEditor(Colors.C_TYPE, 
				 Messages.getString("UnoidlEditorPage.Type"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new ColorFieldEditor(Colors.C_COMMENT, 
				 Messages.getString("UnoidlEditorPage.Comment"), getFieldEditorParent())); //$NON-NLS-1$
		addField(new ColorFieldEditor(Colors.C_AUTODOC_COMMENT,
				 Messages.getString("UnoidlEditorPage.AutodocComment"),  //$NON-NLS-1$
				 getFieldEditorParent()));
		addField(new ColorFieldEditor(Colors.C_XML_TAG,
				 Messages.getString("UnoidlEditorPage.XmlTag"), //$NON-NLS-1$
				 getFieldEditorParent()));
		addField(new ColorFieldEditor(Colors.C_PREPROCESSOR, 
				 Messages.getString("UnoidlEditorPage.PreprocessorCommand"),  //$NON-NLS-1$
				 getFieldEditorParent()));
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}