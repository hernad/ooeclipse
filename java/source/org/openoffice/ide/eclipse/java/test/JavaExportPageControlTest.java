/*************************************************************************
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright: 2010 by Cédric Bosdonnat
 *
 * All Rights Reserved.
 * 
 ************************************************************************/
package org.openoffice.ide.eclipse.java.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openoffice.ide.eclipse.java.export.JavaExportPageControl;

/**
 * Unit test for the Manifest export page part UI controller.
 * 
 * @author Cedric Bosdonnat
 *
 */
public class JavaExportPageControlTest {

    /**
     * Ensure that the default values are the ones expected.
     */
    @Test
    public void testDefaults( ) {
        JavaExportPageControl tested = new JavaExportPageControl();
        assertFalse( "Save ant script shouldn't be default", tested.getSaveAntScript() );
        assertFalse( "Save path shouldn't be enabled by default", tested.isSavePathEnabled() );
        assertEquals( JavaExportPageControl.DEFAULT_ANT_FILENAME, tested.getSavePath() );
    }
    
    /**
     * Test checking and unchecking the Save Ant script box.
     */
    @Test
    public void testSetSaveAntScript() {
        JavaExportPageControl tested = new JavaExportPageControl();
        
        tested.setSaveAntScript( true );
        assertTrue( "Save ant script selection not persisting", tested.getSaveAntScript() );
        assertTrue( "Save path should be enabled when save ant script is checked", tested.isSavePathEnabled() );
        
        tested.setSaveAntScript( false );
        assertFalse( "Save ant script selection not persisting", tested.getSaveAntScript() );
        assertFalse( "Save path should be disabled after save ant script is unchecked", tested.isSavePathEnabled() );
    }

}
