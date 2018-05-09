package barbapplications.sft

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class RequestFactory private constructor() {

    companion object {
        private var requestQueue: RequestQueue? = null

        fun getRequestQueue(callerContext: Context): RequestQueue? {
            if (requestQueue == null) requestQueue = Volley.newRequestQueue(callerContext)
            return requestQueue
        }
    }
}