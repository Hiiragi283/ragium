package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet

object HTTagHelper {
    @JvmStatic
    fun <T : Any> getFirstHolder(holderSet: HolderSet<T>): Holder<T>? {
        for (modId: String in RagiumConfig.COMMON.tagOutputPriority.get()) {
            val foundHolder: Holder<T>? = getFirstHolder(holderSet, modId)
            if (foundHolder != null) return foundHolder
        }
        return holderSet.firstOrNull()
    }

    @JvmStatic
    private fun <T : Any> getFirstHolder(holderSet: HolderSet<T>, namespace: String): Holder<T>? =
        holderSet.firstOrNull { holder: Holder<T> -> holder.idOrThrow.namespace == namespace }
}
