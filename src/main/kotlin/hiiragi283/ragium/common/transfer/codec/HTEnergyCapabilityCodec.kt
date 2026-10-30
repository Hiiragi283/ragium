package hiiragi283.ragium.common.transfer.codec

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

data object HTEnergyCapabilityCodec : HTCapabilityCodec(HTBlockEntity::hasEnergyStorage) {
    override fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity) {
        val storage: HTEnergyStorage = getHandler(blockEntity) ?: return
        output.putChild(HTConstants.ENERGY, storage)
    }

    override fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity) {
        val storage: HTEnergyStorage = getHandler(blockEntity) ?: return
        input.readChild(HTConstants.ENERGY, storage)
    }

    private fun getHandler(blockEntity: HTBlockEntity): HTEnergyStorage? = blockEntity.getEnergyStorage(null)
}
