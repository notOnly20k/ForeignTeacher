package com.whynuttalk.foreignteacher.ext

import android.content.Context
import android.widget.Toast
import com.whynuttalk.foreignteacher.BaseApp
import com.whynuttalk.foreignteacher.R
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import java.util.concurrent.TimeoutException

/**
 * Created by cz on 3/28/18.
 */

private val logger = LoggerFactory.getLogger("Exceptions")

interface UserDescribableException {
    fun describe(context: Context): CharSequence
}


fun Throwable?.describeInHumanMessage(context: Context): CharSequence {
    return when {
        this is UserDescribableException -> describe(context)
        this is HttpException -> R.string.error_unable_to_connect.toFormattedString(context)
        this is TimeoutException -> R.string.error_timeout.toFormattedString(context)
        else -> {
                this?.printStackTrace()
                R.string.error_unknown.toFormattedString(context)

        }
    }
}

fun Throwable?.toast() {
    logger.e(this) { "Unhandled error" }
    Toast.makeText(BaseApp.instance, describeInHumanMessage(BaseApp.instance), Toast.LENGTH_LONG).show()
}

data class ServerException (val name: String?, override val message: String?) : RuntimeException(name), UserDescribableException {
    private var errorMessageResolved: String? = null

    override fun describe(context: Context): CharSequence {
        if (message.isNullOrBlank().not()) {
            return message!!
        }

        if (errorMessageResolved?.isNotBlank() ?: false) {
            return errorMessageResolved!!
        }

        errorMessageResolved = context.getString(context.resources.getIdentifier("error_$name", "string", context.packageName)) ?: ""
        if (errorMessageResolved!!.isBlank()) {
                printStackTrace()
                return context.getString(R.string.error_unknown)
        }

        return errorMessageResolved!!
    }
}