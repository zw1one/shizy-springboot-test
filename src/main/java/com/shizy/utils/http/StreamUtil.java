package com.shizy.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StreamUtil {
    public static String read(InputStream inputStream) throws IOException {

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            byte[] bytes = new byte[1024];
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        }
    }
}
