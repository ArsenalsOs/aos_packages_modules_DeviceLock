/*
 * Copyright (C) 2023 The Android Open Source Project
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

package com.android.devicelockcontroller.receivers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.core.app.ApplicationProvider;

import com.android.devicelockcontroller.TestDeviceLockControllerApplication;
import com.android.devicelockcontroller.policy.DevicePolicyController;
import com.android.devicelockcontroller.policy.DeviceStateController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestDeviceLockControllerApplication.class)
public final class BootUtilsTest {
    private DeviceStateController mStateController;
    private DevicePolicyController mPolicyController;
    private TestDeviceLockControllerApplication mTestApplication;

    @Before
    public void setup() {
        mTestApplication = ApplicationProvider.getApplicationContext();
        mStateController = mTestApplication.getMockStateController();
        mPolicyController = mTestApplication.getMockPolicyController();
    }

    @Test
    public void startLockTaskModeAtBoot_success() {
        when(mStateController.isLocked()).thenReturn(true);
        BootUtils.startLockTaskModeAtBoot(mTestApplication);
        verify(mPolicyController).enqueueStartLockTaskModeWorker();
    }

    @Test
    public void startLockTaskModeAtBoot_deviceIsNotLocked_doesNotLaunchActivityInLockedMode() {
        when(mStateController.isLocked()).thenReturn(false);
        BootUtils.startLockTaskModeAtBoot(mTestApplication);
        verify(mPolicyController, never()).enqueueStartLockTaskModeWorker();
    }
}