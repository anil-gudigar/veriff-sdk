package com.veriff.sdk.core.config

/**
 * Configuration
 *
 * @constructor
 *
 * @param _deviceID
 * @param _accessToken
 * @param _client_ID
 * @param _client_Secret
 */
class Configuration(_deviceID: String,_accessToken: String,_client_ID: String,_client_Secret: String) {
    var deviceID: String = _deviceID
    var accessToken: String = _accessToken
    var client_ID: String = _client_ID
    var client_Secret: String = _client_Secret

    /**
     * Set client
     *
     * @param _client_ID
     * @param _client_Secret
     */
    fun setClient(_client_ID: String,_client_Secret: String){
        client_ID = _client_ID
        client_Secret = _client_Secret
    }

    /**
     * Set user session
     *
     * @param _deviceID
     * @param _accessToken
     */
    fun setUserSession(_deviceID: String,_accessToken: String) {
        deviceID = _deviceID
        accessToken = _accessToken
    }
}