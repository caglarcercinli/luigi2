package be.vdab.luigi2.repositories;

import be.vdab.luigi2.domain.Pizza;
import be.vdab.luigi2.exceptions.PizzaNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@JdbcTest
@Import(JdbcPizzaRepository.class)
@Sql("/insertPizzas.sql")
public class JdbcPizzaRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String PIZZAS = "pizzas";
    private final JdbcPizzaRepository repository;

    public JdbcPizzaRepositoryTest(JdbcPizzaRepository repository) {
        this.repository = repository;
    }

    @Test
    void findAantal() {
        assertThat(repository.findAantal()).isEqualTo(countRowsInTable(PIZZAS));
    }

    @Test
    void findAllGeeftAllePizzasGesorteerdOpId() {
        assertThat(repository.findAll()).hasSize(countRowsInTable(PIZZAS))
                .extracting(pizza -> pizza.getId()).isSorted();
    }

    @Test
    void create() {
        var id = repository.create(new Pizza(0, "test2", BigDecimal.TEN, false));
        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(PIZZAS, "id=" + id)).isOne();
    }

    private long idVanTestPizza() {
        return jdbcTemplate.queryForObject(
                "select id from pizzas where naam='test'", Long.class);
    }

    private long idVanTest2Pizza() {
        return jdbcTemplate.queryForObject(
                "select id from pizzas where naam='test2'", Long.class);
    }

    @Test
    void delete() {
        var id = idVanTestPizza();
        repository.delete(id);
        assertThat(countRowsInTableWhere(PIZZAS, "id=" + id)).isZero();
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestPizza())
                .get().getNaam()).isEqualTo("test");
    }

    @Test
    void findByOnbestaandeIdVindtGeenPizza() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void update() {
        var id = idVanTestPizza();
        var pizza = new Pizza(id, "test", BigDecimal.TEN, false);
        repository.update(pizza);
        assertThat(countRowsInTableWhere(PIZZAS, "prijs=10 and id=" + id)).isOne();
    }

    @Test
    void updateOnbestaandePizzaGeeftEenFout() {
        assertThatExceptionOfType(PizzaNietGevondenException.class).isThrownBy(
                () -> repository.update(new Pizza(-1, "test", BigDecimal.TEN, false)));
    }

    @Test
    void findByPrijsBetween() {
        assertThat(repository.findByPrijsBetween(BigDecimal.ONE, BigDecimal.TEN)).hasSize(countRowsInTableWhere(PIZZAS, "prijs between 1 and 10"))
                .allSatisfy(pizza ->
                        assertThat(pizza.getPrijs()).isBetween(BigDecimal.ONE, BigDecimal.TEN)).extracting(Pizza::getPrijs).isSorted();
    }

    @Test
    void findUniekePrijzenGeeftPrijzenOplopend() {
        assertThat(repository.findUniekePrijzen()).hasSize(jdbcTemplate.queryForObject(
                "select count(distinct prijs) from pizzas", Integer.class)).doesNotHaveDuplicates()
                .isSorted();
    }

    @Test
    void findByPrijs() {
        assertThat(repository.findByPrijs(BigDecimal.TEN))
                .hasSize(countRowsInTableWhere(PIZZAS, "prijs=10")).extracting(pizza -> pizza.getNaam().toLowerCase()).isSorted();
    }

    @Test
    void findByIds() {
        long id1 = idVanTestPizza();
        long id2 = idVanTest2Pizza();
        assertThat(repository.findByIds(Set.of(id1, id2)))
                .extracting(Pizza::getId).containsOnly(id1, id2).isSorted();
    }

    @Test
    void findByIdsGeeftLegeVerzamelingPizzasBijLegeVerzamelingIds() {
        assertThat(repository.findByIds(Set.of())).isEmpty();
    }

    @Test
    void findByIdsGeeftLegeVerzamelingPizzasBijOnbestaandeIds() {
        assertThat(repository.findByIds(Set.of(-1L))).isEmpty();
    }
}
