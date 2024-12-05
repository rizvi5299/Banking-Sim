public class AccountManager {
    private static AccountManager instance;

    private int totalUsers = 0;

    private AccountManager() {} //singleton class

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public void incrementTotalUsers() {
        totalUsers++;
    }

    public void decrementTotalUsers() {
        totalUsers--;
    }

    public int getTotalUsers() {
        return totalUsers;
    }
}
