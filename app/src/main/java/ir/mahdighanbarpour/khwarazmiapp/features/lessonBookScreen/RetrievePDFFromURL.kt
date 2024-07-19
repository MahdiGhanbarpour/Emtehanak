package ir.mahdighanbarpour.khwarazmiapp.features.lessonBookScreen

import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class RetrievePDFFromURL(private val pdfView: PDFView, private val activity: LessonBookActivity) {

    // Get the pdf from the sent url
    suspend fun loadPDFFromUrl(url: String) {
        // Load the pdf from the url
        withContext(Dispatchers.IO) {
            try {
                // Open the connection
                val urlConnection: HttpURLConnection =
                    URL(url).openConnection() as HttpURLConnection

                // Check the response code
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    // Get the input stream
                    val inputStream = BufferedInputStream(urlConnection.inputStream)
                    withContext(Dispatchers.Main) {
                        pdfView.fromStream(inputStream).onError {
                            activity.loadError()
                        }.onLoad {
                            activity.bookLoaded()
                        }.load()
                    }
                }
            } catch (_: IOException) {
                // If an error occurs, an error will be displayed
                activity.loadError()
            }
        }
    }
}