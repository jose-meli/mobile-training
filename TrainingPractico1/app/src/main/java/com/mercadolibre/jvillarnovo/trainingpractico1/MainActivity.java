package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    public static final String LAST_QUERY="last_query";
    public static final String PREF_FILE="tp1_pref_file";
    private EditText txtSearchItem;
    private Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents(){
        txtSearchItem=(EditText) findViewById(R.id.txtSearch);
        txtSearchItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID== EditorInfo.IME_ACTION_SEARCH ||
                        keyEvent.getAction()==KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    search();
                }
                return false;
            }
        });
        btnSearch=(Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        SharedPreferences settings=getSharedPreferences(PREF_FILE,0);
        txtSearchItem.setText((settings.getString(LAST_QUERY,"")));
    }

    private void search(){
        if(isSearchTextValid()){
            saveLastQuery();
            Intent i= new Intent(this, ResultActivity.class);
            i.putExtra(ResultActivity.ITEM_SEARCH,txtSearchItem.getText().toString());
            startActivity(i);
        }
    }

    private void saveLastQuery() {
        SharedPreferences settings = getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAST_QUERY, getIntent().getStringExtra(txtSearchItem.getText().toString()));
        editor.commit();
    }

    private boolean isSearchTextValid(){
        if(txtSearchItem.getText().toString().isEmpty()){
            txtSearchItem.setError(getString(R.string.error_empty_text));
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
