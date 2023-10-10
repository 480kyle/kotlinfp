import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CafeTest {
    @Test
    fun test1() {
        val cafe: Cafe = Cafe().also {
            it.addFactory(Americano(), CafeLatte())
        }
        val myCard = object :CreditCard{}
        val americano = cafe.buy(myCard, 2, Americano::class)?.let { charge ->
            cafe.payment(charge)?.let { receipt ->
                cafe.receive(receipt)
            }
        }
        assertEquals(americano?.size, 2)
        assertEquals(americano?.get(0)?.price, 1000.0)
    }
}