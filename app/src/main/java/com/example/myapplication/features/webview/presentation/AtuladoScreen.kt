package com.example.myapplication.features.webview.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AtuladoScreen(
    url: String,
    postData: String?,
    shouldStopBrowsing: (String?) -> Boolean,
    modifier: Modifier
) {
    val webView = remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var navigateBack by remember { mutableStateOf(false) }

    // Control del timeout
    var isLoading by remember { mutableStateOf(true) }
    var hasTimedOut by remember { mutableStateOf(false) }
    var showTimeoutDialog by remember { mutableStateOf(false) }
    var loadStartTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isLoading, loadStartTime) {
        if (isLoading && loadStartTime > 0) {
            delay(15000) // 5 segundos
            if (isLoading) {
                hasTimedOut = true
                showTimeoutDialog = true
                webView.value?.stopLoading()
            }
        }
    }

    // boton de retroceso
    BackHandler(enabled = true) {
    }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            val currentWebView = webView.value
            if (currentWebView != null && currentWebView.canGoBack()) {
                currentWebView.goBack()
            }
        }
        navigateBack = false
    }

    // limmpiar el WebView cuando se destruya el composable
    DisposableEffect(Unit) {
        onDispose {
            webView.value?.destroy()
        }
    }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                            settings.apply {
                                loadWithOverviewMode = true
                                isFocusable = true
                                isFocusableInTouchMode = true
                                useWideViewPort = true
                                javaScriptEnabled = true
                                cacheMode = WebSettings.LOAD_NO_CACHE
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    view.settings.setSupportZoom(false)
                                    isLoading = true
                                    hasTimedOut = false
                                    loadStartTime = System.currentTimeMillis()
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?,
                                ) {
                                    super.onReceivedError(view, request, error)
                                    isLoading = false
                                    println("onReceivedError: ${error?.description}")
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)

                                    if (!hasTimedOut) {
                                        isLoading = false
                                    }

                                    canGoBack = view?.canGoBack() == true
                                    println("onPageFinished: $url")
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                ): Boolean {
                                    return if (shouldStopBrowsing(request?.url.toString())) true
                                    else super.shouldOverrideUrlLoading(view, request)
                                }

                                override fun doUpdateVisitedHistory(
                                    view: WebView?,
                                    url: String?,
                                    isReload: Boolean
                                ) {
                                    super.doUpdateVisitedHistory(view, url, isReload)
                                    canGoBack = view?.canGoBack() == true
                                }
                            }

                            if (postData != null) {
                                postUrl(url, postData.toByteArray(StandardCharsets.UTF_8))
                            } else {
                                loadUrl(url)
                            }
                            webView.value = this
                        }
                    },
                )

                // indicador de carga
                if (isLoading && !hasTimedOut) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Dialog de timeout
                if (showTimeoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showTimeoutDialog = false },
                        title = {
                            Text("Tiempo de espera agotado")
                        },
                        text = {
                            Text("La página está tardando demasiado en cargar. ¿Deseas reintentar?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showTimeoutDialog = false
                                    hasTimedOut = false
                                    isLoading = true
                                    loadStartTime = System.currentTimeMillis()
                                    webView.value?.reload()
                                }
                            ) {
                                Text("Reintentar")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showTimeoutDialog = false
                                    // implementar navegacion hacia atras
                                }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    )
}