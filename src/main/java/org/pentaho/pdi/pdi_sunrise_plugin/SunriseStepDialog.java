/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2016-2016 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.pdi.pdi_sunrise_plugin;

import java.util.Arrays;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.LabelCombo;
import org.pentaho.di.ui.core.widget.LabelText;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class SunriseStepDialog extends BaseStepDialog implements StepDialogInterface {

  private static final Class<?> PKG = SunriseStepMeta.class;
  private SunriseStepMeta input;

  private LabelCombo inputFieldDate, inputFieldLatitude, inputFieldLongitude, inputFieldTimeZone;
  private LabelText outputSunriseAstronomical, outputSunriseCivil, outputSunriseNautical, outputSunriseOfficial;
  private LabelText outputSunsetAstronomical, outputSunsetCivil, outputSunsetNautical, outputSunsetOfficial;

  public SunriseStepDialog( Shell parent, Object baseStepMeta, TransMeta transMeta, String stepname ) {
    super( parent, (BaseStepMeta) baseStepMeta, transMeta, stepname );
    input = (SunriseStepMeta) baseStepMeta;
  }

  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
    props.setLook( shell );
    setShellImage( shell, input );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        input.setChanged();
      }
    };
    changed = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "SunriseStepDialog.Shell.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label( shell, SWT.RIGHT );
    wlStepname.setText( BaseMessages.getString( PKG, "SunriseStepDialog.Stepname.Label" ) );
    props.setLook( wlStepname );
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment( 0, 0 );
    fdlStepname.right = new FormAttachment( middle, -margin );
    fdlStepname.top = new FormAttachment( 0, margin );
    wlStepname.setLayoutData( fdlStepname );
    wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wStepname.setText( stepname );
    props.setLook( wStepname );
    wStepname.addModifyListener( lsMod );
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment( middle, 0 );
    fdStepname.top = new FormAttachment( 0, margin );
    fdStepname.right = new FormAttachment( 100, 0 );
    wStepname.setLayoutData( fdStepname );

    // The Tab Folders
    CTabFolder wTabFolder = new CTabFolder( shell, SWT.BORDER );
    props.setLook(  wTabFolder, Props.WIDGET_STYLE_TAB );

    // /////////////////////
    // START OF INPUT TAB //
    // /////////////////////

    CTabItem wInputTab = new CTabItem( wTabFolder, SWT.NONE );
    wInputTab.setText( BaseMessages.getString( PKG, "SunriseStepDialog.InputTab.TabItem" ) );

    Composite wInputComp = new Composite( wTabFolder, SWT.NONE );
    props.setLook( wInputComp );

    FormLayout generalLayout = new FormLayout();
    generalLayout.marginWidth = margin;
    generalLayout.marginHeight = margin;
    wInputComp.setLayout( generalLayout );

    // Input Field Date
    inputFieldDate = new LabelCombo( wInputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.InputDate.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.InputDate.Tooltip" ) );
    props.setLook( inputFieldDate );
    inputFieldDate.addModifyListener( lsMod );
    FormData fdInputFieldDate = new FormData();
    fdInputFieldDate.left = new FormAttachment( 0, -margin );
    fdInputFieldDate.top = new FormAttachment( wStepname, 2 * margin );
    fdInputFieldDate.right = new FormAttachment( 100, -margin );
    inputFieldDate.setLayoutData( fdInputFieldDate );

    // Input Field Latitude
    inputFieldLatitude = new LabelCombo( wInputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.InputLatitude.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.InputLatitude.Tooltip" ) );
    props.setLook( inputFieldLatitude );
    inputFieldLatitude.addModifyListener( lsMod );
    FormData fdInputFieldLatitude = new FormData();
    fdInputFieldLatitude.left = new FormAttachment( 0, -margin );
    fdInputFieldLatitude.top = new FormAttachment( inputFieldDate, 2 * margin );
    fdInputFieldLatitude.right = new FormAttachment( 100, -margin );
    inputFieldLatitude.setLayoutData( fdInputFieldLatitude );

    // Input Field Longitude
    inputFieldLongitude = new LabelCombo( wInputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.InputLongitude.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.InputLongitude.Tooltip" ) );
    props.setLook( inputFieldLongitude );
    inputFieldLongitude.addModifyListener( lsMod );
    FormData fdInputFieldLongitude = new FormData();
    fdInputFieldLongitude.left = new FormAttachment( 0, -margin );
    fdInputFieldLongitude.top = new FormAttachment( inputFieldLatitude, 2 * margin );
    fdInputFieldLongitude.right = new FormAttachment( 100, -margin );
    inputFieldLongitude.setLayoutData( fdInputFieldLongitude );

    // Input Field TimeZone
    inputFieldTimeZone = new LabelCombo( wInputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.InputTimeZone.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.InputTimeZone.Tooltip" ) );
    props.setLook( inputFieldTimeZone );
    inputFieldTimeZone.addModifyListener( lsMod );
    FormData fdInputFieldTimeZone = new FormData();
    fdInputFieldTimeZone.left = new FormAttachment( 0, -margin );
    fdInputFieldTimeZone.top = new FormAttachment( inputFieldLongitude, 2 * margin );
    fdInputFieldTimeZone.right = new FormAttachment( 100, -margin );
    inputFieldTimeZone.setLayoutData( fdInputFieldTimeZone );

    FormData fdInputComp = new FormData();
    fdInputComp.left = new FormAttachment( 0, 0 );
    fdInputComp.top = new FormAttachment( 0, 0 );
    fdInputComp.right = new FormAttachment( 100, 0 );
    fdInputComp.bottom = new FormAttachment( 100, 0 );
    wInputComp.setLayoutData( fdInputComp );

    wInputComp.layout();
    wInputTab.setControl( wInputComp );

    // ///////////////////
    // END OF INPUT TAB //
    // ///////////////////

    // //////////////////////
    // START OF OUTPUT TAB //
    // //////////////////////

    CTabItem wOutputTab = new CTabItem( wTabFolder, SWT.NONE );
    wOutputTab.setText( BaseMessages.getString( PKG, "SunriseStepDialog.OutputTab.TabItem" ) );

    Composite wOutputComp = new Composite( wTabFolder, SWT.NONE );
    props.setLook( wOutputComp );

    FormLayout outputLayout = new FormLayout();
    outputLayout.marginWidth = margin;
    outputLayout.marginHeight = margin;
    wOutputComp.setLayout( outputLayout );

    // Output Field Sunrise Astronomical
    outputSunriseAstronomical = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseAstronomical.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseAstronomical.Tooltip" ) );
    props.setLook( outputSunriseAstronomical );
    outputSunriseAstronomical.addModifyListener( lsMod );
    FormData fdOutputFieldSunriseAstronomical = new FormData();
    fdOutputFieldSunriseAstronomical.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunriseAstronomical.top = new FormAttachment( wStepname, 2 * margin );
    fdOutputFieldSunriseAstronomical.right = new FormAttachment( 100, -margin );
    outputSunriseAstronomical.setLayoutData( fdOutputFieldSunriseAstronomical );

    // Output Field Sunset Astronomical
    outputSunsetAstronomical = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetAstronomical.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetAstronomical.Tooltip" ) );
    props.setLook( outputSunsetAstronomical );
    outputSunsetAstronomical.addModifyListener( lsMod );
    FormData fdOutputFieldSunsetAstronomical = new FormData();
    fdOutputFieldSunsetAstronomical.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunsetAstronomical.top = new FormAttachment( outputSunriseAstronomical, 2 * margin );
    fdOutputFieldSunsetAstronomical.right = new FormAttachment( 100, -margin );
    outputSunsetAstronomical.setLayoutData( fdOutputFieldSunsetAstronomical );

    // Output Field Sunrise Civil
    outputSunriseCivil = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseCivil.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseCivil.Tooltip" ) );
    props.setLook( outputSunriseCivil );
    outputSunriseCivil.addModifyListener( lsMod );
    FormData fdOutputFieldSunriseCivil = new FormData();
    fdOutputFieldSunriseCivil.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunriseCivil.top = new FormAttachment( outputSunsetAstronomical, 2 * margin );
    fdOutputFieldSunriseCivil.right = new FormAttachment( 100, -margin );
    outputSunriseCivil.setLayoutData( fdOutputFieldSunriseCivil );

    // Output Field Sunset Civil
    outputSunsetCivil = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetCivil.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetCivil.Tooltip" ) );
    props.setLook( outputSunsetCivil );
    outputSunsetCivil.addModifyListener( lsMod );
    FormData fdOutputFieldSunsetCivil = new FormData();
    fdOutputFieldSunsetCivil.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunsetCivil.top = new FormAttachment( outputSunriseCivil, 2 * margin );
    fdOutputFieldSunsetCivil.right = new FormAttachment( 100, -margin );
    outputSunsetCivil.setLayoutData( fdOutputFieldSunsetCivil );

    // Output Field Sunrise Nautical
    outputSunriseNautical = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseNautical.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseNautical.Tooltip" ) );
    props.setLook( outputSunriseNautical );
    outputSunriseNautical.addModifyListener( lsMod );
    FormData fdOutputFieldSunriseNautical = new FormData();
    fdOutputFieldSunriseNautical.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunriseNautical.top = new FormAttachment( outputSunsetCivil, 2 * margin );
    fdOutputFieldSunriseNautical.right = new FormAttachment( 100, -margin );
    outputSunriseNautical.setLayoutData( fdOutputFieldSunriseNautical );

    // Output Field Sunset Nautical
    outputSunsetNautical = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetNautical.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetNautical.Tooltip" ) );
    props.setLook( outputSunsetNautical );
    outputSunsetNautical.addModifyListener( lsMod );
    FormData fdOutputFieldSunsetNautical = new FormData();
    fdOutputFieldSunsetNautical.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunsetNautical.top = new FormAttachment( outputSunriseNautical, 2 * margin );
    fdOutputFieldSunsetNautical.right = new FormAttachment( 100, -margin );
    outputSunsetNautical.setLayoutData( fdOutputFieldSunsetNautical );

    // Output Field Sunrise Official
    outputSunriseOfficial = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseOfficial.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunriseOfficial.Tooltip" ) );
    props.setLook( outputSunriseOfficial );
    outputSunriseOfficial.addModifyListener( lsMod );
    FormData fdOutputFieldSunriseOfficial = new FormData();
    fdOutputFieldSunriseOfficial.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunriseOfficial.top = new FormAttachment( outputSunsetNautical, 2 * margin );
    fdOutputFieldSunriseOfficial.right = new FormAttachment( 100, -margin );
    outputSunriseOfficial.setLayoutData( fdOutputFieldSunriseOfficial );

    // Output Field Sunset Official
    outputSunsetOfficial = new LabelText( wOutputComp,
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetOfficial.Label" ),
      BaseMessages.getString( PKG, "SunriseStepDialog.OutputSunsetOfficial.Tooltip" ) );
    props.setLook( outputSunsetOfficial );
    outputSunsetOfficial.addModifyListener( lsMod );
    FormData fdOutputFieldSunsetOfficial = new FormData();
    fdOutputFieldSunsetOfficial.left = new FormAttachment( 0, -margin );
    fdOutputFieldSunsetOfficial.top = new FormAttachment( outputSunriseOfficial, 2 * margin );
    fdOutputFieldSunsetOfficial.right = new FormAttachment( 100, -margin );
    outputSunsetOfficial.setLayoutData( fdOutputFieldSunsetOfficial );

    FormData fdOutputComp = new FormData();
    fdOutputComp.left = new FormAttachment( 0, 0 );
    fdOutputComp.top = new FormAttachment( 0, 0 );
    fdOutputComp.right = new FormAttachment( 100, 0 );
    fdOutputComp.bottom = new FormAttachment( 100, 0 );
    wOutputComp.setLayoutData( fdOutputComp );

    wOutputComp.layout();
    wOutputTab.setControl( wOutputComp );

    // ////////////////////
    // END OF OUTPUT TAB //
    // ////////////////////

    FormData fdTabFolder = new FormData();
    fdTabFolder.left = new FormAttachment( 0, 0 );
    fdTabFolder.top = new FormAttachment( wStepname, margin );
    fdTabFolder.right = new FormAttachment( 100, 0 );
    fdTabFolder.bottom = new FormAttachment( 100, -50 );
    wTabFolder.setLayoutData( fdTabFolder );

    wTabFolder.setSelection( 0 );

    // ////////////////////
    // END OF TAB FOLDER //
    // ////////////////////

    // Some buttons
    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    setButtonPositions( new Button[] { wOK, wCancel }, margin, wTabFolder );

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wCancel.addListener( SWT.Selection, lsCancel );
    wOK.addListener( SWT.Selection, lsOK );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wStepname.addSelectionListener( lsDef );

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    setComboValues();

    // Set the shell size, based upon previous time...
    setSize();

    getData();
    input.setChanged( changed );

    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return stepname;
  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    inputFieldDate.setText( Const.NVL( input.getInputFieldnameDate(), "" ) );
    inputFieldLatitude.setText( Const.NVL( input.getInputFieldnameLatitude(), "" ) );
    inputFieldLongitude.setText( Const.NVL( input.getInputFieldnameLongitude(), "" ) );
    inputFieldTimeZone.setText( Const.NVL( input.getTimeZone(), "" ) );
    
    outputSunriseAstronomical.setText( Const.NVL( input.getOutputFieldnameSunriseAstronomical(), "" ) );
    outputSunriseCivil.setText( Const.NVL( input.getOutputFieldnameSunriseCivil(), "" ) );
    outputSunriseNautical.setText( Const.NVL( input.getOutputFieldnameSunriseNautical(), "" ) );
    outputSunriseOfficial.setText( Const.NVL( input.getOutputFieldnameSunriseOfficial(), "" ) );
    outputSunsetAstronomical.setText( Const.NVL( input.getOutputFieldnameSunsetAstronomical(), "" ) );
    outputSunsetCivil.setText( Const.NVL( input.getOutputFieldnameSunsetCivil(), "" ) );
    outputSunsetNautical.setText( Const.NVL( input.getOutputFieldnameSunsetNautical(), "" ) );
    outputSunsetOfficial.setText( Const.NVL( input.getOutputFieldnameSunsetOfficial(), "" ) );

    wStepname.selectAll();
    wStepname.setFocus();
  }

  private void cancel() {
    stepname = null;
    input.setChanged( changed );
    dispose();
  }

  private void ok() {

    if ( Const.isEmpty( wStepname.getText() ) ) {
      return;
    }

    // Get the information for the dialog into the input structure.
    getInfo( input );

    dispose();
  }

  private void getInfo( SunriseStepMeta inf ) {
    inf.setInputFieldnameDate( inputFieldDate.getText() );
    inf.setInputFieldnameLatitude( inputFieldLatitude.getText() );
    inf.setInputFieldnameLongitude( inputFieldLongitude.getText() );
    inf.setTimeZone( inputFieldTimeZone.getText() );

    inf.setOutputFieldnameSunriseAstronomical( outputSunriseAstronomical.getText() );
    inf.setOutputFieldnameSunriseCivil( outputSunriseCivil.getText() );
    inf.setOutputFieldnameSunriseNautical( outputSunriseNautical.getText() );
    inf.setOutputFieldnameSunriseOfficial( outputSunriseOfficial.getText() );
    inf.setOutputFieldnameSunsetAstronomical( outputSunsetAstronomical.getText() );
    inf.setOutputFieldnameSunsetCivil( outputSunsetCivil.getText() );
    inf.setOutputFieldnameSunsetNautical( outputSunsetNautical.getText() );
    inf.setOutputFieldnameSunsetOfficial( outputSunsetOfficial.getText() );

    stepname = wStepname.getText(); // return value
  }

  private void setComboValues() {
    Runnable fieldLoader = new Runnable() {
      public void run() {
        RowMetaInterface prevFields = null;
        try {
          prevFields = transMeta.getPrevStepFields( stepname );
        } catch ( KettleException e ) {
          prevFields = new RowMeta();
          String msg = BaseMessages.getString( PKG, "SunriseDialog.DoMapping.UnableToFindInput" );
          logError( msg );
        }
        String[] prevStepFieldNames = prevFields != null ? prevFields.getFieldNames() : new String[0];
        Arrays.sort( prevStepFieldNames );
        inputFieldDate.setItems( prevStepFieldNames );
        inputFieldLatitude.setItems( prevStepFieldNames );
        inputFieldLongitude.setItems( prevStepFieldNames );

        String[] timeZones = TimeZone.getAvailableIDs();
        Arrays.sort( timeZones );
        inputFieldTimeZone.setItems( timeZones );
      }
    };
    shell.getDisplay().asyncExec( fieldLoader );
  }
}
