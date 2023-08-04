import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import java.security.KeyStore

fun main() {
    val ts = KeyStore.getInstance("JKS")
    ts.load(java.io.FileInputStream("clientTruststore.jks"), "password".toCharArray())
    val tmf = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ts)
    val sc = javax.net.ssl.SSLContext.getInstance("TLS")
    sc.init(null, tmf.trustManagers, null)
    val sf = sc.socketFactory
    val s = sf.createSocket("localhost", 1234)
    val output = PrintWriter(s.getOutputStream(), true)
    val input = BufferedReader(InputStreamReader(s.getInputStream()))
    output.println("hello")
    val response = input.readLine()
    println(response) // Outputs "world"
    s.close()
}
