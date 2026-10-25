package hiiragi283.ragium.common.transfer.codec

import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTCapabilityCodec(private val canHandle: (HTBlockEntity) -> Boolean) {
    abstract fun saveTo(output: ValueOutput, blockEntity: HTBlockEntity)

    abstract fun loadFrom(input: ValueInput, blockEntity: HTBlockEntity)

    fun canHandle(blockEntity: HTBlockEntity): Boolean = canHandle.invoke(blockEntity)
}
