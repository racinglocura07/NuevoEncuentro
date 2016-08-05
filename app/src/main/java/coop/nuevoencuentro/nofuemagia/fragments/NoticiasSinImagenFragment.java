package coop.nuevoencuentro.nofuemagia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasComunAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.xml.XMLNuestrasVoces;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasSinImagenFragment extends Fragment {

    public static final String ESPAGINA = "ESPAGINA";
    private AsyncHttpClient client;
    private SwipeRefreshLayout swipe;
    private RecyclerView recList;
    private NoticiasComunAdapter adapter;

    private String url = null;
    private AsyncHttpResponseHandler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new AsyncHttpClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nuestras_noticias, container, false);

        if (getArguments().getBoolean(ESPAGINA)) {
            url = Common.PAGINA_12;
            handler = handlerPagina;
            handler.setCharset("ISO-8859-1");
        }
        else {
            url = Common.NUESTAS_VOCES;
            handler = handlerVoces;
        }


        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_noticias);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recList.setAdapter(null);
                swipe.setRefreshing(true);
                BuscarNoticias(url);
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_noticias);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        adapter = new NoticiasComunAdapter(getContext());
        if (adapter.haveUpdate()) {
            recList.setAdapter(null);
            swipe.post(new Runnable() {
                @Override
                public void run() {
                    swipe.setRefreshing(true);
                }
            });
            BuscarNoticias(url);
        }

        return v;
    }

    //NUESTRAS VOCES
    private TextHttpResponseHandler handlerVoces = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            System.out.println(statusCode + " - " + responseString);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            //System.out.println(statusCode + " - " + responseString);

            try {
                List<XMLNuestrasVoces> items = XMLNuestrasVoces.parse(responseString, false);
                adapter.setItems(items);
                if (recList != null) {
                    recList.setAdapter(adapter);
                    swipe.setRefreshing(false);
                }

            } catch (Exception ex) {
                System.out.println("Error xml = " + ex.getMessage());
            }
        }
    };

    //PAGINA 12
    private AsyncHttpResponseHandler handlerPagina = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                List<XMLNuestrasVoces> items = XMLNuestrasVoces.parse(new String(responseBody, "ISO-8859-1"), true);
                adapter.setItems(items);
                if (recList != null) {
                    recList.setAdapter(adapter);
                    swipe.post(new Runnable() {
                        @Override
                        public void run() {
                            swipe.setRefreshing(false);
                        }
                    });
                }

            } catch (Exception ex) {
                System.out.println("Error xml = " + ex.getMessage());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            System.out.println(statusCode + " - " + error.getMessage());
        }
    };

    private void BuscarNoticias(String url) {
        client.get(getContext(), url, handler);
    }

}
