package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.util.Mth

class HTEnergyNetwork(private var amount: Long, private var capacity: Long) :
    HTEnergyBattery.Mutable,
    HTContentListener.Empty {
    constructor(amount: Int, capacity: Int) : this(amount.toLong(), capacity.toLong())

    companion object {
        const val INITIAL_CAPACITY = 1_000_000L
    }

    constructor() : this(0, INITIAL_CAPACITY)

    override fun getAmountAsLong(): Long = amount

    override fun getCapacityAsLong(): Long = capacity

    override fun serialize(output: HTValueOutput) {
        output.putLong("amount", this.amount)
        output.putLong("capacity", this.capacity)
    }

    override fun deserialize(input: HTValueInput) {
        this.amount = input.getLong("amount", 0)
        this.capacity = input.getLong("capacity", INITIAL_CAPACITY)
    }

    override fun setAmountAsLong(amount: Long) {
        this.amount = Mth.clamp(amount, 0, capacity)
    }
}
