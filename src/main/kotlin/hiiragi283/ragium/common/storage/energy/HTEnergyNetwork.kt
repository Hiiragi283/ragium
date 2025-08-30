package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.storage.energy.IEnergyStorageModifiable
import io.netty.buffer.ByteBuf
import net.minecraft.util.Mth
import kotlin.math.min

class HTEnergyNetwork(private var amount: Int, private var capacity: Int) : IEnergyStorageModifiable {
    companion object {
        @JvmStatic
        private fun getInitialCapacity(): Int = RagiumAPI.getConfig().getDefaultNetworkCapacity()

        @JvmField
        val CODEC: BiCodec<ByteBuf, HTEnergyNetwork> = BiCodec.composite(
            BiCodecs.NON_NEGATIVE_INT.optionalFieldOf("amount", 0),
            HTEnergyNetwork::amount,
            BiCodecs.POSITIVE_INT.optionalFieldOf("capacity", ::getInitialCapacity),
            HTEnergyNetwork::capacity,
            ::HTEnergyNetwork,
        )
    }

    constructor() : this(0, getInitialCapacity())

    override fun setEnergyStored(amount: Int) {
        this.amount = amount
    }

    override fun setMaxEnergyStored(capacity: Int) {
        this.capacity = capacity
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!canReceive() || toReceive <= 0) return 0
        val received: Int = Mth.clamp(maxEnergyStored - energyStored, 0, toReceive)
        if (!simulate) {
            this.amount += received
        }
        return received
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!canExtract() || toExtract <= 0) return 0
        val extracted: Int = min(energyStored, toExtract)
        if (!simulate) {
            this.amount -= extracted
        }
        return extracted
    }

    override fun getEnergyStored(): Int = amount

    override fun getMaxEnergyStored(): Int = capacity

    override fun canExtract(): Boolean = true

    override fun canReceive(): Boolean = true
}
