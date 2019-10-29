package com.pc.searchapi.util;

/**
 * Created by chitra_chitralekha on 10/29/19.
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SearchApiUtil {

    private static final String API_DEFAULT_CHARSET = "UTF-8";
    private static JSONParser parser = new JSONParser();


    public static JSONObject parseLambdaInputStream(InputStream inputStream) throws IOException, ParseException {

        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(inputStream, API_DEFAULT_CHARSET));

            JSONObject event = (JSONObject) parser.parse(reader);

            return event;

        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                }
        }
    }
}
