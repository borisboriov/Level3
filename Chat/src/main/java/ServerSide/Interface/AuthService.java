package ServerSide.Interface;

public interface AuthService {


    void start();

    void stop();

    String getNickByLoginAndPassword(String login, String password);

    }
