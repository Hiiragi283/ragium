package hiiragi283.lib.serialization

import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap

interface HTDataComponentSerializable {
    fun serialize(builder: DataComponentMap.Builder)

    fun deserialize(getter: DataComponentGetter)
}
