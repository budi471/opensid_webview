package com.asligresik.opensidwebview

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import java.net.URL

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var URLOPENSID : String
        private lateinit var urlPreference: ListPreference

        companion object{
            private const val DEFAULT_VALUE = ""
        }
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
            setSummary()
        }

        private fun init(){
            URLOPENSID = resources.getString(R.string.key_url_opensid)
            urlPreference = findPreference<ListPreference>(URLOPENSID) as ListPreference
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        private fun setSummary(){
            val sh = preferenceManager.sharedPreferences
            urlPreference.summary = sh.getString(URLOPENSID, DEFAULT_VALUE)
        }

        override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
            if(p1 == URLOPENSID){
                //val index = urlPreference.findIndexOfValue(urlPreference.value)
                urlPreference.summary = p0?.getString(URLOPENSID, DEFAULT_VALUE)
            }
        }
    }
}