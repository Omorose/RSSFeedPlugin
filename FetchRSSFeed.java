/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/

package org.apache.cordova;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FetchRSSFeed extends CordovaPlugin{
	private String finalFeed = "";
	private String rssImgLocation = "";
	private String titleTag, descriptionTag, timeTag, linkTag;
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> content = new ArrayList<String>();
	private ArrayList<String> time = new ArrayList<String>();
	private ArrayList<String> links = new ArrayList<String>();
	
	@Override
	public boolean execute(String action, final JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		if (action.equals("fetchFeed")) {
			 cordova.getThreadPool().execute(new Runnable(){
				 String feedURL = "";
				
				@Override
				public void run() {
					try {
						feedURL = args.getString(0);
						titleTag = args.getString(1);
						descriptionTag = args.getString(2);
						timeTag = args.getString(3);
						linkTag = args.getString(4);
						rssImgLocation = args.getString(5);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					fetchFeed(feedURL);
					this.callbackResponse(finalFeed, callbackContext);					
				}
				private void callbackResponse(String feed,
						CallbackContext callbackContext) {
					if (feed != null && feed.length() > 0) { 
			            callbackContext.success(feed);
			        } else {
			            callbackContext.error("Expected one non-empty string argument.");
			        }
					
				}
				
			});
			
			return true;
		}
		return false;
	}

	private void fetchFeed(String url){ 
		titles.clear();
		content.clear();
		time.clear();
		URL rssFeedURL;
		DocumentBuilderFactory newInstance;
		DocumentBuilder newDocumentBuilder;
		InputStream inputStream;
		InputSource inputSource;
		Document document;
		NodeList titleNodeList, contentNodeList, timeNodeList, linkNodeList;
		Element titleElement, contentElement, timeElement, linkElement;
		try {

			rssFeedURL = new URL(url);
			inputStream = rssFeedURL.openStream();

			newInstance = DocumentBuilderFactory.newInstance();
			newDocumentBuilder = newInstance.newDocumentBuilder();
			inputSource = new InputSource(inputStream);
			document = newDocumentBuilder.parse(inputSource);
	
			titleNodeList = document.getElementsByTagName(titleTag);
			contentNodeList = document.getElementsByTagName(descriptionTag);
			timeNodeList = document.getElementsByTagName(timeTag);
			linkNodeList = document.getElementsByTagName(linkTag);

			/*
			 * depending on the feed and when the actual news start i will have to be adjusted
			 *to get a correct result
			 */
			for (int i = 0; i < titleNodeList.getLength(); i++) {
				titleElement = (Element) titleNodeList.item(i);
				contentElement = (Element) contentNodeList.item(i);
				timeElement = (Element) timeNodeList.item(i);
				linkElement = (Element) linkNodeList.item(i);
				
				titles.add(titleElement.getTextContent());
				content.add(contentElement.getTextContent());
				time.add(timeElement.getTextContent());
				links.add(linkElement.getTextContent());
				
			}
			
			inputStream.close();
			formatFeed();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	private void formatFeed(){
		finalFeed = "";
		for(int i = 0; i < titles.size(); i++){
			finalFeed += "<div class=\"feedTitle\" onclick=\"window.open(\'" + links.get(i) + "\', \'_blank\')\"><img src=\"" + rssImgLocation + "\" alt=\"rss logga\"/>" + titles.get(i) + "<br><span class=\"feedContent\">"+ time.get(i) +"</span><br><span class=\"feedContent\">" + content.get(i) + "</span></div>";
		}
	}

}