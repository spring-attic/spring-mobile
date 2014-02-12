/*
 * Copyright 2010-2014 the original author or authors.
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

package org.springframework.mobile.device;

/**
 * This class contains a <emphasis>sampling</emphasis> of mobile and tablet user agent
 * strings. It is by no means comprehensive.
 * 
 * @author Roy Clarkson
 */
class UserAgent {

	// Browsers

	public static final String OSX_10_6_FIREFOX3_6_STRING = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12";

	public static final String OSX_10_7_3_SAFARI5_1_5_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/534.55.3 (KHTML, like Gecko) Version/5.1.5 Safari/534.55.3";

	// iOS

	public static final String IPODTOUCH_IOS1_1_3_STRING = "Mozilla/5.0 (iPod; U; CPU like Mac OS X; en) AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A93 Safari/419.3";

	public static final String IPHONE_IOS1_STRING = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3";

	public static final String IPHONE_IOS2_STRING = "Mozilla/5.0 (iPhone; U; CPU iOS 2_0 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/XXXXX Safari/525.20";

	public static final String IPHONE_IOS4_STRING = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";

	public static final String IPHONE_IOS4_0_1_STRING = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0_1 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A306 Safari/6531.22.7";

	public static final String IPHONE_IOS4_1_STRING = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_1 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8B5097d Safari/6531.22.7";

