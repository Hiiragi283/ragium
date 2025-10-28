package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.util.Mth

class HTEnergyNetwork(private var amount: Int, private var capacity: Int) :
    HTEnergyStorage.Basic(),
    HTContentListener.Empty {
    companion object {
        const val INITIAL_CAPACITY = 1_000_000
    }

    constructor() : this(0, INITIAL_CAPACITY)

    override fun setAmount(amount: Int, action: HTStorageAction) {
        if (action.execute) {
            this.amount = Mth.clamp(amount, 0, capacity)
        }
    }

    override fun getAmount(): Int = amount

    override fun getCapacity(): Int = capacity

    override fun serialize(output: HTValueOutput) {
        output.putInt(RagiumConst.AMOUNT, this.amount)
        output.putInt("capacity", this.capacity)
    }

    override fun deserialize(input: HTValueInput) {
        this.amount = input.getInt(RagiumConst.AMOUNT, 0)
        this.capacity = input.getInt("capacity", INITIAL_CAPACITY)
    }
}
