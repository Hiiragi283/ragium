package hiiragi283.ragium.common.block

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTHorizontalEntityBlock
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty

class HTMachineBlock(private val translation: HTTranslation, type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTHorizontalEntityBlock(type, properties),
    HTBlockWithDescription {
    companion object {
        @JvmField
        val IS_ACTIVE: BooleanProperty = BooleanProperty.create("is_active")
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(IS_ACTIVE)
    }

    override fun getDescription(): HTTranslation = translation
}
