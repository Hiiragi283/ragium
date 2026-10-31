package hiiragi283.ragium.common.transfer.codec

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

data object HTEnergyCapabilityCodec : HTCapabilityCodec(HTBlockEntity::hasEnergyStorage) {
    override fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity) {
        val storage: HTEnergyStorage = getHandler(blockEntity) ?: return
        if (storage is ValueIOSerializable) {
            output.putChild(HTConstants.ENERGY, storage)
        } else {
            RagiumAPI.LOGGER.warn("Energy storage: $storage does not support serialization")
        }
    }

    override fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity) {
        val storage: HTEnergyStorage = getHandler(blockEntity) ?: return
        if (storage is ValueIOSerializable) {
            input.readChild(HTConstants.ENERGY, storage)
        } else {
            RagiumAPI.LOGGER.warn("Energy storage: $storage does not support deserialization")
        }
    }

    private fun getHandler(blockEntity: HTBlockEntity): HTEnergyStorage? = blockEntity.getEnergyStorage(null)
}
