package com.pratik.apputils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class AppUtils {

	private Process p1;

	/**
	 * Download and load the json.
	 */

	public String loadJSON(String someURL) {

		String json = null;
		HttpClient mHttpClient = new DefaultHttpClient();
		HttpGet mHttpGet = new HttpGet(someURL);

		try {

			HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
			StatusLine statusline = mHttpResponse.getStatusLine();
			int statusCode = statusline.getStatusCode();
			if (statusCode != 200) {

				return null;

			}
			InputStream jsonStream = mHttpResponse.getEntity().getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					jsonStream));

			StringBuilder builder = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {

				builder.append(line);

			}

			json = builder.toString();

		} catch (IOException ex) {

			ex.printStackTrace();

			return null;
		}
		mHttpClient.getConnectionManager().shutdown();
		return json;

	}

	/**
	 * Check Internet connectivity by ping. Check if the data packets are being
	 * sent and received. In this case, just trying to connect to www.google.com
	 */
	public Boolean isOnline() {
		try {
			p1 = java.lang.Runtime.getRuntime()
					.exec("ping -c 1 www.google.com");
			int returnVal = p1.waitFor();
			boolean reachable = (returnVal == 0);
			if (reachable) {

				System.out.println("Internet access");
				return reachable;
			} else {

				System.out.println("No Internet access");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			p1.destroy();
		}
		return false;
	}

}
