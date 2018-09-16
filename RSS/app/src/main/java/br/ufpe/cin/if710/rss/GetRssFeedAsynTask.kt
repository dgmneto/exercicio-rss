package br.ufpe.cin.if710.rss

import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*

class GetRssFeedAsynTask(val callback: (v: List<ItemRSS>) -> Unit): AsyncTask<String, Void, List<ItemRSS>>() {
    override fun doInBackground(vararg p0: String?): List<ItemRSS> {
        var ins: InputStream? = null
        var rssFeed = ""
        try {
            val url = URL(p0.get(0))
            val conn = url.openConnection() as HttpURLConnection
            ins = conn.inputStream
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count = ins.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                count = ins.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        } finally {
            ins?.close()
        }
        return ParserRSS.parse(rssFeed)
    }

    override fun onPostExecute(result: List<ItemRSS>?) {
        super.onPostExecute(result)
        println("OPSSS$result")
        if (result != null) this.callback(result)
    }
}