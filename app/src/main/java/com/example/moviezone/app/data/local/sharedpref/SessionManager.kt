import android.content.Context

class SessionManager(private val context: Context) {

    private val pref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun setLogin(isLoggedIn: Boolean) {
        pref.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean("isLoggedIn", false)
    }

    fun logout() {
        pref.edit().clear().apply()
    }
}