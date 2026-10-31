package hiiragi283.ragium.common.block

import net.minecraft.world.level.block.state.properties.BooleanProperty

data object RagiumBlockStateProperties {
    @JvmField
    val IS_ACTIVE: BooleanProperty = BooleanProperty.create("is_active")
}
