package be.vdab.luigi2.sessions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MandjeTest {
    private Mandje mandje;

    @BeforeEach
    void beforeEach() {
        mandje = new Mandje();
    }

    @Test
    void eenNieuweMandjeIsLeeg() {
        assertThat(mandje.getIds()).isEmpty();
    }

    @Test
    void nadatJeEenItemInHetMandjeLegtBevatDitMandjeEnkelDitItem() {
        mandje.voegToe(10L);
        assertThat(mandje.getIds()).containsOnly(10L);
    }

    @Test
    void nadatJeTweeItemsInHetMandjeLegtBevatDitMandjeEnkelDieItems() {
        mandje.voegToe(10L);
        mandje.voegToe(20L);
        assertThat(mandje.getIds()).containsOnly(10L, 20L);
    }
}
