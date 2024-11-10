package vn.edu.usth.chatbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatboxActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    private EditText messageEditText;
    private Button sendButton;
    private MyBot myBot;
    private ExecutorService botExecutorService = Executors.newSingleThreadExecutor();
    private String username = "Anonymous"; // Default username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_chat);

        // Initialize RecyclerView
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize message list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setAdapter(chatAdapter);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Retrieve username from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        }

        String selectedServer = intent != null ? intent.getStringExtra("selectedServer") : null;

        // Initialize MyBot instance
        if (myBot == null) {
            myBot = selectedServer != null
                    ? new MyBot(this, selectedServer, "#YourChannel", username)
                    : new MyBot(this, "irc.freenode.net", "#YourChannel", username);

            // Attempt to connect to the bot in a background thread
            botExecutorService.execute(() -> {
                try {
                    myBot.connect();
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(ChatboxActivity.this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        }

        // Prevent the Enter key from sending a message
        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true; // Consume the Enter key event to prevent new message sending
            }
            return false;
        });

        // Set up the send button click listener
        sendButton.setOnClickListener(v -> sendMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myBot != null && !myBot.isConnected()) {
            // Ensure the bot is connected when the activity resumes
            botExecutorService.execute(() -> {
                try {
                    myBot.connect();
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(ChatboxActivity.this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void sendMessage() {
        // Trim whitespace and check if the message is not empty
        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            // Replace line breaks with a special marker to keep the message as a single unit
            String formattedMessage = message.replace("\n", " ");  // Or "\n" to keep line breaks if needed

            // Add the message to the list and update RecyclerView
            chatMessages.add(new ChatMessage(username, formattedMessage));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);

            // Scroll to the latest message
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

            // Send the message through the bot
            myBot.sendMessage("#YourChannel", formattedMessage);

            // Clear the input field
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Cannot send an empty message.", Toast.LENGTH_SHORT).show();
        }
    }`


    // Method to handle received messages
    public void addReceivedMessage(String senderName, String messageContent) {
        runOnUiThread(() -> {
            // Add received message to the list
            chatMessages.add(new ChatMessage(senderName, messageContent));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);

            // Scroll to the latest message
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBot != null) {
            myBot.disconnect();
            myBot = null;
        }
        botExecutorService.shutdownNow();
    }
}
