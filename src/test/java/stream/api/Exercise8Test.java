package stream.api;

import common.test.tool.annotation.Difficult;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.entity.Shop;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class Exercise8Test extends ClassicOnlineStore {

    @Difficult
    @Test
    public void itemsNotOnSale() {
        // reuse lists directly from mall to avoid stream reuse issues
        List<Customer> customers = this.mall.getCustomerList();
        List<Shop> shops = this.mall.getShopList();

        Set<String> itemListOnSale = shops.stream()
                .flatMap(s -> s.getItemList().stream())
                .map(Item::getName)
                .collect(Collectors.toSet());

        Set<String> itemSetNotOnSale = customers.stream()
                .flatMap(c -> c.getWantToBuy().stream())
                .map(Item::getName)
                .filter(name -> !itemListOnSale.contains(name))
                .collect(Collectors.toSet());

        assertThat(itemSetNotOnSale, hasSize(3));
        assertThat(itemSetNotOnSale, hasItems("bag", "pants", "coat"));
    }

    @Difficult
    @Test
    public void havingEnoughMoney() {
        List<Customer> customers = this.mall.getCustomerList();
        List<Shop> shops = this.mall.getShopList();

        // build the cheapest price map — explicitly box to Long and compare primitive long values
        Map<String, Long> cheapest = shops.stream()
                .flatMap(s -> s.getItemList().stream())
                .collect(Collectors.toMap(
                        Item::getName,
                        item -> Long.valueOf(item.getPrice()),          // ensure value type is Long
                        (p1, p2) -> p1.longValue() <= p2.longValue() ? p1 : p2  // compare primitives
                ));

        List<String> customerNameList = customers.stream()
                .filter(c -> {
                    long total = c.getWantToBuy().stream()
                            .map(Item::getName)
                            .mapToLong(name -> cheapest.getOrDefault(name, 0L))
                            .sum();
                    return c.getBudget() >= total;
                })
                .map(Customer::getName)
                .collect(Collectors.toList());

        assertThat(customerNameList, hasSize(7));
        assertThat(customerNameList, hasItems("Joe", "Patrick", "Chris", "Kathy", "Alice", "Andrew", "Amy"));
    }
}
