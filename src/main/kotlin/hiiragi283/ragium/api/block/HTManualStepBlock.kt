package hiiragi283.ragium.api.block

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty

abstract class HTManualStepBlock(properties: Properties) : HTEntityBlock(properties) {
    init {
        registerDefaultState(
            stateDefinition
                .any()
                .setValue(getStepProperty(), 0),
        )
    }

    protected abstract fun getStepProperty(): IntegerProperty

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(getStepProperty())
    }
}
