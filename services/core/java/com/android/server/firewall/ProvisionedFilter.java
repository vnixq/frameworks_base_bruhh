/*
 * Copyright (C) 2024 LibreMobileOS
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

package com.android.server.firewall;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class ProvisionedFilter implements Filter {
    @Override
    public boolean matches(IntentFirewall ifw, ComponentName resolvedComponent, Intent intent,
            int callerUid, int callerPid, String resolvedType, int receivingUid, int userId) {
        return matchesPackage(ifw, resolvedComponent.getPackageName(), callerUid, receivingUid,
                userId);
    }

    @Override
    public boolean matchesPackage(IntentFirewall ifw, String resolvedPackage, int callerUid,
            int receivingUid, int userId) {
        try {
            return Settings.Global.getInt(ifw.getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 0) == 1;
        } catch (Exception ex) {
            // It is probably too early to access settings
            return false;
        }
    }

    public static final FilterFactory FACTORY = new FilterFactory("is-provisioned") {
        @Override
        public Filter newFilter(XmlPullParser parser)
                throws IOException, XmlPullParserException {
            return new ProvisionedFilter();
        }
    };
}

