package com.exemple.android.crud_exercicio;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.os.AsyncTask;;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvContants;
    OkHttpClient client;
    ObjectMapper mapper;

    public class ContactsFetcherTask extends AsyncTask<URL, Void, Contact[]> {

        @Override
        protected Contact[] doInBackground(URL... params) {
            URL searchURL = params[0];
            Request request = new Request.Builder().url(searchURL).build();

            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();

                Contact[] contacts = mapper.readValue(jsonString, Contact[].class);

                return contacts;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Contact[] contacts) {
          if (contacts != null) {
                ContactListAdapter adapter = new ContactListAdapter(contacts);
                rvContants.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        rvContants = (RecyclerView) findViewById(R.id.rvContants);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvContants.setLayoutManager(layoutManager);
        rvContants.setHasFixedSize(true);

        client = new OkHttpClient();
        mapper = new ObjectMapper();

        fetchContacts();
    }

    public void onClickFloatingButton(View view) {
        Snackbar.make(view, "Not implemented yet", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    private void fetchContacts() {
        /*
         * Ao invés de fazer o parse da URL aqui, foi criando uma nossa classe, WebServiceUtils, que
         * ajuda a gerar URL compatíveis com o webservice.
         */
        URL url = WebServiceUtil.WEBSERVICE_URL;

        ContactsFetcherTask task = new ContactsFetcherTask();
        task.execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
