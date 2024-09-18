package hiiragi283.ragium.common.block

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType

object HTBlockEntityTickers {
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <E : BlockEntity, A : BlockEntity> validateTicker(
        givenType: BlockEntityType<A>,
        expectedType: BlockEntityType<E>,
        ticker: BlockEntityTicker<*>?,
    ): BlockEntityTicker<A>? = when (expectedType == givenType) {
        true -> ticker
        false -> null
    } as? BlockEntityTicker<A>
}
