package vn.edu.usth.listserver;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Field;
import java.util.ArrayList;

import vn.edu.usth.chatbox.R;

public class SearchActivity extends AppCompatActivity {
    ListView listView;
    SearchView searchView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listserver_activitysearch);

        // Initialize searchView and listView
        searchView = findViewById(R.id.search);
        listView = findViewById(R.id.list);

        // Populate the list with sample data
        arrayList = new ArrayList<>();
        arrayList.add("#My-project");
        arrayList.add("#Extra-size");
        arrayList.add("#BCCF");
        arrayList.add("#Friend-Group");
        arrayList.add("#Class-1");
        arrayList.add("#Club-activity");

        // Setup ArrayAdapter with the custom layout (list_black_text) and TextView (list_content)
        arrayAdapter = new ArrayAdapter<>(this, R.layout.listblacktext, R.id.list_content, arrayList);
        listView.setAdapter(arrayAdapter);

        // Customize the SearchView text color
        int searchTextId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = searchView.findViewById(searchTextId);
        if (searchText != null) {
            searchText.setTextColor(Color.WHITE);  // Set text color to WHITE
            searchText.setHintTextColor(Color.WHITE);  // Optional: Hint color
        }

        // Access the search icon via reflection and change its color
        try {
            Field searchIconField = SearchView.class.getDeclaredField("mSearchButton");
            searchIconField.setAccessible(true);
            ImageView searchIcon = (ImageView) searchIconField.get(searchView);
            if (searchIcon != null) {
                searchIcon.setColorFilter(Color.WHITE);  // Set icon color to WHITE
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Ensure SearchView is editable
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();

        // Set up SearchView query text listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (arrayList.contains(query)) {
                    arrayAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(SearchActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setVisibility(ListView.VISIBLE);
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Set up the toolbar with back button
        Toolbar toolbar = findViewById(R.id.back_bar);
        setSupportActionBar(toolbar);

        // Load the icon and resize it
        Drawable backIcon = getResources().getDrawable(R.drawable.back, null);
        backIcon.setBounds(0, 0, 50, 50); // Set the size here (width, height)

        // Set the resized icon as navigation icon
        toolbar.setNavigationIcon(backIcon);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menubackbutton, menu);
        return true;
    }
}
