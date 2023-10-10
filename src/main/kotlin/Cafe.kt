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

class Cafe {
    @PublishedApi
    internal val factories: HashMap<KClass<*>, () -> Product> = hashMapOf()
    fun addFactory(vararg products: Product) {
        products.forEach {
            factories[it::class] = it.factory
        }
    }
    fun <PRODUCT: Product> getProduct(product:KClass<PRODUCT>): PRODUCT = factories[product]!!() as PRODUCT
    fun <PRODUCT: Product> buy(cc: CreditCard, qty: Int, product: KClass<PRODUCT>): Charge<PRODUCT>?
        = if(qty == 0) null else Charge(cc, qty, product)
    fun <PRODUCT: Product> payment(vararg charges: Charge<PRODUCT>): Receipt<PRODUCT>? {
        if(charges.isEmpty()) return null
        val totalQty = charges.sumOf { it.qty }
        // 결제 부수효과
        val isPaidOK = true
        return if(isPaidOK) Receipt(totalQty, charges.first().product) else null
    }
    inline fun <reified PRODUCT: Product> receive(receipt: Receipt<PRODUCT>): Array<PRODUCT>? {
        factories[receipt.product] ?: return null
        // receipt에 있는 갯수만큼 받는다.
        return Array(receipt.qty){getProduct(receipt.product)}
    }
}