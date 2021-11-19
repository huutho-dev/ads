### AdsSDK
- Thư viện support show quảng cáo trên android
- Thư viện hiện tại đang support *GoogleAds* và *FacebookAds*
- Nên sử dụng *RemoteConfig* để tải *AdsJsonConfig* cho AdsSDK
- Thời gian mặc định lần load quảng cáo tiếp theo là 60s



#### Config AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.sdk.ads">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

    <application android:hardwareAccelerated="true">
        <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-3940256099942544~3347511713" />
    </application>
</manifest>
```

#### Config build.gradle
```kotlin
defaultConfig {
 	multiDexEnabled true
}

dependencies {
	implementation 'com.android.support:multidex:1.0.3'
	implementation 'com.google.android.gms:play-services-ads:20.4.0'
	implementation 'com.google.code.gson:gson:2.8.8'
	implementation 'com.facebook.android:audience-network-sdk:6.+'
}
```

#### Config init adsSdk
- Khởi tạo trong class Application: ```AdManager.initialize(this)```
- Set config cho sdk: ```AdManager.setAdsConfig(adsJsonConfig, isPremium)```
```json
{
  "isEnable": true,
  "timeDelayToLoad": 0,
  "ads": {
    "banner": [
      {
        "key": "key",
        "value": "value"
      },
      {
        "key": "key",
        "value": "value"
      }
    ],
    "interstitial": [
      {
        "key": "key",
        "value": "value"
      },
      {
        "key": "key",
        "value": "value"
      }
    ],
    "native": [
      {
        "key": "key",
        "value": "value"
      },
      {
        "key": "key",
        "value": "value"
      }
    ],
    "rewarded": [
      {
        "key": "key",
        "value": "value"
      },
      {
        "key": "key",
        "value": "value"
      }
    ],
    "open": [
      {
        "key": "openApp",
        "value": ""
      }
    ]
  }
}
```