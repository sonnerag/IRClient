package vn.edu.usth.chatbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Random;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;

    // Constructor to initialize the chat messages
    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view for each message item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        // Get the message at the current position
        ChatMessage message = chatMessages.get(position);

        // Set the sender name and message content
        holder.senderNameTextView.setText(message.getSenderName());
        holder.chatTextView.setText(message.getMessage());

        // Set dark text color for both TextViews
        int textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.text_dark);
        holder.senderNameTextView.setTextColor(textColor);
        holder.chatTextView.setTextColor(textColor);

        // Get the color resources for random background color selection
        int[] colors = {
                ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_light),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.blue),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_dark),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_darker),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_lightest)
        };

        // Set a random background color for each message
        Random random = new Random();
        int randomColor = colors[random.nextInt(colors.length)];
        holder.itemView.setBackgroundColor(randomColor);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    // ViewHolder class to hold references to the text views
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderNameTextView, chatTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the text views by ID
            senderNameTextView = itemView.findViewById(R.id.senderNameTextView);
            chatTextView = itemView.findViewById(R.id.chatTextView);
        }
    }

    // Method to add a new message and notify the adapter to update the view
    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyItemInserted(chatMessages.size() - 1);
    }
}
