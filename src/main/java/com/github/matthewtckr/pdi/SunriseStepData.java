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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;


public class SunriseStepData extends BaseStepData implements StepDataInterface {
  int indexInputDate;
  int indexInputLatitude;
  int indexInputLongitude;
  int indexOutputSunriseAstronomical;
  int indexOutputSunriseCivil;
  int indexOutputSunriseNautical;
  int indexOutputSunriseOfficial;
  int indexOutputSunsetAstronomical;
  int indexOutputSunsetCivil;
  int indexOutputSunsetNautical;
  int indexOutputSunsetOfficial;

  RowMetaInterface outputRowMeta;

  private SunriseSunsetCalculator calculator;
  private TimeZone currentTimeZone;
  private Map<String, TimeZone> timezoneCache = new HashMap<String, TimeZone>();
  private Calendar calInstance = Calendar.getInstance();

  void resetCalculator( Double latitude, Double longitude, String timeZone ) {
    TimeZone newTimeZone = null;

    // Keep cached lookups of timezones, to avoid excessive TimeZone init times
    if ( timezoneCache.containsKey( timeZone ) ) {
      newTimeZone = timezoneCache.get( timeZone );
    } else {
      newTimeZone = TimeZone.getTimeZone( timeZone );
      timezoneCache.put( timeZone, newTimeZone );
    }

    if ( calculator == null ) {
      currentTimeZone = newTimeZone;
      calculator = new SunriseSunsetCalculator(
        new Location( latitude.doubleValue(), longitude.doubleValue() ), newTimeZone );
      calInstance.setTimeZone( currentTimeZone );
    } else {
      if ( currentTimeZone.equals( newTimeZone ) &&
          calculator.getLocation().getLatitude().equals( latitude ) &&
          calculator.getLocation().getLongitude().equals( longitude ) ) {
        return;
      } else {
        currentTimeZone = newTimeZone;
        calculator = new SunriseSunsetCalculator(
          new Location( latitude.doubleValue(), longitude.doubleValue() ), currentTimeZone );
        calInstance.setTimeZone( currentTimeZone );
      }
    }
  }

  Calendar getAstronomicalSunriseCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getAstronomicalSunriseCalendarForDate( calInstance );
  }

  Calendar getCivilSunriseCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getCivilSunriseCalendarForDate( calInstance );
  }

  Calendar getNauticalSunriseCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getNauticalSunriseCalendarForDate( calInstance );
  }

  Calendar getOfficialSunriseCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getOfficialSunriseCalendarForDate( calInstance );
  }

  Calendar getAstronomicalSunsetCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getAstronomicalSunsetCalendarForDate( calInstance );
  }

  Calendar getCivilSunsetCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getCivilSunsetCalendarForDate( calInstance );
  }

  Calendar getNauticalSunsetCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getNauticalSunsetCalendarForDate( calInstance );
  }

  Calendar getOfficialSunsetCalendarForDate( Date inputDate ) {
    calInstance.setTime( inputDate );
    return calculator.getOfficialSunsetCalendarForDate( calInstance );
  }
}