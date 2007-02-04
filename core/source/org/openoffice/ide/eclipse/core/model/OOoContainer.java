/*************************************************************************
 *
 * $RCSfile: OOoContainer.java,v $
 *
 * $Revision: 1.6 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2007/02/04 18:17:03 $
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
package org.openoffice.ide.eclipse.core.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openoffice.ide.eclipse.core.PluginLogger;
import org.openoffice.ide.eclipse.core.internal.helpers.PropertiesManager;
import org.openoffice.ide.eclipse.core.preferences.IConfigListener;
import org.openoffice.ide.eclipse.core.preferences.IOOo;
import org.openoffice.ide.eclipse.core.preferences.InvalidConfigException;

/**
 * Singleton object containing the OOo configurations.
 * 
 * @author cbosdonnat
 *
 */
public class OOoContainer { 
	
	private static OOoContainer sInstance = new OOoContainer();
	
	/**
	 * Vector of the config container listeners
	 */
	private Vector<IConfigListener> mListeners;

	/**
	 * HashMap containing the ooo lines referenced by their path
	 */
	private HashMap<String, IOOo> mElements;
	
	
	/* Methods to manage the listeners */
	
	/**
	 * Add a config listener to the container
	 * 
	 *  @param listener config listener to add 
	 */
	public static void addListener(IConfigListener listener){
		if (null != listener){
			sInstance.mListeners.add(listener);
		}
	}
	
	/**
	 * Removes a config listener from the container
	 * 
	 * @param listener config listener to remove
	 */
	public static void removeListener(IConfigListener listener){
		if (null != listener){
			sInstance.mListeners.remove(listener);
		}
	}
	
	/* Methods to manage the ooos */
	
	/**
	 * Returns the ooos elements in an array
	 */
	public static Object[] toArray(){
		Vector vElements = toVector();
		Object[] elements = vElements.toArray();
		
		vElements.clear();
		return elements;
	}
	
	/**
	 * Add the OOo given in parameter to the list of the others. Do not use 
	 * directly the private field to handle OOos
	 * 
	 * @param ooo OOo to add
	 */
	public static void addOOo(IOOo ooo){
		
		/** 
		 * If there already is a OOo with such an identifier, replace the 
		 * values, not the object to keep the references on it
		 */ 
		
		if (null != ooo){
			if (!sInstance.mElements.containsKey(ooo.getName())){
				sInstance.mElements.put(ooo.getName(), ooo);
				sInstance.fireOOoAdded(ooo);
			} else {
				IOOo oooref = sInstance.mElements.get(ooo.getName());
				updateOOo(oooref.getName(), ooo);
			}
		}
	}
	
	private void fireOOoAdded(IOOo ooo) {
		for (int i=0, length=mListeners.size(); i<length; i++){
			IConfigListener listeneri = mListeners.get(i);
			listeneri.ConfigAdded(ooo);
		}
	}

	/**
	 * remove the given OOo from the list. Do not use directly the private 
	 * field to handle OOos
	 *  
	 * @param ooo OOo to remove
	 */
	public static void delOOo(IOOo ooo){
		if (null != ooo){
			if (sInstance.mElements.containsKey(ooo.getName())){
				sInstance.mElements.remove(ooo.getName());
				sInstance.fireOOoRemoved(ooo);
			}
		}
	}
	
	/**
	 * Removes all the OOo contained
	 *
	 */
	public static void clear(){
		sInstance.mElements.clear();
		sInstance.fireOOoRemoved(null);
	}
	
	/**
	 * Returns a vector containing the unique identifiers of the contained OOos
	 * 
	 * @return names of the contained OOos
	 */
	public static Vector<String> getOOoKeys(){
		Set<String> paths = sInstance.mElements.keySet();
		return new Vector<String>(paths);
	}
	
	/**
	 * Checks whether the corresponding OOo name already exists
	 * 
	 * @param name the OOo Name to check
	 * @return <code>true</code> if the name is already present, 
	 * 		<code>false</code> otherwise.
	 */
	public static boolean containsName(String name) {
		return sInstance.mElements.containsKey(name);
	}
	
