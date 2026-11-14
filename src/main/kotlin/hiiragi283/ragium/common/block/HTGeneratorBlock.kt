package hiiragi283.ragium.common.block

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

open class HTGeneratorBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTBasicEntityBlock(type, properties) {
    init {
        registerDefaultState(stateDefinition.any().setValue(RagiumBlockStateProperties.IS_ACTIVE, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(RagiumBlockStateProperties.IS_ACTIVE)
    }
}
