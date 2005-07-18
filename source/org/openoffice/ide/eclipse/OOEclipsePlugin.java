/*************************************************************************
 *
 * $RCSfile: OOEclipsePlugin.java,v $
 *
 * $Revision: 1.1 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2005/07/18 19:35:55 $
 *
 * The Contents of this file are made available subject to the terms of
 * either of the following licenses
 *
 *     - GNU Lesser General Public License Version 2.1
 *     - Sun Industry Standards Source License Version 1.1
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
 *
 * Sun Industry Standards Source License Version 1.1
 * =================================================
 * The contents of this file are subject to the Sun Industry Standards
 * Source License Version 1.1 (the "License"); You may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at http://www.openoffice.org/license.html.
 *
 * Software provided under this License is provided on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING,
 * WITHOUT LIMITATION, WARRANTIES THAT THE SOFTWARE IS FREE OF DEFECTS,
 * MERCHANTABLE, FIT FOR A PARTICULAR PURPOSE, OR NON-INFRINGING.
 * See the License for the specific provisions governing your rights and
 * obligations concerning the Software.
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
package org.openoffice.ide.eclipse;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.openoffice.ide.eclipse.editors.Colors;
import org.openoffice.ide.eclipse.i18n.Translator;
import org.osgi.framework.BundleContext;

/**
 * TODOC
 * 
 * @author cbosdonnat
 *
 */
public class OOEclipsePlugin extends AbstractUIPlugin {

	/**
	 * ooeclipseintegration plugin id
	 */
	public static final String OOECLIPSE_PLUGIN_ID = "org.openoffice.ide.eclipse";
	
	/**
	 * uno nature id
	 */
	public static final String UNO_NATURE_ID = OOECLIPSE_PLUGIN_ID + ".natures.uno";
	
	public static final String SDKNAME_PREFERENCE_KEY    = "sdkname";
	public static final String SDKVERSION_PREFERENCE_KEY = "sdkversion";
	public static final String SDKPATH_PREFERENCE_KEY    = "sdkpath";
	public static final String OOOPATH_PREFERENCE_KEY    = "ooopath";

	// The shared instance.
	private static OOEclipsePlugin plugin;
	
	// An instance of the translator
	private Translator translator;
	
	/**
	 * The constructor.
	 */
	public OOEclipsePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setDefaultPreferences();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static OOEclipsePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the translator. If the translator is null, this method will
	 * create it before returning it.
	 * 
	 * @return the translator
	 */
	public Translator getTranslator(){
		
		// HELP Do not access to the translator directly, even if it is
	    //      supposed to be non-null: it could cause strange errors
		//      such as Bundle errors from eclipse...
		if (null == translator){
			translator = new Translator();
		}
		
		return translator;
	}
	
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.openoffice.ide.eclipse", path);
	}
	
	/**
	 * This method uses internationalization files. They should be placed in the i18n
	 * directory of the plugin. Their name is contitued by <strong>OOEclipsePlugin_
	 * <em>contry</em>.lang</strong> where <em>country</em> corresponds to the two 
	 * letters designing the country or the word default (for the default translation file).
	 * 
	 * The default file UnoPlugin_us.lang should be provided. If no key is found in the 
	 * locale corresponding file, the key is returned instead of the internationalized 
	 * message.
	 * 
	 * @param key Asked entry in the internationalization file.
	 */
	public static String getTranslationString(String key) {
		return getDefault().getTranslator().getString(key);
	}
	
	/**
	 * Method that initialize the default preferences of the plugin
	 */
	public static void setDefaultPreferences() {
		final RGB
			STRING = new RGB(255,0,0),	                     // Ligth red 
			BACKGROUND = new RGB(255,255,255),               // White
			DEFAULT = new RGB(0,0,0),                        // Black
			KEYWORD = new RGB(127,0,85),                     // Prune
			TYPE = new RGB(0,0,128),                         // Dark blue
			COMMENT = new RGB(63,127,95),                    // Grey green
			DOC_COMMENT = new RGB(64,128,255),               // Light blue
			XML_TAG = new RGB(180, 180, 180),                // Light grey
			MODIFIER = new RGB(54, 221, 28),                 // Light green
			PREPROCESSOR_COMMAND = new RGB(128, 128, 128);   // Dark grey 
		
		IPreferenceStore store = getDefault().getPreferenceStore();
		PreferenceConverter.setDefault(store, Colors.C_KEYWORD, KEYWORD);
		PreferenceConverter.setDefault(store, Colors.C_BACKGROUND, BACKGROUND);
		PreferenceConverter.setDefault(store, Colors.C_TEXT, DEFAULT);
		PreferenceConverter.setDefault(store, Colors.C_STRING, STRING);
		PreferenceConverter.setDefault(store, Colors.C_TYPE, TYPE);
		PreferenceConverter.setDefault(store, Colors.C_COMMENT, COMMENT);
		PreferenceConverter.setDefault(store, Colors.C_AUTODOC_COMMENT, DOC_COMMENT);
		PreferenceConverter.setDefault(store, Colors.C_PREPROCESSOR, PREPROCESSOR_COMMAND);
		PreferenceConverter.setDefault(store, Colors.C_XML_TAG, XML_TAG);
		PreferenceConverter.setDefault(store, Colors.C_MODIFIER, MODIFIER);
	}
	
	/**
	 * This static method is provided to easier log errors in the eclipse error view
	 * 
	 * @param message Message to print in the error log view
	 * @param e Exception raised. Could be null.
	 */
	public static void logError(String message, Exception e){
		getDefault().getLog().log(new Status(
				Status.ERROR, 
				getDefault().getBundle().getSymbolicName(),
				Status.ERROR,
				message,
				e));
	}

	/**
	 * This static method is provided to easier log warnings in the eclipse error view
	 * 
	 * @param message Message to print in the warning log view
	 * @param e Exception raised. Could be null.
	 */
	public static void logWarning(String message, Exception e){
		getDefault().getLog().log(new Status(
				Status.WARNING, 
				getDefault().getBundle().getSymbolicName(),
				Status.WARNING,
				message,
				e));
	}
	
}