package hiiragi283.ragium.common.block

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTBlockEntityTicker {

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

    @JvmStatic
    private fun <T : BlockEntity> ticker(action: (World, BlockPos, BlockState, T) -> Unit): BlockEntityTicker<T> =
        BlockEntityTicker(action)

}