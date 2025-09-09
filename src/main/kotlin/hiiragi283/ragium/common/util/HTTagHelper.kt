package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import java.util.Optional

object HTTagHelper {
    @JvmStatic
    fun <T : Any> getFirstHolder(holderSet: HolderSet<T>): Optional<Holder<T>> {
        for (modId: String in RagiumConfig.CONFIG.tagOutputPriority.get()) {
            val foundHolder: Holder<T>? = getFirstHolder(holderSet, modId)
            if (foundHolder != null) return Optional.of(foundHolder)
        }
        return Optional.ofNullable(holderSet.firstOrNull())
    }

    @JvmStatic
    private fun <T : Any> getFirstHolder(holderSet: HolderSet<T>, namespace: String): Holder<T>? =
        holderSet.firstOrNull { holder: Holder<T> -> holder.idOrThrow.namespace == namespace }
}
