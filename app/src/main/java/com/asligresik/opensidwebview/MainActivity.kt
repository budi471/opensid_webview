package com.asligresik.opensidwebview

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.preference.PreferenceManager
import com.asligresik.opensidwebview.databinding.ActivityMainBinding
import java.net.URISyntaxException
import java.util.*

class MainActivity : AppCompatActivity() {
    private val MODE_PRIVATE = 0
    companion object{
        private const val DEFAULT_VALUE = ""
    }
    private lateinit var binding:  ActivityMainBinding
    private lateinit var webUrl: String
    private lateinit var progressBar: ProgressBar
    private lateinit var wvMain: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sh = PreferenceManager.getDefaultSharedPreferences(this)
        webUrl = sh.getString(resources.getString(R.string.key_url_opensid), DEFAULT_VALUE)!!
        if(webUrl.equals("")){
            startActivity(Intent(this,SettingsActivity::class.java))
        }else{
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            progressBar = binding.progressBar
            wvMain = binding.wvMain


            initWebview()
        }
    }

    private fun initWebview(){
        wvMain.settings.javaScriptEnabled = true
        wvMain.settings.domStorageEnabled = true
        wvMain.settings.allowFileAccess = true
        wvMain.settings.allowContentAccess = true
        wvMain.settings.loadsImagesAutomatically = true
        wvMain.webViewClient = CustomWebViewClient()
        wvMain.loadUrl(webUrl)
    }

    override fun onResume() {
        super.onResume()
        initWebview()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvMain.canGoBack()){
            wvMain.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /*override fun onBackPressed() {        
        if(wvMain.canGoBack()){
            wvMain.goBack()
        }else{
            super.onBackPressed()
        }        
    }*/

    inner class CustomWebViewClient() : WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progressBar.visibility = View.VISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val uri = request?.url
            if(!uri.toString().startsWith(webUrl)){
                try {
                    val intent = Intent.parseUri(uri?.toString(), Intent.URI_INTENT_SCHEME)
                    if(intent.resolveActivity(packageManager) != null){
                        startActivity(intent)
                    }
                }catch (e: URISyntaxException){

                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            progressBar.visibility = View.GONE
            super.onPageFinished(view, url)
        }
    }
}