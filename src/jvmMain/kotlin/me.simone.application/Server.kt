package me.simone.application

import java.net.Socket
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.PrintWriter
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.KeyManagerFactory
import java.security.KeyStore

fun main() {
    val ks = KeyStore.getInstance("JKS")
    ks.load(java.io.FileInputStream("server.jks"), "password".toCharArray())
    val kmf = KeyManagerFactory.getInstance("SunX509")
    kmf.init(ks, "password".toCharArray())
    val sc = javax.net.ssl.SSLContext.getInstance("TLS")
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
