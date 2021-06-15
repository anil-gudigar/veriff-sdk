package com.veriff.sdk.core.app

import android.content.Context
import android.text.TextUtils
import com.veriff.sdk.core.config.Configuration
import com.veriff.sdk.core.persitance.PersistenceManager
import androidx.collection.ArrayMap
import androidx.annotation.GuardedBy
import java.util.*
import kotlin.collections.ArrayList

/**
 * Veriff app
 *
 * @constructor Create empty Veriff app
 */
class VeriffApp {
    private var name: String? = null
    lateinit var applicationContext: Context
    lateinit var persistenceManager: PersistenceManager

    constructor(context: Context,name:String) {
        context.let {
            this.name = name
            applicationContext = it
            persistenceManager = PersistenceManager(applicationContext)
        }
    }

    /**
     * Returns the unique name of this app.
     *
     * @return String
     */
    fun getName(): String? {
        return name
    }

    /**
     * Get config
     *
     * @return Configuration
     */
    fun getConfig(): Configuration {
        return Configuration(
            persistenceManager.getDeviceID(),
            persistenceManager.getAccessToken(),
            "",
            ""
        )
    }

    companion object {
        private val LOCK = Object()
        val DEFAULT_NAME = "[DEFAULT]"

        @GuardedBy("LOCK")
        var INSTANCES: ArrayMap<String, VeriffApp> = ArrayMap<String, VeriffApp>()

        /**
         * Get instance of Veriff App
         *
         * @return @see VeriffApp
         */
        fun getInstance(): VeriffApp {
            synchronized(LOCK) {
                return INSTANCES.get(DEFAULT_NAME)
                    ?: throw IllegalStateException(
                        "Default Veriff is not initialized."
                                + "Make sure to call "
                                + "Veriff.initialize(Context) first."
                    )
            }
        }

        /**
         * Get instance
         *
         * @param name
         * @return @see VeriffApp
         */
        fun getInstance(name: String): VeriffApp {
            synchronized(LOCK) {
                val veriffApp: VeriffApp? = INSTANCES[name]
                if (veriffApp != null) {
                    return veriffApp
                }
                val availableAppNames: List<String?> = getAllAppNames()
                val availableAppNamesMessage: String
                availableAppNamesMessage = if (availableAppNames.isEmpty()) {
                    ""
                } else {
                    "Available app names: " + TextUtils.join(", ", availableAppNames)
                }
                val errorMessage = String.format(
                    "Veriff with name %s doesn't exist. %s", name, availableAppNamesMessage
                )
                throw java.lang.IllegalStateException(errorMessage)
            }
        }

        /**
         * Get all app names
         *
         * @return List<String?>
         */
        private fun getAllAppNames(): List<String?> {
            val allAppNames: MutableList<String> = ArrayList()
            synchronized(LOCK) {
                for (app in INSTANCES.values) {
                    app.getName()?.let { allAppNames.add(it) }
                }
            }
            Collections.sort(allAppNames)
            return allAppNames
        }

        /**
         * Initialize the SDK
         *
         * @param context
         * @return @see VeriffApp
         */
        fun initialize(context: Context): VeriffApp? {
            synchronized(LOCK) {
                if (INSTANCES.containsKey(DEFAULT_NAME)) {
                    return getInstance()
                }
                return initialize(context, DEFAULT_NAME)
            }
        }

        /**
         * Initialize SDK with custom name
         *
         * @param context
         * @param name
         * @return @see VeriffApp
         */
        fun initialize(
            context: Context, name: String
        ): VeriffApp {
            val veriffApp: VeriffApp
            val applicationContext: Context
            applicationContext = if (context.applicationContext == null) {
                // In shared processes' content providers getApplicationContext() can return null.
                context
            } else {
                context.applicationContext
            }
            synchronized(LOCK) {
                veriffApp = VeriffApp(applicationContext,name)
                INSTANCES.put(name, veriffApp)
            }
            return veriffApp
        }
    }

}

