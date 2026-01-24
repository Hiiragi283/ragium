package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCreativeBatteryBlockEntity(pos: BlockPos, state: BlockState) :
    HTBatteryBlockEntity(RagiumBlockEntityTypes.CREATIVE_BATTERY, pos, state) {
    override fun createBattery(listener: HTContentListener): HTEnergyBattery.Basic =
        object : HTEnergyBattery.Basic(), HTContentListener.Empty, HTValueSerializable.Empty {
            override fun setAmount(amount: Int) {}

            override fun getAmount(): Int = Int.MAX_VALUE

            override fun getCapacity(): Int = Int.MAX_VALUE
        }
}
