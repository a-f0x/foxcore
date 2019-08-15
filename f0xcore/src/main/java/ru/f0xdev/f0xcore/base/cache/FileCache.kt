package ru.f0xdev.f0xcore.base.cache

import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileCache(rootCacheDirPath: String, cacheDirName: String?, private val gson: Gson) : Cache {

    private val cacheDir: File by lazy {
        val path = if (cacheDirName != null)
            "$rootCacheDirPath/$cacheDirName"
        else
            rootCacheDirPath
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        dir
    }

    /**
     * сохраняет один объект класса, перезеаписывает если такой уже есть
     * */
    override fun write(any: Any) {
        writeToFile(any::class.java.name, any)
    }

    /**
     * возвращает объект если он есть в кеше
     * */
    override fun <T> read(clazz: Class<T>): T? {
        return readFromFile(clazz.name, clazz)
    }

    /**
     * Удаляет объект из кеша если он там есть
     *
     * */
    override fun <T> clear(clazz: Class<T>) {
        clearFile(clazz.name)
    }

    /**
     *
     * Удаляет все данные из кеша
     * в директории [cacheDir]
     * */
    override fun clearAll() {
        cacheDir.listFiles()?.forEach { file -> file.delete() }
    }


    private fun writeToFile(fileName: String, any: Any) {
        val fileWriter = FileWriter(cacheDir.path + "//" + fileName)
        gson.toJson(any, fileWriter)
        fileWriter.flush()
    }

    private fun clearFile(fileName: String) {
        val file = File(cacheDir, fileName)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun <T> readFromFile(fileName: String, clazz: Class<T>): T? {
        val file = File(cacheDir, fileName)
        return if (file.exists()) {
            try {
                gson.fromJson(FileReader(file), clazz)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}