/**
 * Created by jrzeznicki on 13/03/2023.
 */
object Retrofit {
    private const val retrofitVersion = "2.9.0"
    private const val okhttpVersion = "5.0.0-alpha.11"
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val converterGson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    const val converterScalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"
    const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
}