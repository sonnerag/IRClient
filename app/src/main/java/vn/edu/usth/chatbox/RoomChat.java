package vn.edu.usth.chatbox;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class RoomChat extends Fragment {
    private ScaleGestureDetector scaleGestureDetector;
    private float currentTextSize;
    private LinearLayout messageContainer;
    private EditText messageEditText;
    private Button sendButton;
    private ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.box_chat, container, false);

        // Set up views
//        scrollView = rootView.findViewById(R.id.scrollView);
//        messageContainer = rootView.findViewById(R.id.messageContainer);
        messageEditText = rootView.findViewById(R.id.messageEditText);
        sendButton = rootView.findViewById(R.id.sendButton);

        // Get the default text size from resources
        currentTextSize = getResources().getDimension(R.dimen.chat_text_size);

        // Set up pinch-to-zoom
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        // Handle touch for zoom and allow normal scrolling
        scrollView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return false; // Let ScrollView handle scrolling events
        });

        // Set up send button to send message
        sendButton.setOnClickListener(v -> sendMessage());

        return rootView;
    }

    // Custom scale listener to handle pinch zoom
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            currentTextSize *= scaleFactor;

            // Limit the text size to a reasonable range
            currentTextSize = Math.max(getResources().getDimension(R.dimen.chat_text_size_small),
                    Math.min(currentTextSize, getResources().getDimension(R.dimen.chat_text_size_large)));

            // Update text size for all existing TextViews inside the ScrollView
            updateTextSize(currentTextSize);
            return true;
        }
    }

    public void addReceivedMessage(String message) {
        TextView newMessage = new TextView(getContext());
        newMessage.setText(message);
        newMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize);
        newMessage.setTextColor(ContextCompat.getColor(getContext(),android.R.color.black));
        newMessage.setPadding(8, 8, 8, 8);
        messageContainer.addView(newMessage);

        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    // Function to update text size of all TextViews inside the messageContainer (ScrollView)
    private void updateTextSize(float textSize) {
        // Loop through all children in messageContainer (which includes all messages)
        for (int i = 0; i < messageContainer.getChildCount(); i++) {
            View child = messageContainer.getChildAt(i);

            // If the child is a TextView, apply the new text size directly
            if (child instanceof TextView) {
                ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // Check if the child is a LinearLayout (which holds multiple TextViews)
            if (child instanceof LinearLayout) {
                LinearLayout conversationLayout = (LinearLayout) child;

                // Loop through the children of the LinearLayout
                for (int j = 0; j < conversationLayout.getChildCount(); j++) {
                    View conversationChild = conversationLayout.getChildAt(j);

                    // If this child is a TextView, apply the new text size
                    if (conversationChild instanceof TextView) {
                        ((TextView) conversationChild).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                }
            }
        }
    }

    // Send message when the send button is clicked
    private void sendMessage() {
        // Get the text from the EditText
        String message = messageEditText.getText().toString().trim();

        // Check if the message is not empty
        if (!message.isEmpty()) {
            // Create a new TextView to display the message
            TextView newMessage = new TextView(getContext());
            newMessage.setText("You: " + message);
            newMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize);  // Apply current text size
            newMessage.setTextColor(getResources().getColor(android.R.color.black));
            newMessage.setPadding(8, 8, 8, 8);  // Add some padding
            messageContainer.addView(newMessage); // Add the new message to the container

            // Clear the EditText after sending the message
            messageEditText.setText("");

            // Scroll to the bottom to show the latest message
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));

            // Ensure that the newly added message also responds to pinch-zoom
            newMessage.setOnTouchListener((v, event) -> {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            });
        }
    }
}
