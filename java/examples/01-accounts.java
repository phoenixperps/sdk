// 01: who you are (the key, its access and limits) and your accounts.
//   PHOENIXPERPS_API_KEY=ppk_test_... gradle -q example -Pn=01
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.Account;
import com.phoenixperpsfunding.sdk.gen.Me;

final class Example01 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv(); // the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)

        Me me = client.me().get();
        System.out.printf("%s \"%s\" on %s: %s access%n", me.credential().kind(), me.credential().name(), me.credential().environment(), me.credential().access());
        System.out.printf("limits: %d reads and %d trading calls a minute%n", me.budgets().readPerMin(), me.budgets().tradePerMin());

        for (Account a : client.accounts().list().data()) {
            System.out.printf("%s  %s  %s  balance %s%n", a.id(), a.stage(), a.status(), a.balance() == null ? "-" : a.balance().toPlainString());
        }
    }
}
