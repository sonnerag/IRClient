package vn.edu.usth.chatbox;
import android.util.Log;
import android.widget.Toast;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

public class MyBot extends ListenerAdapter {
    private final PircBotX bot;
    private final ChatboxActivity activity; // Reference to the ChatboxActivity
    private final String channel;
    private static final String TAG = "MyBot";
    public MyBot(ChatboxActivity activity ,String server, String channel, String nick) {
        this.activity = activity;
        this.channel = channel;
        Configuration config = new Configuration.Builder()
                .setName(nick) // Bot's nickname
                .setAutoNickChange(true) // Automatically change nickname if it's taken
                .addServer(server) // IRC server
                .addAutoJoinChannel(channel) // Join this channel on connect
                .addListener(this) // Add this class as a listener for events
                .buildConfiguration();
        this.bot = new PircBotX(config);
    }

    public void connect() {
        new Thread(() -> {
            try {
                if (!bot.isConnected()) {
                    bot.startBot(); // Kết nối bot
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Connected to IRC", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d(TAG, "Bot is already connected.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Failed to connect: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    public void sendMessage(String channel, String message) {
        new Thread(() -> {
            try {
                if (bot.isConnected()) {
                    bot.send().message(channel, message); // Send a message to a channel
                } else {
                    Log.d(TAG, "Bot is not connected.");
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Bot is not connected.", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Failed to send message: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


    public void disconnect() {
        Log.d(TAG, "Disconnecting from server...");

        // Kiểm tra bot có đang kết nối không trước khi thực hiện disconnect
        if (bot.isConnected()) {
            new Thread(() -> {
                try {
                    bot.sendIRC().quitServer("Bot disconnected"); // Send a quit message
                    Log.d(TAG, "Bot disconnected successfully.");
                } catch (Exception e) {
                    Log.e(TAG, "Error while disconnecting: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();
        } else {
            Log.d(TAG, "Bot was not connected.");
        }
    }
    public boolean isConnected() {
        return bot != null && bot.isConnected();
    }

    // Listen for incoming messages and handle commands
    @Override
    public void onMessage(MessageEvent event) {
        String message = event.getMessage();
        String channel = event.getChannel().getName();
        String senderNick = event.getUser().getNick();

        if (message.equalsIgnoreCase("!help")) {
            sendMessage(channel, "Available commands: !help, !joke, !time");
        } else if (message.equalsIgnoreCase("!joke")) {
            sendMessage(channel, "Why did the bot join the IRC channel? To connect with friends!");
        } else if (message.equalsIgnoreCase("!time")) {
            String time = java.time.LocalTime.now().toString();
            sendMessage(channel, "Current time is: " + time);
        }

        // Pass both the sender's name and the message content to display
        activity.runOnUiThread(() -> activity.addReceivedMessage(senderNick, message));
    }


    @Override
    public void onJoin(JoinEvent event) {
        String channel = event.getChannel().getName();
        String user = event.getUser().getNick();
        Log.d(TAG, "Bot joined channel: " + channel + " by " + user);
        activity.runOnUiThread(() ->
                Toast.makeText(activity, "Bot joined channel: " + channel, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDisconnect(org.pircbotx.hooks.events.DisconnectEvent event) {
        Log.d(TAG, "Bot disconnected from IRC.");
        activity.runOnUiThread(() ->
                Toast.makeText(activity, "Bot disconnected from IRC", Toast.LENGTH_SHORT).show());
    }

}


