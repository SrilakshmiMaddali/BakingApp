package sm.com.bakingapp.util;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static String s_baseUrl = "https://d17h27t6h515a5.cloudfront.net";


    private static Retrofit s_retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (s_retrofit == null) {
            s_retrofit = new Retrofit.Builder()
                    .baseUrl(s_baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return s_retrofit;
    }
}
