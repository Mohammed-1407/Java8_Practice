package stream.api;

import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.entity.Shop;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class StreamExercisesMain extends ClassicOnlineStore {

    public static void main(String[] args) {
        StreamExercisesMain app = new StreamExercisesMain();
        app.runAll();
    }

    private void runAll() {

        System.out.println("============================================");
        System.out.println("         STREAM EXERCISES QUICK RUN");
        System.out.println("============================================\n");

        List<Customer> customers = this.mall.getCustomerList();
        List<Shop> shops = this.mall.getShopList();

        System.out.println("Loaded " + customers.size() + " customers and " + shops.size() + " shops.\n");

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 1 — Rich Customers");
        // ----------------------------------------------------
        List<Customer> rich = customers.stream()
                .filter(c -> c.getBudget() > 10000)
                .collect(Collectors.toList());
        System.out.println("Rich customers (>10000): "
                + rich.stream().map(Customer::getName).collect(Collectors.toList()));
        System.out.println();

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 2 — Sorted Ages");
        // ----------------------------------------------------
        List<Integer> sortedAges = customers.stream()
                .map(Customer::getAge)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Sorted ages: " + sortedAges);
        System.out.println();

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 3 — Total Wanted Items");
        // ----------------------------------------------------
        long wantedCount = customers.stream()
                .flatMap(c -> c.getWantToBuy().stream())
                .count();
        System.out.println("Total number of wanted items: " + wantedCount);
        System.out.println();

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 5 — Customer Names CSV");
        // ----------------------------------------------------
        String csv = customers.stream()
                .map(Customer::getName)
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println("Customer names as CSV: " + csv);
        System.out.println();

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 7 — Total Shop Items Price");
        // ----------------------------------------------------
        long totalShopPrice = shops.stream()
                .flatMap(s -> s.getItemList().stream())
                .mapToLong(Item::getPrice)
                .sum();
        System.out.println("Total price of all shop items: " + totalShopPrice);
        System.out.println();

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 8 — Items Not on Sale");
        // ----------------------------------------------------
        List<String> onSale = shops.stream()
                .flatMap(s -> s.getItemList().stream())
                .map(Item::getName)
                .collect(Collectors.toList());

        Set<String> notOnSale = customers.stream()
                .flatMap(c -> c.getWantToBuy().stream())
                .map(Item::getName)
                .filter(n -> !onSale.contains(n))
                .collect(Collectors.toSet());

        System.out.println("Items customers want but are NOT on sale: " + notOnSale);
        System.out.println();

        // ----------------------------------------------------
        System.out.println("▶ EXERCISE 9 — Joined Names");
        // ----------------------------------------------------
        String join = customers.stream()
                .map(Customer::getName)
                .collect(Collectors.joining(", "));
        System.out.println("All customer names joined: " + join);
        System.out.println();

        // ----------------------------------------------------
        System.out.println("============================================");
        System.out.println("                 END OF RUN");
        System.out.println("============================================");
    }
}
