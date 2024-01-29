package com.krishnaZyala.faceRecognition.lib

import android.util.Log

object LOG {
    private val debug get():Boolean = true
    private fun stackTrace(index: Int): StackTraceElement = Thread.currentThread().stackTrace[index]
    private fun msg(args: Array<out Any?>): String = buildString { append("\t");args.forEach { append("\n$it") } }
    private val TAG
        get() = try {
            """${stackTrace(6).lineNumber} ${stackTrace(6).fileName} | ${
                stackTrace(6).className.substringAfter("$").substringBefore("$").substringBeforeLast("Kt")
            }.${stackTrace(6).methodName}()"""
        } catch (t: Throwable) {
            t.message
        }

    fun d(vararg args: Any?) = if (!debug) null else Log.d(TAG, msg(args))
    fun e(throwable: Throwable?, vararg args: Any?) = if (!debug) null
    else if (throwable != null) Log.e(TAG, msg(args), throwable) else Log.e(TAG, msg(args))
    fun i(vararg args: Any?) = if (!debug) null else Log.i(TAG, msg(args))

}
