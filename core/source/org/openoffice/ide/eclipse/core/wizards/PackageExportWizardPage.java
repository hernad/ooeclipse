/*************************************************************************
 *
 * $RCSfile: PackageExportWizardPage.java,v $
 *
 * $Revision: 1.5 $
 *
 * last change: $Author: cedricbosdo $ $Date: 2007/02/04 18:17:04 $
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
package org.openoffice.ide.eclipse.core.wizards;

import java.io.File;

import org.openoffice.ide.eclipse.core.OOEclipsePlugin;
import org.openoffice.ide.eclipse.core.gui.UnoProjectProvider;
import org.openoffice.ide.eclipse.core.gui.rows.ChoiceRow;
import org.openoffice.ide.eclipse.core.gui.rows.DialogRow;
import org.openoffice.ide.eclipse.core.gui.rows.FieldEvent;
import org.openoffice.ide.eclipse.core.gui.rows.FileRow;
import org.openoffice.ide.eclipse.core.gui.rows.IFieldChangedListener;
import org.openoffice.ide.eclipse.core.i18n.ImagesConstants;
import org.openoffice.ide.eclipse.core.model.IUnoidlProject;
import org.openoffice.ide.eclipse.core.model.ProjectsManager;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * Page of the OpenOffice.org package export wizard.
 * 
 * @author cedricbosdo
 *
 */
public class PackageExportWizardPage extends WizardPage {

	private static final String DESTDIR = "__destdir"; //$NON-NLS-1$
	private static final String OOVERSION = "__oooversion"; //$NON-NLS-1$
	public static final String LAST_EXPORT_DIR = "__last_export_dir"; //$NON-NLS-1$

	public PackageExportWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		setImageDescriptor(OOEclipsePlugin.getImageDescriptor(ImagesConstants.PACKAGE_EXPORT_WIZ));
		setDescription(Messages.getString("PackageExportWizardPage.Description")); //$NON-NLS-1$
		setTitle(Messages.getString("PackageExportWizardPage.Title")); //$NON-NLS-1$
		
		if (selection.getFirstElement() instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable)selection.getFirstElement();
			IResource res = (IResource)adaptable.getAdapter(IResource.class);
			
			try {
				String prjName = res.getProject().getName();
				IUnoidlProject project = ProjectsManager.getProject(prjName);
				if (project != null) {
					mProject = project;
				}
			} catch (Exception e) {
			}
		}
	}
	
	private DialogRow mProjectRow;
	private IUnoidlProject mProject;
	private FileRow mOutputdirRow;
	private ChoiceRow mOOoVersion;
	
	IUnoidlProject getProject() {
		return mProject;
	}
	
	File getOutputPath() {
		IPreferenceStore store = OOEclipsePlugin.getDefault().getPreferenceStore();
		store.setValue(LAST_EXPORT_DIR, mOutputdirRow.getValue());
		return new File(mOutputdirRow.getValue());
	}
	
	String getPackageExtension(){
		return mOOoVersion.getValue();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		
		Composite body = new Composite(parent, SWT.NONE);
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		body.setLayout(new GridLayout(3, false));
		
		// createProjectField(body);
		mProjectRow = new DialogRow(body, "", Messages.getString("PackageExportWizardPage.PackageLabel")) { //$NON-NLS-1$ //$NON-NLS-2$
			@Override
			public String doOpenDialog() {
				String result = getValue();
				
				// Open the project selection dialog
				ILabelProvider labelProvider = new UnoProjectProvider();
				ElementListSelectionDialog dialog = new ElementListSelectionDialog(
						getShell(), labelProvider);
				dialog.setTitle(Messages.getString("PackageExportWizardPage.ChooserTitle")); //$NON-NLS-1$
				dialog.setMessage(Messages.getString("PackageExportWizardPage.ChooserDescription")); //$NON-NLS-1$
				dialog.setElements(ProjectsManager.getProjects());

				if (dialog.open() == Window.OK) {
					mProject = (IUnoidlProject)dialog.getFirstResult();
					result = mProject.getName();
				}
				checkPageComplete();
				return result;
			}
		};
		if (mProject != null) {
			mProjectRow.setValue(mProject.getName());
		}
		mProjectRow.setFieldChangedListener(new IFieldChangedListener() {
			public void fieldChanged(FieldEvent e) {
				IUnoidlProject project = ProjectsManager.getProject(e.getValue());
				mProject = project;
					
				checkPageComplete();
			}
		});
		
		mOutputdirRow = new FileRow(body, DESTDIR, Messages.getString("PackageExportWizardPage.DestinationLabel"), true); //$NON-NLS-1$
		mOutputdirRow.setTooltip(Messages.getString("PackageExportWizardPage.DestinationTooltip")); //$NON-NLS-1$
		
		// Set the user dir or the latest dir if any
		IPreferenceStore store = OOEclipsePlugin.getDefault().getPreferenceStore();
		String lastDir = store.getString(LAST_EXPORT_DIR);
		if ("".equals(lastDir)) { //$NON-NLS-1$
			// Get the user home
			lastDir = System.getProperty("user.home"); //$NON-NLS-1$
		}
		mOutputdirRow.setValue(lastDir);
		
		mOutputdirRow.setFieldChangedListener(new IFieldChangedListener() {
			public void fieldChanged(FieldEvent e) {
				checkPageComplete();
			}
		});
		
		mOOoVersion = new ChoiceRow(body, OOVERSION, Messages.getString("PackageExportWizardPage.OOoVersionLabel")); //$NON-NLS-1$
		mOOoVersion.setTooltip(Messages.getString("PackageExportWizardPage.OOoVersionTooltip")); //$NON-NLS-1$
		// TODO Check for the exact versions
		mOOoVersion.add("1.x", "zip"); //$NON-NLS-1$ //$NON-NLS-2$
		mOOoVersion.add("2.0", "uno.pkg"); //$NON-NLS-1$ //$NON-NLS-2$
		mOOoVersion.add("2.0.4", "oxt"); //$NON-NLS-1$ //$NON-NLS-2$
		mOOoVersion.select(1);
		
		setControl(body);
	}
	
	/**
	 * Checks if the page is valid or not
	 */
	private void checkPageComplete() {
		if (mProject == null) {
			setErrorMessage(Messages.getString("PackageExportWizardPage.SelectProjectError")); //$NON-NLS-1$
		}
		
		String outPath = mOutputdirRow.getValue();
		boolean outputExists = false;
		try {
			outputExists = new File(outPath).exists();
			if (!outputExists) {
				setErrorMessage(Messages.getString("PackageExportWizardPage.WrongDestinationError")); //$NON-NLS-1$
			}
		} catch (Exception e) {
			outputExists = false;
			setErrorMessage(Messages.getString("PackageExportWizardPage.WrongDestinationError")); //$NON-NLS-1$
		}
		
		if (mProject != null && outputExists) {
			setPageComplete(true);
			setErrorMessage(null);
		} else {
			setPageComplete(false);
		}
	}
}
