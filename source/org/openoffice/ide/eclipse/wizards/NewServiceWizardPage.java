/*************************************************************************
 *
 * $RCSfile: NewServiceWizardPage.java,v $
 *
 * $Revision: 1.2 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2005/11/27 17:48:22 $
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
package org.openoffice.ide.eclipse.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.openoffice.ide.eclipse.OOEclipsePlugin;
import org.openoffice.ide.eclipse.gui.rows.BooleanRow;
import org.openoffice.ide.eclipse.gui.rows.TypeRow;
import org.openoffice.ide.eclipse.i18n.I18nConstants;
import org.openoffice.ide.eclipse.i18n.ImagesConstants;
import org.openoffice.ide.eclipse.model.Declaration;
import org.openoffice.ide.eclipse.model.Include;
import org.openoffice.ide.eclipse.model.InterfaceService;
import org.openoffice.ide.eclipse.model.ScopedName;
import org.openoffice.ide.eclipse.model.TreeNode;
import org.openoffice.ide.eclipse.model.UnoidlFile;
import org.openoffice.ide.eclipse.model.UnoidlModel;
import org.openoffice.ide.eclipse.model.UnoidlProject;
import org.openoffice.ide.eclipse.unotypebrowser.InternalUnoType;
import org.openoffice.ide.eclipse.unotypebrowser.UnoTypeProvider;

public class NewServiceWizardPage extends NewScopedElementWizardPage {
	
	public NewServiceWizardPage(String pageName, UnoidlProject project) {
		super(pageName, project);
	}
	
	public NewServiceWizardPage(String pageName, UnoidlProject project, 
								String aRootName, String aServiceName){
		super(pageName, project, aRootName, aServiceName);
	}
	
	public int getProvidedTypes() {
		return UnoTypeProvider.INTERFACE;
	}

	//-------------------------------------------------- Page content managment
	
	private final static String P_IFACE_INHERITANCE = "__iface_inheritance"; 
	private final static String P_PUBLISHED			= "__published";
	
	private TypeRow ifaceInheritanceRow;
	private BooleanRow publishedRow;
	
	public void createSpecificControl(Composite parent) {
		
		ifaceInheritanceRow = new TypeRow(parent, 
				P_IFACE_INHERITANCE, 
				"Inherited interface",
				typesProvider,
				UnoTypeProvider.INTERFACE);
		ifaceInheritanceRow.setValue("com.sun.star.uno.XInterface"); // TODO Configure
		ifaceInheritanceRow.setFieldChangedListener(this);
		
		
		publishedRow = new BooleanRow(parent, P_PUBLISHED,
				OOEclipsePlugin.getTranslationString(
						I18nConstants.PUBLISHED));
		publishedRow.setFieldChangedListener(this);
		publishedRow.setValue(true);
	}
	
	public String getTitle() {
		return OOEclipsePlugin.getTranslationString(
				I18nConstants.NEW_SERVICE_TITLE);
	}
	
	public String getDescription() {
		return "";
	}
	
	protected String getTypeLabel() {
		return OOEclipsePlugin.getTranslationString(I18nConstants.SERVICE_NAME);
	}
	
	protected ImageDescriptor getImageDescriptor() {
		return OOEclipsePlugin.getImageDescriptor(
				ImagesConstants.NEW_SERVICE_IMAGE);
	}
	
	public String getInheritanceName() {
		return ifaceInheritanceRow.getValue();
	}
	
	public boolean isPublished() {
		return publishedRow.getBooleanValue();
	}
	
	public void setInheritanceName(String value, boolean forced) {
		
		if (value.matches("[a-zA-Z0-9_]+(.[a-zA-Z0-9_])*")) {
			ifaceInheritanceRow.setValue(value);
			ifaceInheritanceRow.setEnabled(!forced);	
		}
	}
	
	public void setPublished(boolean value, boolean forced) {
		
		publishedRow.setValue(value);
		publishedRow.setEnabled(!forced);
	}

	public boolean isPageComplete() {
		boolean result = super.isPageComplete(); 
		
		try {
			if (ifaceInheritanceRow.getValue().equals("")) {
				result = false;
			}
		} catch (NullPointerException e) {
			result = false;
		}
		
		return result;
	}
	
	public InterfaceService createService(String packageName, String name,
			String inheritanceName, boolean published){
		
		InterfaceService service = null;
		
		try {
			String path = unoProject.getPath() + unoProject.getSeparator() +
							unoProject.getRootScopedName();
			
			if (!packageName.equals("")) {
				path = path + Declaration.SEPARATOR + packageName;
			}
			
			unoProject.createModules(
					new ScopedName(unoProject.getRootScopedName() + 
							ScopedName.SEPARATOR + packageName),
					null);
			
			TreeNode parent = UnoidlModel.getUnoidlModel().findNode(path);
			
			// Perform only if the parent exists
			if (null != parent) {
				
				// Creates a file and the necessary folders for this new type
				String[] segments = packageName.split("\\.");
				
				IFolder folder = unoProject.getProject().getFolder(
						unoProject.getUnoidlPrefixPath());
				
				for (int i=0; i<segments.length; i++) {
					folder = folder.getFolder(segments[i]);
					if (!folder.exists()) {
						folder.create(true, true, null);
					}
				}
				
				IFile file = folder.getFile(name + ".idl");
				TreeNode fileNode = unoProject.findNode(
						UnoidlFile.computePath(file));
				UnoidlFile unofile = null;
				
				if (null == fileNode){
					unofile = new UnoidlFile(unoProject, file);
				} else {
					unofile = (UnoidlFile)fileNode;
				}
				
				String ifaceInheritanceName = inheritanceName.replace(".", "::");
				
				service = new InterfaceService(parent, 
						name, 
						unofile, 
						new ScopedName(ifaceInheritanceName));
				((InterfaceService)service).setPublished(
						published);
				
				InternalUnoType type = typesProvider.getType(
						inheritanceName);
				boolean isLibrary = false;
				if (null != type) {
					isLibrary = !type.isLocalType();
				}
				
				unofile.addInclude(
						Include.createInclude(ifaceInheritanceName, isLibrary));
				
				parent.addNode(service);
				unoProject.addNode(unofile);
				
				unofile.addDeclaration(service);
				unofile.save();
			}
		} catch (Exception e) {
			OOEclipsePlugin.logError(OOEclipsePlugin.getTranslationString(
					I18nConstants.SERVICE_CREATION_FAILED), e);
		}
		return service;
	}

}