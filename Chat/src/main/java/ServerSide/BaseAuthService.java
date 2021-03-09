package ServerSide;

import ServerSide.Interface.AuthService;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    private List<Entry> entryList;

    public BaseAuthService(){
        entryList = new ArrayList<>();
        entryList.add(new Entry("log1", "pass1", "One"));
        entryList.add(new Entry("log2", "pass2", "Two"));
        entryList.add(new Entry("log3", "pass3", "Three"));

    }

    @Override
    public void start() {
        System.out.println("Authservice started");
    }

    @Override
    public void stop() {
        System.out.println("Authservice stopped");
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password){
        for (Entry e : entryList) {
            if (e.login.equalsIgnoreCase(login) && e.password.equalsIgnoreCase(password)){
                return e.nick;
            }
        }
        return null;
    }

    private class Entry{
        private String login;
        private String password;
        private String nick;

        public Entry(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
}
