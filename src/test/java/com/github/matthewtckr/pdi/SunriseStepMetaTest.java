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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;

import com.github.matthewtckr.pdi.SunriseStepMeta;

public class SunriseStepMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "inputFieldnameDate", "inputFieldnameLatitude", "inputFieldnameLongitude", "timeZone",
        "outputFieldnameSunriseAstronomical", "outputFieldnameSunriseCivil",
        "outputFieldnameSunriseNautical", "outputFieldnameSunriseOfficial",
        "outputFieldnameSunsetAstronomical", "outputFieldnameSunsetCivil",
        "outputFieldnameSunsetNautical", "outputFieldnameSunsetOfficial" );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( SunriseStepMeta.class, attributes, new HashMap<String, String>(),
        new HashMap<String, String>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }
}
