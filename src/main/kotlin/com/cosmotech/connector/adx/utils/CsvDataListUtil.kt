package com.cosmotech.connector.adx.utils

import com.cosmotech.connector.commons.pojo.CsvData
import java.io.File
import java.util.logging.Logger

class CsvDataListUtil {
  companion object {
    val LOGGER: Logger = Logger.getLogger(CsvDataListUtil::class.simpleName)
    @JvmStatic
    fun addCsv(path: String, datas: MutableMap<String, CsvData>) {
      // println("path: ${path}")
      File(path).walk()
        .filter { file -> println(file.toString())
          file.name.endsWith(".csv") }
        .forEach { file ->
          LOGGER.fine("File: ${file}")
          // To debug file content
          // val data = file.readText(Charsets.UTF_8)
          // LOGGER.info("file content:")
          // LOGGER.info(data)
          var header: String? = file.bufferedReader().readLine()
          if (header == null) {
            LOGGER.warning("Empty file ${file.path}")
          } else {
            // Replace specific Cosmo Tech prefix
            header = header.replace("@","")
            LOGGER.fine("Header line:${header}")
            val headers = if (header != null) header.split(",") else listOf()
            // Filename will be used as table name so in fact it is file witout extension we want here
            val cols: MutableMap<String, String> = headers
              .map { it.removeSurrounding("\"","\"") }
              .map { it.removeSurrounding("\'","\'") }
              .associateWith { "string" }
              .toMutableMap<String, String>()
            val csvData = CsvData(
              fileName = file.nameWithoutExtension,
              headers = cols,
              rows = mutableListOf(mutableListOf()),
              exportDirectory = "",
            )

            LOGGER.fine("Add ${file.path} to ${csvData.toString()}")
            datas.put(file.path, csvData)
          }
        }
      }
    }
}
