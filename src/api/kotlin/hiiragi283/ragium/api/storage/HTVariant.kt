package hiiragi283.ragium.api.storage

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch

interface HTVariant<T : Any> {
    val holder: Holder.Reference<T>
    val components: DataComponentPatch

    val isEmpty: Boolean
}
