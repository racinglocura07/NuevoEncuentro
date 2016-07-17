package coop.nuevoencuentro.nofuemagia.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.model.Bolsones;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ComprasComunitariasFragment extends Fragment {

    private ProgressBar pbCompras;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_compras_comunitarias, container, false);

        Bolsones ultimo = Bolsones.getLast();
        System.out.println("LINK = " + ultimo.getLink());

        WebView wvCompras = (WebView) v.findViewById(R.id.wv_compras);
        wvCompras.loadUrl(ultimo.getLink());
        wvCompras.requestFocus(View.FOCUS_DOWN);
        wvCompras.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = wvCompras.getSettings();
        webSettings.setJavaScriptEnabled(true);

        pbCompras = (ProgressBar) v.findViewById(R.id.pb_compras);

        return v;
    }

    public void showProgress() {
        pbCompras.setProgress(0);
        pbCompras.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        pbCompras.setProgress(100);
        pbCompras.setVisibility(View.GONE);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ComprasComunitariasFragment.this.hideProgress();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ComprasComunitariasFragment.this.showProgress();
            super.onPageStarted(view, url, favicon);
        }


    }


}