	public static final String IPHONE_IOS4_2_STRING = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2 like Mac OS X; en_us) AppleWebKit/525.18.1 (KHTML, like Gecko)";

	public static final String IPHONE_IOS4_2_1_STRING = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5";

	public static final String IPHONE_IOS4_3_2_STRING = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5";

	public static final String IPHONE_IOS5_STRING = "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";

	public static final String IPHONE_IOS6_STRING = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25";

	public static final String IPHONE_IOS7_STRING = "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53";

	public static final String IPAD_IOS3_2_STRING = "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10";

	public static final String IPAD_IOS3_2_1_STRING = "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B405 Safari/531.21.10";

	public static final String IPAD_IOS4_3_STRING = "Mozilla/5.0 (iPad; U; CPU OS 4_3 like Mac OS X; da-dk) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8F190 Safari/6533.18.5";

	public static final String IPAD_IOS4_3_5_STRING = "Mozilla/5.0 (iPad; U; CPU OS 4_3_5 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8L1 Safari/6533.18.5";

	public static final String IPAD_IOS5_STRING = "Mozilla/5.0 (iPad; CPU OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";

	public static final String IPAD_IOS6_STRING = "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25";

	public static final String IPAD_IOS7_STRING = "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53";

	// Android

	public static final String HTC_DREAM_ANDROID1_5_STRING = "HTC_Dream Mozilla/5.0 (Linux; U; Android 1.5; en-ca; Build/CUPCAKE) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1";

	public static final String HTC_DESIRE_ANDROID2_1_STRING = "Mozilla/5.0 (Linux; U; Android 2.1-update1; de-de; HTC Desire 1.19.161.5 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";

	public static final String HTC_DESIRE_ANDROID2_2_STRING = "Mozilla/5.0 (Linux; U; Android 2.2; nl-nl; Desire_A8181 Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String GOOGLE_NEXUSONE_ANDROID2_1_STRING = "Mozilla/5.0 (Linux; U; Android 2.1; en-us; Nexus One Build/ERD62) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";

	public static final String GOOGLE_NEXUSONE_ANDROID2_2_STRING = "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String GOOGLE_NEXUSS_ANDROID2_3_STRING = "Mozilla/5.0 (Linux; U; Android 2.3; xx-xx; Nexus S Build/GRH41B) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String SAMSUNG_GALAXYNEXUS_ANDROID4_STRING = "Mozilla/5.0 (Linux; U; Android 4.0; xx-xx; Galaxy Nexus Build/IFL10C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	public static final String SAMSUNG_GALAXYS2_GT_I9100_ANDROID2_3_STRING = "Mozilla/5.0 (Linux; U; Android 2.3; xx-xx; GT-I9100 Build/GRH78) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String SAMSUNG_GALAXYS3_GT_I9300_ANDROID4_0_4_STRING = "Mozilla/5.0 (Linux; U; Android 4.0.4; xx-xx; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	public static final String SAMSUNG_GALAXYS4_GT_I9500_ANDROID4_2_STRING = "Mozilla/5.0 (Linux; U; Android 4.2; xx-xx; GT-I9500 Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	public static final String SAMSUNG_GALAXYNOTE2_GT_N7100_ANDROID4_1_STRING = "Mozilla/5.0 (Linux; U; Android 4.1; xx-xx; GT-N7100 Build/JRO03C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	public static final String SAMSUNG_GALAXYTAB_GT_P1000_ANDROID2_2_STRING = "Mozilla/5.0 (Linux; U; Android 2.2; en-gb; GT-P1000 Build/FROYO) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String SAMSUNG_GALAXYTAB8_9_GT_P7310_ANDROID3_1_STRING = "Mozilla/5.0 (Linux; U; Android 3.1; xx-xx; GT-P7310 Build/HMJ37) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13";

	public static final String SAMSUNG_GALAXYTAB10_1V_GT_P7100_ANDROID3_0_STRING = "Mozilla/5.0 (Linux; U; Android 3.0; xx-xx; GT-P7100 Build/HRI83) AppleWebkit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13";

	public static final String SAMSUNG_GALAXYTAB10_1_GT_P7510_ANDROID3_1_STRING = "Mozilla/5.0 (Linux; U; Android 3.1; xx-xx; GT-P7510 Build/HMJ37) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13";

	public static final String MOTOROLA_DROID_ANDROID2_1_STRING = "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; Droid Build/ESE81) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";

	public static final String MOTOROLA_DROID_ANDROID2_2_STRING = "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Droid Build/FRG22D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String MOTOROLA_DROID2_ANDROID2_2_STRING = "Mozilla/5.0 (Linux; U; Android 2.2; en-us; DROID2 GLOBAL Build/S273) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	public static final String MOTOROLA_DROIDX_ANDROID2_1_STRING = "Mozilla/5.0 (Linux; U; Android 2.1-update1; en-us; DROIDX Build/VZW) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17 480X854 motorola DROIDX";

	public static final String MOTOROLA_XOOM_ANDROID3_1_STRING = "Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13";

	// Palm

	public static final String PALM_CENTRO_STRING = "PalmCentro/v0001 Mozilla/4.0 (compatible; MSIE 6.0; Windows 98; PalmSource/Palm-D061; Blazer/4.5) 16;320x320";

	public static final String PALM_PRE_WEBOS1_4_STRING = "Mozilla/5.0 (webOS/1.4.0; U; en-US) AppleWebKit/532.2 (KHTML, like Gecko) Version/1.0 Safari/532.2 Pre/1.1";

	public static final String PALM_PRE2_WEBOS2_1_STRING = "Mozilla/5.0 (webOS/2.1.0; U; xx-xx) AppleWebKit/532.2 (KHTML, like Gecko) Version/1.0 Safari/532.2 Pre/1.2";

	public static final String HP_PRE3_WEBOS2_1_STRING = "Mozilla/5.0 (Linux; webOS/2.1.2; U; xx-xx) AppleWebKit/534.6 (KHTML, like Gecko) webOSBrowser/221.11 Safari/534.6 Pre/3.0";

	public static final String PALM_PIXI_WEBOS1_4_STRING = "Mozilla/5.0 (webOS/1.4.0; U; en-US) AppleWebKit/532.2 (KHTML, like Gecko) Version/1.0 Safari/532.2 Pixi/1.1";

	public static final String HPTOUCHPAD_WEBOS3_STRING = "Mozilla/5.0 (hp-tablet; Linux; hpwOS/3.0.0; U; en-US) AppleWebKit/534.6 (KHTML, like Gecko) wOSBrowser/233.70 Safari/534.6 TouchPad/1.0";

	// BlackBerry

	public static final String BLACKBERRY9700_OS5_STRING = "BlackBerry9700/5.0.0.862 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/331";

	public static final String BLACKBERRY9700_OS6_STRING = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9700; en-US) AppleWebKit/534.8+ (KHTML, like Gecko) Version/6.0.0.546 Mobile Safari/534.8+";

	public static final String BLACKBERRY9800_OS5_STRING = "BlackBerry9800/5.0.0.862 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/331 UNTRUSTED/1.0 3gpp-gba";

	public static final String BLACKBERRY9800_OS6_STRING = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en) AppleWebKit/534.1+(KHTML, Like Gecko) Version/6.0.0.141 Mobile Safari/534.1+";

	public static final String BLACKBERRY9850_OS7_STRING = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9850; en-US) AppleWebKit/534.11+ (KHTML, like Gecko) Version/7.0.0.115 Mobile Safari/534.11+";

	public static final String BLACKBERRY9930_OS6_1_STRING = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9930; xx-xx) AppleWebKit/534.11+ (KHTML, like Gecko) Version/6.1.0.61 Mobile Safari/534.11+";

	public static final String BLACKBERRY_PLAYBOOK_STRING = "Mozilla/5.0 (PlayBook; U; RIM Tablet OS 1.0.0; en-US) AppleWebKit/534.8+(KHTML, like Gecko) Version/0.0.1 Safari/534.8+";

	public static final String BLACKBERRY_TOUCH_OS10_STRING = "Mozilla/5.0 (BB10; Touch) AppleWebKit/537.3+ (KHTML, like Gecko) Version/10.0.9.386 Mobile Safari/537.3+";

	// Kindle

	public static final String KINDLE1_STRING = "Mozilla/4.0 (compatible; Linux 2.6.10) NetFront/3.3 Kindle/1.0 (screen 600x800)";

	public static final String KINDLE2_STRING = "Mozilla/4.0 (compatible; Linux 2.6.22) NetFront/3.4 Kindle/2.0 (screen 600x800)";

	public static final String KINDLE2_5_STRING = "Mozilla/4.0 (compatible; Linux 2.6.22) NetFront/3.4 Kindle/2.5 (screen 600x800; rotate)";

	public static final String KINDLE3_STRING = "Mozilla/5.0 (Linux; U; en-US) AppleWebKit/528.5+ (KHTML, like Gecko, Safari/528.5+) Version/4.0 Kindle/3.0 (screen 600X800; rotate)";

	public static final String KINDLE_FIRE1_SILK_DESKTOP_STRING = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3; en-us; Silk/1.0.22.79_10013310) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16 Silk-Accelerated=<state>";

	public static final String KINDLE_FIRE1_SILK_MOBILE_STRING = "Mozilla/5.0 (Linux; U; Android 2.3.4; en-us; Silk/1.0.22.79_10013310) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 Silk-Accelerated=<state>";

	public static final String KINDLE_FIRE1_ANDROID_WEBVIEW_STRING = "Mozilla/5.0 (Linux; U; Android 2.3.4; en-us; Kindle Fire Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Safari/533.1";

	public static final String KINDLE_FIRE2_SILK_DESKTOP_STRING = "Mozilla/5.0 (Linux; U; <locale>; KFOT Build/IML74K) AppleWebKit/<webkit> (KHTML, like Gecko) Silk/<version> Safari/<safari> Silk-Accelerated=<state>";

	public static final String KINDLE_FIRE2_SILK_MOBILE_STRING = "Mozilla/5.0 (Linux; U; Android 4.0.3; <locale>; KFOT Build/IML74K) AppleWebKit/<webkit> (KHTML, like Gecko) Silk/<version> Mobile Safari/<safari> Silk-Accelerated=<state>";

	public static final String KINDLE_FIRE2_ANDROID_WEBVIEW_STRING = "Mozilla/5.0 (Linux; U; Android 4.0.3; <locale>; KFOT Build/IML74K) AppleWebKit/<webkit> (KHTML, like Gecko) Version/4.0 Safari/<safari>";

	// Firefox OS

	public static final String FIREFOXOS_MOBILE_STRING = "Mozilla/5.0 (Mobile; rv:15.0) Gecko/15.0 Firefox/15.0";

	// Windows

	public static final String ASUS_GALAXY6_WINDOWSPHONE7_STRING = "Mozilla/4.0 (compatible; MSIE 7.0; Windows Phone OS 7.0; Trident/3.1; IEMobile/7.0) Asus;Galaxy6";

	public static final String SAMSUNG_FOCUS_WINDOWSPHONE7_5_STRING = "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0; SAMSUNG; SGH-i917)";

	public static final String NOKIA_LUMIA920_WINDOWSPHONE8_MOBILE_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 920)";

	public static final String NOKIA_LUMIA920_WINDOWSPHONE8_DESKTOP_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0; ARM; Touch; WPDesktop)";

	public static final String WINDOWSXP_IE8_STRING = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)";

	public static final String WINDOWS7_IE9_STRING = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

	public static final String WINDOWS7_IE9_COMPATIBILITY_STRING = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0)";

	public static final String WINDOWS7_IE10_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)";

	public static final String WINDOWS7_IE11_STRING = "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko";

	public static final String WINDOWS8_IE10_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)";

	public static final String WINDOWS8_IE10_TOUCH_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0; Touch)";

	public static final String WINDOWSRT_IE10_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; ARM; Trident/6.0)";

	public static final String WINDOWSRT_IE10_TOUCH_STRING = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; ARM; Trident/6.0; Touch)";

	public static final String WINDOWS8_1_IE_11_STRING = "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko";

	public static final String WINDOWS8_1_IE11_COMPATIBILITY_STRING = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.3; Trident/7.0)";

}
