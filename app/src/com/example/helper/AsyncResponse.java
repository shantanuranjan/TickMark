package com.example.helper;

import org.json.JSONObject;

public interface AsyncResponse {
	void processFinish(JSONObject output);
}