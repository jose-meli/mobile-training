package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.content.Intent;
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

    }

    private void search(){
        if(checkTextSearchItem()){
            Intent i= new Intent(this, ResultActivity.class);
            i.putExtra(ResultActivity.ITEM_SEARCH,txtSearchItem.getText().toString());
            startActivity(i);
        }
    }

    private boolean checkTextSearchItem(){
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
