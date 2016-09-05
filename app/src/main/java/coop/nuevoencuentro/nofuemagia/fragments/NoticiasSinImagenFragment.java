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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal;
import coop.nuevoencuentro.nofuemagia.activities.PantallaPrincipal2;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasComunAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import coop.nuevoencuentro.nofuemagia.xml.RSSItems;

//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.TextHttpResponseHandler;
//import cz.msebera.android.httpclient.Header;
//import cz.msebera.android.httpclient.HttpHeaders;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NoticiasSinImagenFragment extends Fragment {

    // public static final String ESPAGINA = "ESPAGINA";
    public static final String QUE_NOTICIA = "QUE_NOTICIA";
    //    private AsyncHttpClient client;
    private SwipeRefreshLayout swipe;
    private RecyclerView recList;
    private NoticiasComunAdapter adapter;

    private String url = null;
    private RequestQueue mRequestQueue;
    private List<RSSItems> itemsImpresa = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof PantallaPrincipal)
            mRequestQueue = ((PantallaPrincipal) getActivity()).GetRequest();
        else if (getActivity() instanceof PantallaPrincipal2)
            mRequestQueue = ((PantallaPrincipal2) getActivity()).GetRequest();
    }

    //NUESTRAS VOCES
    /*private TextHttpResponseHandler handlerVoces = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            throwable.printStackTrace();
            swipe.setRefreshing(false);
            Common.ShowOkMessage(swipe, R.string.error_internet);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                List<RSSItems> items = RSSItems.parse(responseString, false);
                adapter.setItems(items);
                if (recList != null) {
                    recList.setAdapter(adapter);
                    swipe.setRefreshing(false);
                }

            } catch (Exception ex) {
                System.out.println("Error xml = " + ex.getMessage());
                swipe.setRefreshing(false);
                Common.ShowOkMessage(swipe, R.string.error_internet);
            }
        }
    };*/

    /*private AsyncHttpResponseHandler handlerUltimas = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                List<RSSItems> ultimas = RSSItems.parse(new String(responseBody, "ISO-8859-1"), true);
                List<RSSItems> todos = new ArrayList<>();
                todos.addAll(ultimas);
                todos.addAll(itemsImpresa);
                adapter.setItems(todos);
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
                swipe.setRefreshing(false);
                Common.ShowOkMessage(swipe, R.string.error_internet);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
            throwable.printStackTrace();
            swipe.setRefreshing(false);
            Common.ShowOkMessage(swipe, R.string.error_internet);
        }
    };*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nuestras_noticias, container, false);

        Bundle args = getArguments();
        url = args.getString(QUE_NOTICIA);

        String que = args.getString(QUE_NOTICIA);
        assert que != null;

        /*switch (que) {
            case Common.PAGINA_12:
                handler = handlerPagina;
                handler.setCharset("ISO-8859-1");
                break;
            case Common.NUESTAS_VOCES:
                handler = handlerVoces;
                break;
            case Common.COMUNIDAD_BSAS:
                handler = handlerVoces;
                break;
        }*/


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
            itemsImpresa = null;
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
    //PAGINA 12
    /*private AsyncHttpResponseHandler handlerPagina = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                itemsImpresa = RSSItems.parse(new String(responseBody, "ISO-8859-1"), true);
                client.get(getContext(), Common.PAGINA_12_ULTIMAS, handlerUltimas);
            } catch (Exception ex) {
                System.out.println("Error xml = " + ex.getMessage());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            System.out.println(statusCode + " - " + error.getMessage());
        }
    };*/

    private void BuscarNoticias(final String url) {
//        client.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
//        client.get(getContext(), url, handler);

        StringRequest checkAdmin = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                switch (url) {
                    case Common.PAGINA_12:
                        PaginaHandler(response);
                        break;
                    case Common.NUESTAS_VOCES:
                        NuestrasHandler(response);
                        break;
                    case Common.COMUNIDAD_BSAS:
                        NuestrasHandler(response);
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String que;
                if (url.equals(Common.PAGINA_12))
                    que = "Pagina 12";
                else if (url.equals(Common.NUESTAS_VOCES))
                    que = "Nuestras Voces";
                else
                    que = "Comunidad Bs As";

                error.printStackTrace();
                swipe.post(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                });
                recList.setAdapter(new NoticiasComunAdapter(getContext()));
                mRequestQueue.getCache().clear();
                Common.ShowMessage(swipe, getContext().getString(R.string.error_internet) + " " + que);
            }
        });

        mRequestQueue.add(checkAdmin);
    }

    private void NuestrasHandler(String response) {
        List<RSSItems> items = RSSItems.parse(response, false);
        adapter.setItems(items);
        if (recList != null) {
            recList.setAdapter(adapter);
            swipe.setRefreshing(false);
        }
    }

    private void PaginaHandler(String response) {
        itemsImpresa = RSSItems.parse(response, true);
        StringRequest ultimas = new StringRequest(Request.Method.POST, Common.PAGINA_12_ULTIMAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<RSSItems> ultimas = RSSItems.parse(response, true);
                List<RSSItems> todos = new ArrayList<>();
                todos.addAll(ultimas);
                todos.addAll(itemsImpresa);
                adapter.setItems(todos);
                if (recList != null) {
                    recList.setAdapter(adapter);
                    swipe.post(new Runnable() {
                        @Override
                        public void run() {
                            swipe.setRefreshing(false);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(ultimas);
    }

}
