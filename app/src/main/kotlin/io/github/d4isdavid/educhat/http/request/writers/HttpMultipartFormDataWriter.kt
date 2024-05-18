package io.github.d4isdavid.educhat.http.request.writers

import io.github.d4isdavid.educhat.http.request.HttpFormDataPart
import java.net.HttpURLConnection

val boundaryChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
const val BOUNDARY_LENGTH = 40 // max is 70
const val CHUNK_SIZE = 8192 // default BufferedWriter chunk size

@Suppress("unused")
fun HttpURLConnection.writeMultipartFormData(
    data: Array<HttpFormDataPart>,
    customBoundary: String?
) {
    val boundary = customBoundary ?: (1..BOUNDARY_LENGTH)
        .map { boundaryChars.random() }
        .joinToString("")
    setRequestProperty("Content-Type", "multipart/form-data; boundary=\"$boundary\"")

    doOutput = true
    setChunkedStreamingMode(CHUNK_SIZE)

    outputStream.writer().buffered(CHUNK_SIZE).use { writer ->
        data.forEach {
            writer.write("--$boundary\n")
            writer.write("Content-Disposition: form-data; name=${it.name}")
            it.filename?.let { filename -> writer.write("; filename=${filename}") }
            writer.write("\n")
            it.contentType?.let { contentType -> writer.write("Content-Type: $contentType\n") }
            writer.write(it.content)
            writer.write("\n")
        }

        writer.write("--$boundary--")
    }
}
