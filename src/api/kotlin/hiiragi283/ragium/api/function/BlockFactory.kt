package hiiragi283.ragium.api.function

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.BiFunction
import java.util.function.Function

fun interface BlockFactory<BLOCK : Block> :
    Function<BlockBehaviour.Properties, BLOCK>,
    (BlockBehaviour.Properties) -> BLOCK {
    override fun apply(properties: BlockBehaviour.Properties): BLOCK = invoke(properties)

    fun interface WithContext<C, BLOCK : Block> :
        BiFunction<C, BlockBehaviour.Properties, BLOCK>,
        (C, BlockBehaviour.Properties) -> BLOCK {
        override fun apply(context: C, properties: BlockBehaviour.Properties): BLOCK = invoke(context, properties)
    }
}
