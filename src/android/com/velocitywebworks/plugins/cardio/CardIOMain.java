/**
 * CardIOPlugin.js
 * card.io phonegap plugin
 * @Copyright 2013 Cubet Technologies http://www.cubettechnologies.com
 * @author Robin <robin@cubettech.com>
 * @Since 28 June, 2013
 */

package com.velocitywebworks.plugins.cardio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CardIOMain extends Activity {

	private static int MY_SCAN_REQUEST_CODE=10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

		// TODO Auto-generated method stub
		Intent scanIntent = new Intent(CardIOMain.this, CardIOActivity.class);

	    // required for authentication with card.io
	    scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, CardIOPlugin.cardIOAPIKey);

	    // customize these values to suit your needs.
	    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, CardIOPlugin.expiry);
	    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, CardIOPlugin.cvv); 
	    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, CardIOPlugin.zip); 
	    scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, CardIOPlugin.confirm);
	    scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, CardIOPlugin.suppressManual);
	    scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, CardIOPlugin.showLogo);

	    // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
	    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);		
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == MY_SCAN_REQUEST_CODE) {
	    	
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
				
	            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

	            if(scanResult != null) {
	            	// Never log a raw card number. Avoid displaying it, but if necessary use redacted number	            
	            	JSONArray carddata = new JSONArray();
	            	JSONObject j = new JSONObject();

	            	try {
	            		j.put("card_number",scanResult.cardNumber);
	            		j.put("redacted_card_number", scanResult.getFormattedCardNumber());
	            		if (scanResult.isExpiryValid()) {
	            			j.put("expiry_month",scanResult.expiryMonth);
	            			j.put("expiry_year",scanResult.expiryYear);
	            		}

	            		if (scanResult.cvv != null) {
	            			j.put("cvv",scanResult.cvv);

	            		}

	            		if (scanResult.zip != null) {
	            			j.put("zip",scanResult.zip);
	            		}

	            	} catch (JSONException e) {
						// TODO Auto-generated catch block
	            		e.printStackTrace();
	            	}

	            	carddata.put(j);
	            	CardIOPlugin.mCreditcardNumber = carddata;
	            }
	        }

	    }
	    CardIOMain.this.finish();
	}
	
}
