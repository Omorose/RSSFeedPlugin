RSSFeedPlugin
=============
Phonegap/Cordova plugin for Android, developed with cordova 2.5.0
minimum sdk = 8, targeted sdk = 17

To use this plugin add following to your android phonegap project:

In /res/xml/config.xml
<plugin name="FetchRSSFeed" value="org.apache.cordova.FetchRSSFeed"/>
------------------------

In /src/org/apache/cordova
Copy paste into package the FetchRssFeed.java file

Depending on the rss feed variable "i" in lines 122-126 may need to be adjusted for the individual feed
due to that some tags may be iniated earlier than others
------------------------

In /assets/www
Copy paste into directory the FetchRssFeed.js, the news.html and the rss_img.png file


The news.html file comes with predefined CSS that can be customized as per your wish
