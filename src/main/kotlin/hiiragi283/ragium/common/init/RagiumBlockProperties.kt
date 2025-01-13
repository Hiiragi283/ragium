package hiiragi283.ragium.common.init

import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty

object RagiumBlockProperties {
    @JvmField
    val ACTIVE: BooleanProperty = BooleanProperty.create("active")

    @JvmField
    val COLOR: EnumProperty<DyeColor> = EnumProperty.create("color", DyeColor::class.java)

    @JvmField
    val LEVEL_7: IntegerProperty = IntegerProperty.create("level", 0, 7)
}
