package com.veriff.sdk.core.persitance

import android.content.Context
import android.content.SharedPreferences

/**
 * Persistence manager
 *
 * @constructor Create empty Persistence manager
 */
class PersistenceManager {
    private lateinit var preferences: SharedPreferences
    private lateinit var mContext: Context

    constructor(context: Context) {
        context.let {
            mContext = it
            preferences = it.getSharedPreferences(SDKPackageName+mContext.packageName, Context.MODE_PRIVATE)
        }
    }

    /**
     * Get preferences
     *
     * @return @see SharedPreferences
     */
    fun getPreferences(): SharedPreferences {
        mContext.let {
            if (preferences == null) {
                preferences = it.getSharedPreferences(SDKPackageName+mContext.packageName, Context.MODE_PRIVATE)
            }
        }
        return preferences
    }

    companion object {
        const val SDKPackageName = "com.veriff.sdk."
        const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        const val KEY_ACCESS_TOKEN_EXPIRY = "KEY_TOKEN_EXPIRY"
        const val KEY_DEVICE_ID = "KEY_PERSON_ID"
    }

    /**
     * Save refresh token
     *
     * @param token
     */
    fun saveRefreshToken(token:String){
        saveStringToPrefrence(KEY_REFRESH_TOKEN,token)
    }

    /**
     * Save access token
     *
     * @param token
     */
    fun saveAccessToken(token:String){
        saveStringToPrefrence(KEY_ACCESS_TOKEN,token)
    }

    /**
     * Save access tokenexpiry
     *
     * @param expiry
     */
    fun saveAccessTokenexpiry(expiry:String){
        saveStringToPrefrence(KEY_ACCESS_TOKEN_EXPIRY,expiry)
    }

    /**
     * Save device i d
     *
     * @param deviceID
     */
    fun saveDeviceID(deviceID:String){
        saveStringToPrefrence(KEY_DEVICE_ID,deviceID)
    }

    /**
     * Get refresh token
     *
     * @return String
     */
    fun getRefreshToken():String{
        return getStringToPrefrence(KEY_REFRESH_TOKEN)
    }

    /**
     * Get access token
     *
     * @return String
     */
    fun getAccessToken():String{
        return getStringToPrefrence(KEY_ACCESS_TOKEN)
    }

    /**
     * Get access tokenexpiry
     *
     * @return String
     */
    fun getAccessTokenexpiry():String{
        return getStringToPrefrence(KEY_ACCESS_TOKEN_EXPIRY)
    }

    /**
     * Get device i d
     *
     * @return String
     */
    fun getDeviceID():String{
        return  getStringToPrefrence(KEY_DEVICE_ID)
    }

    /**
     * Save string to prefrence
     *
     * @param key
     * @param value
     */
    fun saveStringToPrefrence(key:String,value:String){
        getPreferences().edit().putString(key,value).apply()
    }

    /**
     * Get string to prefrence
     *
     * @param key
     * @return String
     */
    fun getStringToPrefrence(key:String):String{
        return getPreferences().getString(key,"")?:""
    }

    /**
     * Clear SharedPreferences
     *
     */
    fun clear(){
        getPreferences().edit().clear().commit()
    }

}