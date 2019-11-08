package com.future.pms.util

import android.content.Context
import com.future.pms.model.oauth.Token
import com.future.pms.util.Constants.Companion.AUTHENTCATION
import com.future.pms.util.Constants.Companion.TOKEN
import com.google.gson.Gson
import java.util.*

object Authentication {

    private fun put(context: Context, obj: Token): Boolean {
        val preferences = context.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        return editor.putString(TOKEN, Gson().toJson(obj)).commit()
    }

    fun get(context: Context): Token? {
        val preferences = context.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)
        val json = preferences.getString(TOKEN, null)
        if(json != null) return Gson().fromJson(json, Token::class.java)
        return null
    }

    fun save(context: Context, obj: Token, email: String): Boolean {
        val calendar = GregorianCalendar.getInstance()
        var expiresIn: Long = calendar.time.time
        expiresIn += obj.expires_in * 1000
        obj.expires_in = expiresIn
        obj.email = email

        return put(context, obj)
    }

    fun isExpired(context: Context): Boolean {
        val token = get(context)
        if(token != null) {
            val calendar = GregorianCalendar.getInstance()
            val currentTime = calendar.time.time
            val expiresIn = token.expires_in
            if (expiresIn == 0L) throw WithoutAuthenticatedException()
            return currentTime > expiresIn
        } else {
            throw WithoutAuthenticatedException()
        }
    }

    fun isAuthenticated(context: Context): Boolean = !isExpired(context)

    class WithoutAuthenticatedException : Exception()

    fun getRefresh(context: Context): String {
        val token = get(context)
        if(token != null){
            return token.refresh_token
        }else{
            throw WithoutAuthenticatedException()
        }
    }

    fun delete(context: Context) {
        val preferences = context.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        println(TOKEN + preferences.all[TOKEN])
        editor.remove(TOKEN).apply()
        println(preferences.all[TOKEN])
    }
}