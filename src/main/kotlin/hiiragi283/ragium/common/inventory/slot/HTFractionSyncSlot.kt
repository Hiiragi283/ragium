package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.common.inventory.slot.payload.HTFractionSyncPayload
import hiiragi283.ragium.common.inventory.slot.payload.HTIntSyncPayload
import net.minecraft.core.RegistryAccess
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0

class HTFractionSyncSlot(private val getter: Supplier<Fraction>, private val setter: Consumer<Fraction>) : HTIntSyncSlot {
    constructor(property: KMutableProperty0<Fraction>) : this(property::get, property::set)

    private var lastValue: Fraction = Fraction.ZERO

    override fun getAmountAsInt(): Int = getAmount().numerator

    override fun setAmountAsInt(amount: Int) {
        setAmount(fraction(amount, lastValue.denominator))
    }

    fun getAmount(): Fraction = this.getter.get()

    fun setAmount(amount: Fraction) {
        this.setter.accept(amount)
    }

    override fun getChange(): HTChangeType {
        val current: Fraction = this.getAmount()
        val last: Fraction = this.lastValue
        this.lastValue = current
        return when {
            current == last -> HTChangeType.EMPTY
            current.denominator == last.denominator -> HTChangeType.AMOUNT
            else -> HTChangeType.FULL
        }
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload? = when (changeType) {
        HTChangeType.EMPTY -> null
        HTChangeType.AMOUNT -> HTIntSyncPayload(this.getAmount().numerator)
        HTChangeType.FULL -> HTFractionSyncPayload(this.getAmount())
    }
}
