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

package org.pentaho.pdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaDate;
import org.pentaho.di.core.row.value.ValueMetaNumber;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.TransTestFactory;

public class SunriseStepIntegrationTest {

  private static final ValueMetaString vmString = new ValueMetaString();

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  private List<RowMetaAndData> generateInputRow() {
    RowMetaInterface rowMeta = new RowMeta();
    rowMeta.addValueMeta( new ValueMetaDate( "dateField" ) );
    rowMeta.addValueMeta( new ValueMetaNumber( "latField" ) );
    rowMeta.addValueMeta( new ValueMetaNumber( "longField" ) );

    // Pensacola, FL on February 22, 2016
    Object[] rowData = new Object[3];
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.set( 2016, 1, 22 );
    rowData[0] = cal.getTime();
    rowData[1] = Double.parseDouble( "30.4333" );
    rowData[2] = -87.2;

    List<RowMetaAndData> returnValues = new ArrayList<RowMetaAndData>();
    returnValues.add( new RowMetaAndData( rowMeta, rowData ) );
    return returnValues;
  }

  @Test
  public void transTest() throws KettleException {
    final String stepName = "Sunrise Step";
    SunriseStepMeta meta = new SunriseStepMeta();
    meta.setDefault();
    meta.setTimeZone( "US/Pacific" );
    meta.setInputFieldnameDate( "dateField" );
    meta.setInputFieldnameLatitude( "latField" );
    meta.setInputFieldnameLongitude( "longField" );
    meta.setOutputFieldnameSunriseAstronomical( "TheSunrise_Astronomical" );
    meta.setOutputFieldnameSunriseCivil( "TheSunrise_Civil" );
    meta.setOutputFieldnameSunriseNautical( "TheSunrise_Nautical" );
    meta.setOutputFieldnameSunriseOfficial( "TheSunrise_Official" );
    meta.setOutputFieldnameSunsetAstronomical( "TheSunset_Astronomical" );
    meta.setOutputFieldnameSunsetCivil( "TheSunset_Civil" );
    meta.setOutputFieldnameSunsetNautical( "TheSunset_Nautical" );
    meta.setOutputFieldnameSunsetOfficial( "TheSunset_Official" );

    TransMeta transMeta = TransTestFactory.generateTestTransformation( null, meta, stepName );
    List<RowMetaAndData> results = TransTestFactory.executeTestTransformation( transMeta,
      TransTestFactory.INJECTOR_STEPNAME, stepName, TransTestFactory.DUMMY_STEPNAME, generateInputRow() );

    assertNotNull( results );
    assertEquals( 1, results.size() );
    assertEquals( 11, results.get( 0 ).getRowMeta().size() );

    // NOTE: The requested timezone is different than the local time of the GPS coordinates, that is intentional for this test
    TimeZone tz = TimeZone.getTimeZone( meta.getTimeZone() );
    assertEquals( convertStringToDate( "2016/02/21 03:03:00.000", tz ), results.get( 0 ).getDate( "TheSunrise_Astronomical", new Date( 0L ) ) );
    assertEquals( convertStringToDate( "2016/02/21 03:59:00.000", tz ), results.get( 0 ).getDate( "TheSunrise_Civil", new Date( 0L ) ) );
    assertEquals( convertStringToDate( "2016/02/21 03:31:00.000", tz ), results.get( 0 ).getDate( "TheSunrise_Nautical", new Date( 0L ) ) );
    assertEquals( convertStringToDate( "2016/02/21 04:24:00.000", tz ), results.get( 0 ).getDate( "TheSunrise_Official", new Date( 0L ) ) );
    
    assertEquals( convertStringToDate( "2016/02/21 17:02:00.000", tz ), results.get( 0 ).getDate( "TheSunset_Astronomical", new Date( 0L ) ) );
    assertEquals( convertStringToDate( "2016/02/21 16:06:00.000", tz ), results.get( 0 ).getDate( "TheSunset_Civil", new Date( 0L ) ) );
    assertEquals( convertStringToDate( "2016/02/21 16:34:00.000", tz ), results.get( 0 ).getDate( "TheSunset_Nautical", new Date( 0L ) ) );
    assertEquals( convertStringToDate( "2016/02/21 15:42:00.000", tz ), results.get( 0 ).getDate( "TheSunset_Official", new Date( 0L ) ) );
  }

  static Date convertStringToDate( String input, TimeZone tz ) throws KettleValueException {
    vmString.setDateFormatTimeZone( tz );
    return vmString.getDate( input );
  }

  @Test
  public void testEmptyInput() throws KettleException {
    final String stepName = "Sunrise Step";
    SunriseStepMeta meta = new SunriseStepMeta();
    meta.setDefault();
    meta.setTimeZone( "US/Pacific" );
    meta.setInputFieldnameDate( "dateField" );
    meta.setInputFieldnameLatitude( "latField" );
    meta.setInputFieldnameLongitude( "longField" );
    meta.setOutputFieldnameSunriseAstronomical( "TheSunrise_Astronomical" );
    meta.setOutputFieldnameSunriseCivil( "TheSunrise_Civil" );
    meta.setOutputFieldnameSunriseNautical( "TheSunrise_Nautical" );
    meta.setOutputFieldnameSunriseOfficial( "TheSunrise_Official" );
    meta.setOutputFieldnameSunsetAstronomical( "TheSunset_Astronomical" );
    meta.setOutputFieldnameSunsetCivil( "TheSunset_Civil" );
    meta.setOutputFieldnameSunsetNautical( "TheSunset_Nautical" );
    meta.setOutputFieldnameSunsetOfficial( "TheSunset_Official" );

    TransMeta transMeta = TransTestFactory.generateTestTransformation( null, meta, stepName );
    List<RowMetaAndData> results = null;
    try {
      results = TransTestFactory.executeTestTransformation( transMeta,
        TransTestFactory.INJECTOR_STEPNAME, stepName, TransTestFactory.DUMMY_STEPNAME,
        new ArrayList<RowMetaAndData>() );
    } catch( KettleException e ) {
      fail();
    }

    assertNotNull( results );
    assertEquals( 0, results.size() );
  }
}
