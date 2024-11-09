package vn.edu.usth.createserver;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import vn.edu.usth.chatbox.ChatboxActivity;
import vn.edu.usth.chatbox.R;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_1 extends Fragment {

    private EditText usernameEditText;
    private Button saveButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_1 newInstance(String param1, String param2) {
        Fragment_1 fragment = new Fragment_1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.createserver_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.back_bar);

        // Load the icon and resize it
        Drawable backIcon = getResources().getDrawable(R.drawable.back);
        backIcon.setBounds(0, 0, 50, 50); // Set the size here (width, height)

        // Set the resized icon
        toolbar.setNavigationIcon(backIcon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.spinner);

        // Use custom spinner item layout for black text on white background
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.network, R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_item); // Set custom layout for dropdown items

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedServer = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Selected server: " + selectedServer, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle if needed
            }
        });



        // Initialize views from the fragment layout
        usernameEditText = view.findViewById(R.id.username);
        saveButton = view.findViewById(R.id.savetouser);

        // Set an OnClickListener for the Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the username from EditText
                String username = usernameEditText.getText().toString();

                if (!username.isEmpty()) {
                    // Show a Toast message confirming the username was saved
                    Toast.makeText(getActivity(), "Username saved: " + username, Toast.LENGTH_SHORT).show();

                    // Navigate to the IRC activity
                    Intent intent = new Intent(getActivity(), ChatboxActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    // Show an error if the username is empty
                    Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}