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

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.value.ValueMetaString;

public class SunriseStepDataTest {

  private static final ValueMetaString vmString = new ValueMetaString();

  @Test
  public void smokeTest() throws KettleValueException {
    SunriseStepData data = new SunriseStepData();

    // Orlando, FL
    String tzString = "America/New_York";
    data.resetCalculator( 28.4158, -81.2989, tzString );
    TimeZone tz = TimeZone.getTimeZone( tzString );
    Date inputDate = convertStringToDate( "2015/02/01 00:00:00.000", tz );

    assertEquals( convertStringToDate( "2015/02/01 05:52:00.000", tz ),
      data.getAstronomicalSunriseCalendarForDate( inputDate ).getTime() );
    assertEquals( convertStringToDate( "2015/02/01 06:49:00.000", tz ),
      data.getCivilSunriseCalendarForDate( inputDate ).getTime() );
    assertEquals( convertStringToDate( "2015/02/01 06:20:00.000", tz ),
      data.getNauticalSunriseCalendarForDate( inputDate ).getTime() );
    assertEquals( convertStringToDate( "2015/02/01 07:13:00.000", tz ),
      data.getOfficialSunriseCalendarForDate( inputDate ).getTime() );

    assertEquals( convertStringToDate( "2015/02/01 19:26:00.000", tz ),
      data.getAstronomicalSunsetCalendarForDate( inputDate ).getTime() );
    assertEquals( convertStringToDate( "2015/02/01 18:30:00.000", tz ),
      data.getCivilSunsetCalendarForDate( inputDate ).getTime() );
    assertEquals( convertStringToDate( "2015/02/01 18:58:00.000", tz ),
      data.getNauticalSunsetCalendarForDate( inputDate ).getTime() );
    assertEquals( convertStringToDate( "2015/02/01 18:05:00.000", tz ),
      data.getOfficialSunsetCalendarForDate( inputDate ).getTime() );
  }

  static Date convertStringToDate( String input, TimeZone tz ) throws KettleValueException {
    vmString.setDateFormatTimeZone( tz );
    return vmString.getDate( input );
  }
}
