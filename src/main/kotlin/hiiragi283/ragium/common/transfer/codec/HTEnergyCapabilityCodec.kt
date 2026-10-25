package hiiragi283.ragium.common.transfer.codec

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

data object HTEnergyCapabilityCodec : HTCapabilityCodec(HTBlockEntity::hasEnergyHandler) {
    override fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity) {
        val handler: HTEnergyHandler = getHandler(blockEntity) ?: return
        output.putChild(HTConstants.ENERGY, handler)
    }

    override fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity) {
        val handler: HTEnergyHandler = getHandler(blockEntity) ?: return
        input.readChild(HTConstants.ENERGY, handler)
    }

    private fun getHandler(blockEntity: HTBlockEntity): HTEnergyHandler? = blockEntity.getEnergyHandler(null)
}
