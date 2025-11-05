package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

class HTEnergyNetwork(private var amount: Int, private var capacity: Int) :
    HTEnergyBattery.Basic(),
    HTContentListener.Empty {
    companion object {
        const val INITIAL_CAPACITY = 1_000_000
    }

    constructor() : this(0, INITIAL_CAPACITY)

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (this.amount == 0) return
            this.amount = 0
        } else if (!validate || amount > 0) {
            this.amount = min(amount, getCapacity())
        } else {
            error("Invalid amount for network: $amount")
        }
        onContentsChanged()
    }

    override fun getAmount(): Int = amount

    override fun getCapacity(): Int = capacity

    override fun serialize(output: HTValueOutput) {
        output.putInt(RagiumConst.AMOUNT, this.amount)
        output.putInt(RagiumConst.CAPACITY, this.capacity)
    }

    override fun deserialize(input: HTValueInput) {
        this.amount = input.getInt(RagiumConst.AMOUNT, 0)
        this.capacity = input.getInt(RagiumConst.CAPACITY, INITIAL_CAPACITY)
    }
}
