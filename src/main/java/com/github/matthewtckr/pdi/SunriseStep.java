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

package com.github.matthewtckr.pdi;

import java.util.Date;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.github.matthewtckr.pdi.SunriseStepData;
import com.github.matthewtckr.pdi.SunriseStepMeta;

public class SunriseStep extends BaseStep implements StepInterface {
	
  private static Class<?> PKG = SunriseStepMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$
  private SunriseStepMeta meta;
  private SunriseStepData data;

	public SunriseStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
      Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
	}
	
	/**
     * Initialize and do work where other steps need to wait for...
     *
     * @param stepMetaInterface
     *          The metadata to work with
     * @param stepDataInterface
     *          The data to initialize
     */
    public boolean init( StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface ) {
      if ( !super.init( stepMetaInterface, stepDataInterface ) ) {
        return false;
      }
      meta = (SunriseStepMeta) stepMetaInterface;
      data = (SunriseStepData) stepDataInterface;
      return true;
    }

	public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
	  Object[] row = getRow();

    if ( row == null ) {
      setOutputDone();
      return false;
    }

    if ( first ) {
      first = false;
      data.outputRowMeta = getInputRowMeta().clone();
      meta.getFields( data.outputRowMeta, getStepname(), null, null, getTrans(), repository, metaStore );

      data.indexInputDate = getInputRowMeta().indexOfValue( meta.getInputFieldnameDate() );
      data.indexInputLatitude = getInputRowMeta().indexOfValue( meta.getInputFieldnameLatitude() );
      data.indexInputLongitude = getInputRowMeta().indexOfValue( meta.getInputFieldnameLongitude() );
      data.indexOutputSunriseAstronomical =
        data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunriseAstronomical() );
      data.indexOutputSunriseCivil = data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunriseCivil() );
      data.indexOutputSunriseNautical = data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunriseNautical() );
      data.indexOutputSunriseOfficial = data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunriseOfficial() );
      data.indexOutputSunsetAstronomical =
        data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunsetAstronomical() );
      data.indexOutputSunsetCivil = data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunsetCivil() );
      data.indexOutputSunsetNautical = data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunsetNautical() );
      data.indexOutputSunsetOfficial = data.outputRowMeta.indexOfValue( meta.getOutputFieldnameSunsetOfficial() );

      if ( data.indexInputDate < 0 ) {
        setErrors( 1 );
        logError( BaseMessages.getString( PKG, "SunriseStep.Error.MissingInputDate",
          meta.getInputFieldnameDate() ) );
        setOutputDone();
        return false;
      }
      if ( data.indexInputLatitude < 0 ) {
        setErrors( 1 );
        logError( BaseMessages.getString( PKG, "SunriseStep.Error.MissingInputLatitude",
          meta.getInputFieldnameLatitude() ) );
        setOutputDone();
        return false;
      }
      if ( data.indexInputLongitude < 0 ) {
        setErrors( 1 );
        logError( BaseMessages.getString( PKG, "SunriseStep.Error.MissingInputLongitude",
          meta.getInputFieldnameLongitude() ) );
        setOutputDone();
        return false;
      }
    }

    row = RowDataUtil.resizeArray( row, data.outputRowMeta.size() );

    data.resetCalculator(
      getInputRowMeta().getNumber( row, data.indexInputLatitude ),
      getInputRowMeta().getNumber( row, data.indexInputLongitude ),
      Const.NVL( meta.getTimeZone(), "GMT" ) );

    Date inputDate = getInputRowMeta().getDate( row, data.indexInputDate );
    if ( data.indexOutputSunriseAstronomical >= 0 ) {
      row[data.indexOutputSunriseAstronomical] = data.getAstronomicalSunriseCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunriseCivil >= 0 ) {
      row[data.indexOutputSunriseCivil] = data.getCivilSunriseCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunriseNautical >= 0 ) {
      row[data.indexOutputSunriseNautical] = data.getNauticalSunriseCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunriseOfficial >= 0 ) {
      row[data.indexOutputSunriseOfficial] = data.getOfficialSunriseCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunsetAstronomical >= 0 ) {
      row[data.indexOutputSunsetAstronomical] = data.getAstronomicalSunsetCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunsetCivil >= 0 ) {
      row[data.indexOutputSunsetCivil] = data.getCivilSunsetCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunsetNautical >= 0 ) {
      row[data.indexOutputSunsetNautical] = data.getNauticalSunsetCalendarForDate( inputDate ).getTime();
    }
    if ( data.indexOutputSunsetOfficial >= 0 ) {
      row[data.indexOutputSunsetOfficial] = data.getOfficialSunsetCalendarForDate( inputDate ).getTime();
    }

    putRow( data.outputRowMeta, row );

    if ( checkFeedback( getLinesRead() ) ) {
      if( log.isBasic() ) {
        logBasic( BaseMessages.getString( PKG, "SunriseStep.Log.LineNumber" ) + getLinesRead() ); 
      }
    }

    return true;
	}
}