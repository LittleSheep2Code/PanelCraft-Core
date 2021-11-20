package club.smartsheep.panelcraftcore.Common.Tokens;

public class PersonalToken extends CommonToken {

    public String createPersonalToken(String password, long expire) {
        return this.summonToken(expire, password);
        // TODO Add a Activities log to database
    }

    public String createPersonalToken(String password) {
        return this.summonToken(password);
        // TODO Add a Activities log to database
    }
}
