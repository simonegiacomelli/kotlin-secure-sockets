package on_th_fly
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

fun main() {
    val cf = CertificateFactory.getInstance("X.509")
    val serverCert = cf.generateCertificate(java.io.FileInputStream("data/server.der")) as X509Certificate

    val tm: TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            if (chain[0] != serverCert) throw CertificateException("Server certificate not trusted")
        }
    }

    val sc = SSLContext.getInstance("TLS")
    sc.init(null, arrayOf(tm), null)
    val sf = sc.socketFactory
    val s = sf.createSocket("localhost", 1234)
    val output = PrintWriter(s.getOutputStream(), true)
    val input = BufferedReader(InputStreamReader(s.getInputStream()))
    output.println("hello")
    val response = input.readLine()
    println(response) // Outputs "world"
    s.close()
}
