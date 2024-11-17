package hiiragi283.ragium.common.init

import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.DyeColor

object RagiumBlockProperties {
    @JvmField
    val ACTIVE: BooleanProperty = BooleanProperty.of("active")

    @JvmField
    val COLOR: EnumProperty<DyeColor> = EnumProperty.of("color", DyeColor::class.java)

    @JvmField
    val LEVEL_7: IntProperty = IntProperty.of("level", 0, 7)
}
