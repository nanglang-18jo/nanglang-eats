package com.sparta.nanglangeats.domain.address.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

@Service
public class GeocodingService {

	@Value("${GOOGLE_GEOCODING_API_KEY}")
	private String apiKey;

	public CommonAddress getCoordinates(String address) {
		try {
			String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;

			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuilder response = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();

				// Parse JSON response to get coordinates
				String jsonResponse = response.toString();
				double[] coordinates = parseCoordinates(jsonResponse);

				if (coordinates != null) {
					return new CommonAddress(address, coordinates[0], coordinates[1]);
				} else {
					throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
				}
			} else {
				throw new CustomException(ErrorCode.GEOCODING_API_CALL_FAILED);
			}
		} catch (IOException e) {
			throw new CustomException(ErrorCode.IO_EXCEPTION_OCCURRED);
		}
	}

	private static double[] parseCoordinates(String jsonResponse) {
		JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();

		JsonArray results = json.getAsJsonArray("results");
		if (!results.isEmpty()) {
			JsonObject location = results.get(0).getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");
			double latitude = location.get("lat").getAsDouble();
			double longitude = location.get("lng").getAsDouble();
			return new double[]{latitude, longitude};
		} else {
			throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
		}
	}
}
