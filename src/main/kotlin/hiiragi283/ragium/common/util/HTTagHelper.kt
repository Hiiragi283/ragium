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
            val foundHolder: Optional<Holder<T>> = getFirstHolder(holderSet, modId)
            if (foundHolder.isPresent) return foundHolder
        }
        return holderSet.stream().findFirst()
    }

    @JvmStatic
    private fun <T : Any> getFirstHolder(holderSet: HolderSet<T>, namespace: String): Optional<Holder<T>> =
        holderSet.stream().filter { holder: Holder<T> -> holder.idOrThrow.namespace == namespace }.findFirst()
}
