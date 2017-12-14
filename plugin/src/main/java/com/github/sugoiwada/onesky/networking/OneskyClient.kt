package com.github.sugoiwada.onesky.networking

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.rx.rx_string
import com.github.kittinunf.result.Result
import com.github.sugoiwada.onesky.entity.LanguagesResponse
import com.github.sugoiwada.onesky.entity.ListFileResponse
import io.reactivex.Single
import java.io.File
import java.security.MessageDigest

class OneskyClient(private val apiKey: String, private val apiSecret: String, projectId: Int) {
    private val endpoint = "https://platform.api.onesky.io"
    private val version = 1

    init {
        FuelManager.instance.basePath = "$endpoint/$version/projects/$projectId"
    }

    fun download(locale: String, sourceFileName: String, saveTo: File): Single<Result<String, FuelError>> {
        val params = authParams()
        params.add("source_file_name" to sourceFileName)
        params.add("locale" to locale)

        return "/translations".httpDownload(params, saveTo).rx_string()
    }

    fun listFile() = "/files".httpGet(authParams()).rx_object<ListFileResponse>()

    fun languages() = "/languages".httpGet(authParams()).rx_object<LanguagesResponse>()

    private fun authParams(): MutableList<Pair<String, String>> {
        val md = MessageDigest.getInstance("MD5")
        val timestamp = (System.currentTimeMillis() / 1000L).toString()
        val devHash = md.digest((timestamp + apiSecret).toByteArray()).toHex()

        return mutableListOf(
                "api_key" to apiKey,
                "dev_hash" to devHash,
                "timestamp" to timestamp
        )
    }
}

private fun String.httpDownload(parameter: List<Pair<String, Any?>>? = null, file: File): Request {
    return Fuel.download(this, parameter).destination { _, _ -> file }
}

private fun ByteArray.toHex(): String = joinToString("") { String.format("%02X", it).toLowerCase() }