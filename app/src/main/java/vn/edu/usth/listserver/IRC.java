package vn.edu.usth.listserver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import vn.edu.usth.chatbox.R;
import vn.edu.usth.createserver.MainActivity;
import vn.edu.usth.loginsignup.MainActivity2;

public class IRC extends AppCompatActivity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listserver);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String username = getIntent().getStringExtra("username"); //add user name (new change)





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.search_bar){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
        }
        if(id==R.id.logout_button){
            Toast.makeText(this,"Log out",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);;
        }
        if(id==R.id.add_server){
            Toast.makeText(this,"Create a new server",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);  //MainActivity in Createserver
            startActivity(intent);
            return true;
        }
        if(id==R.id.change_settings){
            Toast.makeText(this,"Change setting",Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    public IRC(){
        super();
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i("OnStart","start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("OnResume", "resume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPause", "pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("OnStop", "stop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("OnDestroy", "destroy");
    }

}