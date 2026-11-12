package hiiragi283.ragium.common.transfer.codec

import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

interface HTCapabilityCodec {
    fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity)

    fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity)

    fun canHandle(blockEntity: HTBlockEntity): Boolean
}
