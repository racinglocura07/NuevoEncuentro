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
import com.loopj.android.http.TextHttpResponseHandler;

import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import coop.nuevoencuentro.nofuemagia.R;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasAdapter;
import coop.nuevoencuentro.nofuemagia.adapters.NoticiasComunAdapter;
import coop.nuevoencuentro.nofuemagia.helper.Common;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Tano on 31/07/2016.
 * Nuevo Encuentro
 * No Fue Magia
 */
public class NuestrasVocesFragment extends Fragment {

    public static final CharSequence TITLE = "Nuestras Voces";
    private SwipeRefreshLayout swipe;
    private RecyclerView recList;
    private NoticiasComunAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nuestras_noticias, container, false);

        TextView tvConstruccion = (TextView) v.findViewById(R.id.tv_construccion);
        tvConstruccion.setVisibility(View.VISIBLE);

        swipe = (SwipeRefreshLayout) v.findViewById(R.id.srl_noticias);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BuscarNoticias();
            }
        });

        recList = (RecyclerView) v.findViewById(R.id.list_noticias);
        recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        adapter = new NoticiasComunAdapter(getContext());
        BuscarNoticias();

        return v;
    }

    private void BuscarNoticias() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getContext(), Common.NUESTAS_VOCES, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(statusCode + " - " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(statusCode + " - " + responseString);


                try {
                    XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = pullParserFactory.newPullParser();
                } catch (XmlPullParserException e) {

                    e.printStackTrace();
                }

            }
        });
    }

}
