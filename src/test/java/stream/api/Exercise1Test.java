package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;
import common.test.tool.util.AssertUtil;

import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class Exercise1Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void findRichCustomers() {
        List<Customer> customerList = this.mall.getCustomerList();

        Predicate<Customer> richCustomerCondition = c -> c.getBudget() > 10000;
        Stream<Customer> richCustomerStream = customerList.stream().filter(richCustomerCondition);

        assertTrue("Solution for Predicate should be lambda expression", AssertUtil.isLambda(richCustomerCondition));
        List<Customer> richCustomer = richCustomerStream.collect(Collectors.toList());
        assertThat(richCustomer, hasSize(2));
        assertThat(richCustomer, contains(customerList.get(3), customerList.get(7)));
    }

    @Easy
    @Test
    public void howOldAreTheCustomers() {
        List<Customer> customerList = this.mall.getCustomerList();

        Function<Customer, Integer> getAgeFunction = c -> c.getAge();
        Stream<Integer> ageStream = customerList.stream().map(getAgeFunction);

        assertTrue(AssertUtil.isLambda(getAgeFunction));
        List<Integer> ages = ageStream.collect(Collectors.toList());
        assertThat(ages, hasSize(10));
        assertThat(ages, contains(22, 27, 28, 38, 26, 22, 32, 35, 21, 36));
    }
}
