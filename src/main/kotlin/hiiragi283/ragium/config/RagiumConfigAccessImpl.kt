package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumConfigAccess
import net.minecraft.resources.Identifier

class RagiumConfigAccessImpl : RagiumConfigAccess {
    override val modIdComparator: Comparator<Identifier> = Comparator
        .comparingInt { id: Identifier ->
            val modIds: List<String> = RagiumConfig.SERVER.tagOutputPriority.get()
            when (val priority: Int = modIds.indexOf(id.namespace)) {
                -1 -> modIds.size
                else -> priority
            }
        }.thenBy(Identifier::getNamespace)

    override val expRatio: Int get() = RagiumConfig.SERVER.expConversionRatio.asInt
}
