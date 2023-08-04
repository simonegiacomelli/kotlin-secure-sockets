package on_th_fly

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.security.KeyFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import javax.net.ssl.*

fun main() {
    val keyBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("data/private_key.der"))
    val spec = PKCS8EncodedKeySpec(keyBytes)
    val kf = KeyFactory.getInstance("RSA")
    val privateKey = kf.generatePrivate(spec)

    val cf = CertificateFactory.getInstance("X.509")
    val cert = cf.generateCertificate(java.io.FileInputStream("data/server.crt"))

    val ks = KeyStore.getInstance("JKS")
    ks.load(null, null)
    ks.setKeyEntry("server", privateKey, "password".toCharArray(), arrayOf(cert))

    val kmf = KeyManagerFactory.getInstance("SunX509")
    kmf.init(ks, "password".toCharArray())

    val sc = SSLContext.getInstance("TLS")
    sc.init(kmf.keyManagers, null, null)

    val ssf = sc.serverSocketFactory
    val ss = ssf.createServerSocket(1234)
    val s = ss.accept()
    val input = BufferedReader(InputStreamReader(s.getInputStream()))
    val output = PrintWriter(s.getOutputStream(), true)
    val receivedMessage = input.readLine()
    if (receivedMessage == "hello") {
        output.println("world")
    }
    s.close()
}
