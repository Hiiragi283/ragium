package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.minecraft.util.Mth
import kotlin.math.min

class HTEnergyNetwork(private var amount: Int, private var capacity: Int) : HTEnergyBattery {
    companion object {
        const val INITIAL_CAPACITY = 1_000_000
    }

    constructor() : this(0, INITIAL_CAPACITY)

    override fun getAmount(): Int = amount

    override fun setAmount(amount: Int) {
        this.amount = amount
    }

    override fun getCapacity(): Int = capacity

    override fun insertEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int {
        if (amount <= 0) return 0
        val received: Int = Mth.clamp(getNeeded(), 0, amount)
        if (!simulate) {
            this.amount += received
        }
        return received
    }

    override fun extractEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int {
        if (amount <= 0) return 0
        val extracted: Int = min(getAmount(), amount)
        if (!simulate) {
            this.amount -= extracted
        }
        return extracted
    }

    override fun serialize(output: HTValueOutput) {
        output.store("amount", BiCodecs.NON_NEGATIVE_INT, this.amount)
        output.store("capacity", BiCodecs.POSITIVE_INT, this.capacity)
    }

    override fun deserialize(input: HTValueInput) {
        this.amount = input.read("amount", BiCodecs.NON_NEGATIVE_INT) ?: 0
        this.capacity = input.read("capacity", BiCodecs.POSITIVE_INT) ?: INITIAL_CAPACITY
    }

    override fun onContentsChanged() {}
}
