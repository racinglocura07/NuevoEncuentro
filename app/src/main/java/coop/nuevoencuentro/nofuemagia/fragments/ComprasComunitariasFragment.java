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

import com.loopj.android.http.AsyncHttpClient;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.model.Bolsones;

/**
 * Created by jlionti on 10/06/2016. No Fue Magia
 */
public class ComprasComunitariasFragment extends Fragment {

    private ProgressBar pbCompras;
    private WebView wvCompras;
    private ProgressBar pbNavegador;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_compras_comunitarias, container, false);

        wvCompras = (WebView) v.findViewById(R.id.wv_compras);
        wvCompras.setWebViewClient(new MyWebViewClient());
        wvCompras.requestFocus(View.FOCUS_DOWN);

        WebSettings webSettings = wvCompras.getSettings();
        webSettings.setJavaScriptEnabled(true);

        pbCompras = (ProgressBar) v.findViewById(R.id.pb_compras);
        pbNavegador = (ProgressBar) v.findViewById(R.id.pb_navegador);

        Bolsones ultimo = Bolsones.getLast();
        if (ultimo == null) {
            AsyncHttpClient client = new AsyncHttpClient();
            Common.SincronizarBolsones(client, this);
            return v;
        }
        pbCompras.setVisibility(View.GONE);
        pbNavegador.setVisibility(View.VISIBLE);

        wvCompras.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                getActivity().setTitle("Cargando...");
                pbNavegador.setProgress(progress);

                if (progress == 100)
                    getActivity().setTitle(R.string.compras_comunitarias);
            }
        });

        wvCompras.loadUrl(ultimo.getLink());
        return v;
    }

    public void recargar() {
        Bolsones ultimo = Bolsones.getLast();
        wvCompras.loadUrl(ultimo.getLink());
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pbNavegador.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pbNavegador.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }


    }


}
