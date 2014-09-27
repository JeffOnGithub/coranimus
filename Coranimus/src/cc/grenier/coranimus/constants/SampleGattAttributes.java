/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.grenier.coranimus.constants;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap<String, String>();
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    
    static {
        // Sample Services.
        attributes.put(BleServices.GENERIC_ACCESS, "Generic Access Service");
        attributes.put(BleServices.GENERIC_ATTRIBUTE, "Generic Attribute Service");
        attributes.put(BleServices.HEART_RATE, "Heart Rate Service");
        attributes.put(BleServices.DEVICE_INFORMATION, "Device Information Service");
        attributes.put(BleServices.BATTERY_SERVICE,"Battery Service");
        attributes.put(BleServices.IMMEDIATE_ALERT, "Immediate Alert Service");
        attributes.put(BleServices.LINK_LOSS, "Link Loss Service");
        attributes.put(BleServices.TX_POWER, "Transmit Power Service");

        // Sample Characteristics.
        attributes.put(BleCharacteristics.HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put(BleCharacteristics.HEART_RATE_BODY_SENSOR_LOCATION, "Heart Rate Sensor Location");
        attributes.put(BleCharacteristics.MANUFACTURER_NAME_STRING, "Manufacturer Name String");
        
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
