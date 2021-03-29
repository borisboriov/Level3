package ClientSide.Three;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EchoClient extends JFrame {

    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;

    private JTextField msgInputField;
    private JTextArea chatArea;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    boolean isAuthorized = false;

    public EchoClient() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareGUI();
    }

    public void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {

            try {
                while (true) {
                    String strFromServer = dis.readUTF();
                    if (strFromServer.startsWith("/authok")) {
                        isAuthorized = true;
                        chatArea.append(strFromServer + "\n");
                        break;
                    }
                }

                loadMsgHistory();

                while (isAuthorized) {
                    String strFromServer = dis.readUTF();
                    chatArea.append(strFromServer + "\n");

                    saveMsgHistory();
                }
            } catch (Exception ignored) {
            }
        }).start();
    }

    private void saveMsgHistory() throws IOException {
        File history = new File("Chat/src/main/java/ClientSide/Three/history_login3.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(history));
        String str;
        str = chatArea.getText();
        bufferedWriter.write(str);
        bufferedWriter.close();
    }

    private void loadMsgHistory() throws IOException {
        int count = 100;
        File loadHistory = new File("Chat/src/main/java/ClientSide/Three/history_login3.txt");
        List<String> msgList = new ArrayList<>();
        FileInputStream in = new FileInputStream(loadHistory);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            msgList.add(temp);
        }
        if (msgList.size() > count) {
            for (int i = msgList.size() - count; i <= (msgList.size() - 1); i++) {
                chatArea.append(msgList.get(i) + "\n");
            }
        }
    }

    public void closeConnection() {
        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void onAuthClick() {
//        try {
//            dos.writeUTF("/auth" + " " + loginfield.getText() + " "passwordField.getText());
//            loginfield.setText("");
//            passwordField.setText("");
//        } catch (IOException ignored) {
//        }
//    }

    public void sendMessage() {
        if (msgInputField.getText() != null && !msgInputField.getText().trim().isEmpty()) {

            try {
                dos.writeUTF(msgInputField.getText());
                if (msgInputField.getText().equals("/end")) {
                    isAuthorized = false;
                    closeConnection();
                }
                chatArea.append(msgInputField.getText() + "\n");
                msgInputField.setText("");
            } catch (IOException ignored) {
            }
        }
    }

    public void prepareGUI() {
        // Параметры окна
        setBounds(600, 300, 500, 500);
        setTitle("Клиент");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Текстовое поле для вывода сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        btnSendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        msgInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Настраиваем действие на закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    dos.writeUTF("/end");
                    closeConnection();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new EchoClient();
        });
    }

}