	/**
	 * Computes a unique name from the given one.
	 * 
	 * @param name the name to render unique
	 * @return the unique name
	 */
	public static String getUniqueName(String name) {
		
		String newName = name;
		if (containsName(newName)) {
			Matcher m = Pattern.compile("(.*)#([0-9]+)$").matcher(newName); //$NON-NLS-1$
			
			// initialise as if the name contains no #i at its end
			int number = 0;
			String nameRoot = new String(newName);
			
			if (m.matches()) {
				number = Integer.parseInt(m.group(2));
				nameRoot = m.group(1);
			}
			
			// Check for the last number
			do {
				number += 1;
				newName = nameRoot + " #" + number; //$NON-NLS-1$
			} while (containsName(newName));
		}
		return newName;
	}
	
	private void fireOOoRemoved(IOOo ooo) {
		for (int i=0, length=mListeners.size(); i<length; i++){
			IConfigListener listeneri = mListeners.get(i);
			listeneri.ConfigRemoved(ooo);
		}
	}
	
	/**
	 * update the ith OOo from the list with the given OOo.
	 * 
	 * @param oookey position of the ooo to update
	 * @param ooo new value for the OOo
	 */
	public static void updateOOo(String oookey, IOOo ooo){
		if (sInstance.mElements.containsKey(oookey) && null != ooo){
			
			IOOo oooref = sInstance.mElements.get(oookey);
			
			// update the attributes
			try {
				oooref.setHome(ooo.getHome());
			} catch (InvalidConfigException e) {
				PluginLogger.error(e.getLocalizedMessage(), e);
			}
			
			// Reassign the element in the hashmap
			sInstance.mElements.put(oookey, oooref);
			sInstance.fireOOoUpdated(ooo);
		}
	}
	
	private void fireOOoUpdated(IOOo ooo) {
		for (int i=0, length=mListeners.size(); i<length; i++){
			IConfigListener listeneri = mListeners.get(i);
			listeneri.ConfigUpdated(ooo);
		}
	}
	
	/**
	 * Returns the ooo that corresponds to the given ooo name and buildid.
	 * 
	 * @param oookey unique identifier of the wanted ooo
	 * @return OOo which name equals the one provided
	 */
	public static IOOo getOOo(String oookey){
		IOOo ooo = null;
		
		if (sInstance.mElements.containsKey(oookey)){
			ooo = sInstance.mElements.get(oookey);
		} 
		return ooo;
	}
	
	/**
	 * Returns the number of OOo in the list
	 * 
	 * @return number of OOo in the list
	 */
	public static int getOOoCount(){
		return sInstance.mElements.size();
	}
		
	/**
	 * Dispose the vector used
	 *
	 */
	public static void dispose() {
		sInstance.mListeners.clear();
		sInstance.mElements.clear();
	}
	
	/**
	 * Loads the OpenOffice.org already configured instances from the 
	 * preferences.
	 */
	public static void load(){
		
		IOOo[] ooos = PropertiesManager.loadOOos();
		for (int i=0; i < ooos.length; i++) {
			addOOo(ooos[i]);
		}
	}
	
	/**
	 * Saves the OpenOffice.org already configured instances to the 
	 * preferences.
	 */
	public static void saveOOos(){
		
		// Saving the new OOos 
		Vector vElements = toVector();
		IOOo[] ooos = new IOOo[getOOoCount()];
		
		for (int i=0, length=getOOoCount(); i<length; i++){
			ooos[i] = (IOOo)vElements.get(i);
		}
		
		// clean vector
		vElements.clear();
		
		PropertiesManager.saveOOos(ooos);
	}
	
	public static OOoContainer getInstance() {
		if (sInstance == null) {
			sInstance = new OOoContainer();
		}
		return sInstance;
	}
	
	/**
	 * The SDK Container should not be created by another object
	 */
	private OOoContainer(){
		
		// Initialize the members
		mElements = new HashMap<String, IOOo>();
		mListeners = new Vector<IConfigListener>();
	}
	
	/**
	 * Returns a unordered vector with the hashmap of the elements
	 *  
	 * @return vector where the elements order isn't guaranteed
	 */
	private static Vector<IOOo> toVector(){
		Vector<IOOo> result = new Vector<IOOo>();
		Set<Entry<String, IOOo>> entries = sInstance.mElements.entrySet();
		Iterator<Entry<String, IOOo>> iter = entries.iterator();
		
		while (iter.hasNext()){
			IOOo value = iter.next().getValue();
			result.add(value);
		}
		return result;
	}
}
