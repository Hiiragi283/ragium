package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStackView
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.capabilities.BlockCapabilityCache

@JvmRecord
data class HTViewBlockCapabilityCache<T : Any, C : Any, STACK : ImmutableStack<*, STACK>>(
    private val cache: BlockCapabilityCache<T, C?>,
    private val provider: HTViewProvider<T, C, STACK>,
) {
    fun level(): ServerLevel = cache.level()

    fun pos(): BlockPos = cache.pos()

    fun context(): C? = cache.context()

    fun getCapability(): T? = cache.capability

    fun getViews(context: C?): List<HTStackView<STACK>> = getCapability()?.let { provider.apply(it, context) } ?: listOf()

    fun getView(context: C?, index: Int): HTStackView<STACK>? = getViews(context).getOrNull(index)
}
