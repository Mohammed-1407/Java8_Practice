package stream.api;

import common.test.tool.annotation.Difficult;
import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.entity.Item;
import common.test.tool.util.CollectorImpl;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class Exercise9Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void simplestStringJoin() {
        List<Customer> customerList = this.mall.getCustomerList();

        Supplier<StringJoiner> supplier = () -> new StringJoiner(",");
        Collector<String, ?, String> toCsv = Collector.of(
                supplier,
                StringJoiner::add,
                (left, right) -> {
                    left.merge(right);
                    return left;
                },
                StringJoiner::toString
        );

        String nameAsCsv = customerList.stream().map(Customer::getName).collect(toCsv);
        assertThat(nameAsCsv, is("Joe,Steven,Patrick,Diana,Chris,Kathy,Alice,Andrew,Martin,Amy"));
    }

    @Difficult
    @Test
    public void mapKeyedByItems() {
        List<Customer> customerList = this.mall.getCustomerList();

        Collector<Customer, ?, Map<String, Set<String>>> toItemAsKey = Collector.of(
                // supplier: concurrent map for parallel use
                () -> new java.util.concurrent.ConcurrentHashMap<String, Set<String>>(),
                // accumulator
                (map, customer) -> {
                    customer.getWantToBuy().forEach(item -> {
                        map.computeIfAbsent(item.getName(), k -> java.util.concurrent.ConcurrentHashMap.newKeySet())
                                .add(customer.getName());
                    });
                },
                // combiner
                (m1, m2) -> {
                    m2.forEach((k, v) -> {
                        m1.merge(k, v, (s1, s2) -> {
                            s1.addAll(s2);
                            return s1;
                        });
                    });
                    return m1;
                }
                // identity finisher and concurrency characteristics implicit for ConcurrentHashMap usage
        );

        Map<String, Set<String>> itemMap =
                customerList.stream().parallel().collect(toItemAsKey);


        assertThat(itemMap.get("plane"), containsInAnyOrder("Chris"));
        assertThat(itemMap.get("onion"), containsInAnyOrder("Patrick", "Amy"));
        assertThat(itemMap.get("ice cream"), containsInAnyOrder("Patrick", "Steven"));
        assertThat(itemMap.get("earphone"), containsInAnyOrder("Steven"));
        assertThat(itemMap.get("plate"), containsInAnyOrder("Joe", "Martin"));
        assertThat(itemMap.get("fork"), containsInAnyOrder("Joe", "Martin"));
        assertThat(itemMap.get("cable"), containsInAnyOrder("Diana", "Steven"));
        assertThat(itemMap.get("desk"), containsInAnyOrder("Alice"));
    }

}
