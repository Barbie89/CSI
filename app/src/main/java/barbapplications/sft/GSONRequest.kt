package barbapplications.sft

import android.util.Log
import au.com.hrbandroid.tools.JsonConverter
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.error.ParseError
import com.android.volley.error.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.nio.charset.Charset

class GSONRequest<T> : Request<T> {
    private val inputType: Type?
    private val outputType: Type
    private val httpHeaders: MutableMap<String, String>?
    private val listener: Response.Listener<T>
    private var inputObject: Any? = null
    private var mediaType: String? = null

    constructor(url: String, outputType: Type, headers: MutableMap<String, String>?,
                listener: Response.Listener<T>, errorListener: Response.ErrorListener) : super(
            Request.Method.GET, url, errorListener) {
        this.outputType = outputType
        this.inputType = null
        this.httpHeaders = headers
        this.listener = listener
    }

    constructor(method: Int, url: String, inputType: Type, outputType: Type, inputObject: Any,
                mediaType: String, headers: MutableMap<String, String>?,
                listener: Response.Listener<T>, errorListener: Response.ErrorListener) : super(
            method, url, errorListener) {
        this.outputType = outputType
        this.inputType = inputType
        this.inputObject = inputObject
        this.mediaType = mediaType
        this.httpHeaders = headers
        this.listener = listener
    }

    fun noRetry() {
        retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }

    override fun getHeaders(): MutableMap<String, String> = httpHeaders ?: super.getHeaders()
    override fun getBody(): ByteArray? {
        val jsonString = JsonConverter().toJson(inputObject, inputType)
        return jsonString?.toByteArray(charset(PROTOCOL_CHARSET)) ?: throw AuthFailureError()
    }

    override fun getBodyContentType(): String = "application/json; charset=utf-8"
    override fun parseNetworkResponse(networkResponse: NetworkResponse): Response<T>? {
        return try {
            val json = String(networkResponse.data,
                    Charset.forName(HttpHeaderParser.parseCharset(networkResponse.headers)))
            val parseObject: T? = JsonConverter().fromJson(json, outputType)
            when {
                networkResponse.statusCode == HttpURLConnection.HTTP_NO_CONTENT -> {
                    Log.d("Status Code: No Content",
                            "Network status code = ${networkResponse.statusCode}")
                    Response.error<T>(VolleyError(networkResponse))
                }

                networkResponse.statusCode == HttpURLConnection.HTTP_OK -> {
                    Log.d("Status Code: OK", "Network status code = ${networkResponse.statusCode}")
                    Response.success(parseObject,
                            HttpHeaderParser.parseCacheHeaders(networkResponse))
                }

                else -> Response.error(VolleyError())
            }
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: T) {
        Log.i("TAG", response.toString())
        listener.onResponse(response)
    }

    companion object {
        private val PROTOCOL_CHARSET: String = "utf-8"
    }
}