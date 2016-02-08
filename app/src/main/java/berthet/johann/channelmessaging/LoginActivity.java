package berthet.johann.channelmessaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import berthet.johann.channelmessaging.network.CustomResponse;
import berthet.johann.channelmessaging.network.WSRequestHandler;
import berthet.johann.channelmessaging.network.onWSRequestListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, onWSRequestListener {
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        editTextLogin = (EditText) findViewById(R.id.editText);
        editTextPassword = (EditText) findViewById(R.id.editText2);
        buttonValidate = (Button) findViewById(R.id.button);

        buttonValidate.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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


    @Override
    public void onClick(View v) {
        String myLogin = editTextLogin.getText().toString();
        String myPassword = editTextPassword.getText().toString();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", myLogin);
        params.put("password", myPassword);

        WSRequestHandler connectRequest = new WSRequestHandler(params, "http://www.raphaelbischof.fr/messaging/?function=connect");
        connectRequest.setOnWSRequestListener(this);
        connectRequest.execute();
    }

    @Override
    public void onWSRequestCompleted(String response) {
        Gson gson = new Gson();
        CustomResponse toastResponse = gson.fromJson(response, CustomResponse.class);
        Toast.makeText(getApplicationContext(), toastResponse.response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWSRequestError(String exception) {
        Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_SHORT).show();
    }
}