/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.pentaho.pdi;

import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.util.List;
import java.util.TimeZone;


@Step(
  id = "SunriseCalculator",
  image = "SunriseStep.svg",
  i18nPackageName="org.pentaho.pdi",
  name = "SunriseCalculator.Name",
  description = "SunriseCalculator.TooltipDesc",
  categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform"
)
public class SunriseStepMeta extends BaseStepMeta implements StepMetaInterface {
  
  private static Class<?> PKG = SunriseStep.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

  private String inputFieldnameDate;
  private String inputFieldnameLatitude;
  private String inputFieldnameLongitude;
  private String timeZone;

  private String outputFieldnameSunriseAstronomical;
  private String outputFieldnameSunriseCivil;
  private String outputFieldnameSunriseNautical;
  private String outputFieldnameSunriseOfficial;
  private String outputFieldnameSunsetAstronomical;
  private String outputFieldnameSunsetCivil;
  private String outputFieldnameSunsetNautical;
  private String outputFieldnameSunsetOfficial;

  public SunriseStepMeta() {
    super(); // allocate BaseStepMeta
  }

  public String getXML() throws KettleException {
    StringBuilder xml = new StringBuilder( super.getXML() );
    xml.append( "  " ).append( XMLHandler.addTagValue( "inputFieldnameDate", inputFieldnameDate ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "inputFieldnameLatitude", inputFieldnameLatitude ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "inputFieldnameLongitude", inputFieldnameLongitude ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "timeZone", timeZone ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunriseAstronomical", outputFieldnameSunriseAstronomical ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunriseCivil", outputFieldnameSunriseCivil ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunriseNautical", outputFieldnameSunriseNautical ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunriseOfficial", outputFieldnameSunriseOfficial ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunsetAstronomical", outputFieldnameSunsetAstronomical ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunsetCivil", outputFieldnameSunsetCivil ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunsetNautical", outputFieldnameSunsetNautical ) );
    xml.append( "  " ).append( XMLHandler.addTagValue( "outputFieldnameSunsetOfficial", outputFieldnameSunsetOfficial ) );
    return xml.toString();
  }

  public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
    readData(stepnode);
  }

  public Object clone() {
    Object retval = super.clone();
    return retval;
  }
  
  private void readData(Node stepnode) {
    setInputFieldnameDate( XMLHandler.getTagValue( stepnode, "inputFieldnameDate" ) );
    setInputFieldnameLatitude( XMLHandler.getTagValue( stepnode, "inputFieldnameLatitude" ) );
    setInputFieldnameLongitude( XMLHandler.getTagValue( stepnode, "inputFieldnameLongitude" ) );
    setTimeZone( XMLHandler.getTagValue( stepnode, "timeZone" ) );
    setOutputFieldnameSunriseAstronomical( XMLHandler.getTagValue( stepnode, "outputFieldnameSunriseAstronomical" ) );
    setOutputFieldnameSunriseCivil( XMLHandler.getTagValue( stepnode, "outputFieldnameSunriseCivil" ) );
    setOutputFieldnameSunriseNautical( XMLHandler.getTagValue( stepnode, "outputFieldnameSunriseNautical" ) );
    setOutputFieldnameSunriseOfficial( XMLHandler.getTagValue( stepnode, "outputFieldnameSunriseOfficial" ) );
    setOutputFieldnameSunsetAstronomical( XMLHandler.getTagValue( stepnode, "outputFieldnameSunsetAstronomical" ) );
    setOutputFieldnameSunsetCivil( XMLHandler.getTagValue( stepnode, "outputFieldnameSunsetCivil" ) );
    setOutputFieldnameSunsetNautical( XMLHandler.getTagValue( stepnode, "outputFieldnameSunsetNautical" ) );
    setOutputFieldnameSunsetOfficial( XMLHandler.getTagValue( stepnode, "outputFieldnameSunsetOfficial" ) );
  }

  public void setDefault() {
    setTimeZone( "GMT" );
    setOutputFieldnameSunriseAstronomical( "Sunrise_Astronomical" );
    setOutputFieldnameSunriseCivil( "Sunrise_Civil" );
    setOutputFieldnameSunriseNautical( "Sunrise_Nautical" );
    setOutputFieldnameSunriseOfficial( "Sunrise_Official" );
    setOutputFieldnameSunsetAstronomical( "Sunset_Astronomical" );
    setOutputFieldnameSunsetCivil( "Sunset_Civil" );
    setOutputFieldnameSunsetNautical( "Sunset_Nautical" );
    setOutputFieldnameSunsetOfficial( "Sunset_Official" );
  }

  public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException {
    super.readRep( rep, metaStore, id_step, databases );
    setInputFieldnameDate( rep.getStepAttributeString( id_step, "inputFieldnameDate" ) );
    setInputFieldnameLatitude( rep.getStepAttributeString( id_step, "inputFieldnameLatitude" ) );
    setInputFieldnameLongitude( rep.getStepAttributeString( id_step, "inputFieldnameLongitude" ) );
    setTimeZone( rep.getStepAttributeString( id_step, "timeZone" ) );
    setOutputFieldnameSunriseAstronomical( rep.getStepAttributeString( id_step, "outputFieldnameSunriseAstronomical" ) );
    setOutputFieldnameSunriseCivil( rep.getStepAttributeString( id_step, "outputFieldnameSunriseCivil" ) );
    setOutputFieldnameSunriseNautical( rep.getStepAttributeString( id_step, "outputFieldnameSunriseNautical" ) );
    setOutputFieldnameSunriseOfficial( rep.getStepAttributeString( id_step, "outputFieldnameSunriseOfficial" ) );
    setOutputFieldnameSunsetAstronomical( rep.getStepAttributeString( id_step, "outputFieldnameSunsetAstronomical" ) );
    setOutputFieldnameSunsetCivil( rep.getStepAttributeString( id_step, "outputFieldnameSunsetCivil" ) );
    setOutputFieldnameSunsetNautical( rep.getStepAttributeString( id_step, "outputFieldnameSunsetNautical" ) );
    setOutputFieldnameSunsetOfficial( rep.getStepAttributeString( id_step, "outputFieldnameSunsetOfficial" ) );
  }
  
  public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
    throws KettleException {
    super.saveRep( rep, metaStore, id_transformation, id_step );
    rep.saveStepAttribute( id_transformation, id_step, "inputFieldnameDate", inputFieldnameDate );
    rep.saveStepAttribute( id_transformation, id_step, "inputFieldnameLatitude", inputFieldnameLatitude );
    rep.saveStepAttribute( id_transformation, id_step, "inputFieldnameLongitude", inputFieldnameLongitude );
    rep.saveStepAttribute( id_transformation, id_step, "timeZone", timeZone );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunriseAstronomical", outputFieldnameSunriseAstronomical );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunriseCivil", outputFieldnameSunriseCivil );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunriseNautical", outputFieldnameSunriseNautical );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunriseOfficial", outputFieldnameSunriseOfficial );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunsetAstronomical", outputFieldnameSunsetAstronomical );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunsetCivil", outputFieldnameSunsetCivil );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunsetNautical", outputFieldnameSunsetNautical );
    rep.saveStepAttribute( id_transformation, id_step, "outputFieldnameSunsetOfficial", outputFieldnameSunsetOfficial );
  }
  
  public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, 
    VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {
    super.getFields( rowMeta, origin, info, nextStep, space, repository, metaStore );
    TimeZone outputTZ = TimeZone.getTimeZone( "GMT" );
    if ( getTimeZone() != null ) {
      outputTZ = TimeZone.getTimeZone( getTimeZone() );
    }
    addFieldToRow( rowMeta, outputFieldnameSunriseAstronomical, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunriseCivil, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunriseNautical, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunriseOfficial, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunsetAstronomical, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunsetCivil, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunsetNautical, ValueMetaInterface.TYPE_DATE, outputTZ );
    addFieldToRow( rowMeta, outputFieldnameSunsetOfficial, ValueMetaInterface.TYPE_DATE, outputTZ );
  }
  
  public void check(List<CheckResultInterface> remarks, TransMeta transMeta, 
    StepMeta stepMeta, RowMetaInterface prev, String input[], String output[],
    RowMetaInterface info, VariableSpace space, Repository repository, 
    IMetaStore metaStore) {
    CheckResult cr;
    if ( prev==null || prev.size()==0 ) {
      cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, BaseMessages.getString(PKG, "SunriseStepMeta.CheckResult.NotReceivingFields"), stepMeta); 
      remarks.add(cr);
    }
    else {
      cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(PKG, "SunriseStepMeta.CheckResult.StepRecevingData",prev.size()+""), stepMeta);  
      remarks.add(cr);
    }
    
    // See if we have input streams leading to this step!
    if ( input.length > 0 ) {
      cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(PKG, "SunriseStepMeta.CheckResult.StepRecevingData2"), stepMeta); 
      remarks.add(cr);
    }
    else {
      cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(PKG, "SunriseStepMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepMeta); 
      remarks.add(cr);
    }
    if ( Const.isEmpty( getInputFieldnameDate() ) || prev.indexOfValue( getInputFieldnameDate() ) < 0  ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.InputDateMissing" ), stepMeta );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.InputDateOK" ), stepMeta );
    }
    remarks.add( cr );

    if ( Const.isEmpty( getInputFieldnameLatitude() ) || prev.indexOfValue( getInputFieldnameLatitude() ) < 0  ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.InputLatitudeMissing" ), stepMeta );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.InputLatitudeOK" ), stepMeta );
    }
    remarks.add( cr );

    if ( Const.isEmpty( getInputFieldnameLongitude() ) || prev.indexOfValue( getInputFieldnameLongitude() ) < 0  ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.InputLongitudeMissing" ), stepMeta );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.InputLongitudeOK" ), stepMeta );
    }
    remarks.add( cr );

    if ( Const.isEmpty( getTimeZone() ) ) {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_WARNING, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.TimeZoneNotSpecified" ), stepMeta );
    } else {
      cr =
        new CheckResult( CheckResult.TYPE_RESULT_OK, BaseMessages.getString(
          PKG, "SunriseStepMeta.CheckResult.TimeZoneSpecified" ), stepMeta );
    }
  }
  
  public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans) {
    return new SunriseStep(stepMeta, stepDataInterface, cnr, tr, trans);
  }
  
  public StepDataInterface getStepData() {
    return new SunriseStepData();
  }

  public String getDialogClassName() {
    return "org.pentaho.pdi.SunriseStepDialog";
  }

  public String getInputFieldnameDate() {
    return inputFieldnameDate;
  }

  public void setInputFieldnameDate( String inputFieldnameDate ) {
    this.inputFieldnameDate = inputFieldnameDate;
  }

  public String getInputFieldnameLatitude() {
    return inputFieldnameLatitude;
  }

  public void setInputFieldnameLatitude( String inputFieldnameLatitude ) {
    this.inputFieldnameLatitude = inputFieldnameLatitude;
  }

  public String getInputFieldnameLongitude() {
    return inputFieldnameLongitude;
  }

  public void setInputFieldnameLongitude( String inputFieldnameLongitude ) {
    this.inputFieldnameLongitude = inputFieldnameLongitude;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone( String timeZone ) {
    this.timeZone = timeZone;
  }

  public String getOutputFieldnameSunriseAstronomical() {
    return outputFieldnameSunriseAstronomical;
  }

  public void setOutputFieldnameSunriseAstronomical( String outputFieldnameSunriseAstronomical ) {
    this.outputFieldnameSunriseAstronomical = outputFieldnameSunriseAstronomical;
  }

  public String getOutputFieldnameSunriseCivil() {
    return outputFieldnameSunriseCivil;
  }

  public void setOutputFieldnameSunriseCivil( String outputFieldnameSunriseCivil ) {
    this.outputFieldnameSunriseCivil = outputFieldnameSunriseCivil;
  }

  public String getOutputFieldnameSunriseNautical() {
    return outputFieldnameSunriseNautical;
  }

  public void setOutputFieldnameSunriseNautical( String outputFieldnameSunriseNautical ) {
    this.outputFieldnameSunriseNautical = outputFieldnameSunriseNautical;
  }

  public String getOutputFieldnameSunriseOfficial() {
    return outputFieldnameSunriseOfficial;
  }

  public void setOutputFieldnameSunriseOfficial( String outputFieldnameSunriseOfficial ) {
    this.outputFieldnameSunriseOfficial = outputFieldnameSunriseOfficial;
  }

  public String getOutputFieldnameSunsetAstronomical() {
    return outputFieldnameSunsetAstronomical;
  }

  public void setOutputFieldnameSunsetAstronomical( String outputFieldnameSunsetAstronomical ) {
    this.outputFieldnameSunsetAstronomical = outputFieldnameSunsetAstronomical;
  }

  public String getOutputFieldnameSunsetCivil() {
    return outputFieldnameSunsetCivil;
  }

  public void setOutputFieldnameSunsetCivil( String outputFieldnameSunsetCivil ) {
    this.outputFieldnameSunsetCivil = outputFieldnameSunsetCivil;
  }

  public String getOutputFieldnameSunsetNautical() {
    return outputFieldnameSunsetNautical;
  }

  public void setOutputFieldnameSunsetNautical( String outputFieldnameSunsetNautical ) {
    this.outputFieldnameSunsetNautical = outputFieldnameSunsetNautical;
  }

  public String getOutputFieldnameSunsetOfficial() {
    return outputFieldnameSunsetOfficial;
  }

  public void setOutputFieldnameSunsetOfficial( String outputFieldnameSunsetOfficial ) {
    this.outputFieldnameSunsetOfficial = outputFieldnameSunsetOfficial;
  }

  protected void addFieldToRow( RowMetaInterface row, String fieldName, int type, TimeZone tz ) throws KettleStepException {
    if ( !Const.isEmpty( fieldName ) ) {
      try {
        ValueMetaInterface value = ValueMetaFactory.createValueMeta( fieldName, type );
        value.setOrigin( getName() );
        if ( tz != null && ValueMetaInterface.TYPE_DATE == type ) {
          value.setDateFormatTimeZone( tz );
        }
        row.addValueMeta( value );
      } catch ( KettlePluginException e ) {
        throw new KettleStepException( BaseMessages.getString( PKG,
          "SunriseStepMeta.ValueMetaInterfaceCreation", fieldName ), e );
      }
    }
  }

  protected void addFieldToRow( RowMetaInterface row, String fieldName, int type ) throws KettleStepException {
    addFieldToRow( row, fieldName, type, null );
  }
}
