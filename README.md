# Use Android [BroadcastReceiver](https://developer.android.com/reference/android/content/BroadcastReceiver) to Read Incoming Call
In this demo, we will walk you through using the Android [BroadcastReceiver](https://developer.android.com/reference/android/content/BroadcastReceiver) to let your users receive Miscall OTP without typing the code.

## Setup
If you haven't already, prepare your Android apps backend's API. You can see [sample backend with PHP](https://github.com/mchairul/backend-citcall).

## Permissions
Make sure your users allowed these permissions!
for guidelines to get google play permission click [here](https://docs.citcall.com/guide/android.php)

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />

## [Receiver](https://developer.android.com/guide/topics/manifest/receiver-element)

    <receiver
        android:name=".MyBroadcastReceiver"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.PHONE_STATE" />
        </intent-filter>
    </receiver>

Contribute
----------

1. Check for open issues or open a new issue for a feature request or a bug
2. Fork [the repository](https://github.com/mchairul/Citcall-Android-Auto-Read) on Github to start making your changes to the
    `main` branch (or branch off of it)
3. Write a test which shows that the bug was fixed or that the feature works as expected
4. Send a pull request and bug us until We merge it