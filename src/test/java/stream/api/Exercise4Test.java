package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import common.test.tool.entity.Customer;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class Exercise4Test extends ClassicOnlineStore {

    @Easy
    @Test
    public void firstRegistrant() {
        List<Customer> customerList = this.mall.getCustomerList();

        Optional<Customer> firstCustomer = customerList.stream().findFirst();

        assertThat(firstCustomer.get(), is(customerList.get(0)));
    }

    @Easy
    @Test
    public void isThereAnyoneOlderThan40() {
        List<Customer> customerList = this.mall.getCustomerList();

        boolean olderThan40Exists = customerList.stream().anyMatch(c -> c.getAge() > 40);

        assertThat(olderThan40Exists, is(false));
    }

    @Easy
    @Test
    public void isEverybodyOlderThan20() {
        List<Customer> customerList = this.mall.getCustomerList();

        boolean allOlderThan20 = customerList.stream().allMatch(c -> c.getAge() > 20);

        assertThat(allOlderThan20, is(true));
    }

    @Easy
    @Test
    public void everyoneWantsSomething() {
        List<Customer> customerList = this.mall.getCustomerList();

        boolean everyoneWantsSomething = customerList.stream().noneMatch(c -> c.getWantToBuy().isEmpty());

        assertThat(everyoneWantsSomething, is(true));
    }
}
