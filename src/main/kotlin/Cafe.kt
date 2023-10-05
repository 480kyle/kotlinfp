import kotlin.reflect.KClass

interface CreditCard

interface Product{
    val price: Double
    val factory: () -> Product
}

class Charge<PRODUCT:Product>(val cc: CreditCard, val qty: Int, val product:KClass<PRODUCT>)

class Receipt<PRODUCT:Product>(val qty: Int, val product: KClass<PRODUCT>)

class Americano: Product {
    override val price: Double get() = 1000.0
    override val factory: () -> Product
        get() = ::Americano
}

class CafeLatte: Product{
    override val price: Double
        get() = 2000.0
    override val factory: () -> Product
        get() = ::CafeLatte
}