package hiiragi283.lib.transfer.resolver

import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction

/**
 * [HTCapabilityResolver]の実装クラスです。
 *
 * 参照 : [Mekanism - BasicSidedCapabilityResolver](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/capabilities/resolver/BasicSidedCapabilityResolver.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
abstract class HTCapabilityManager<HOLDER : HTCapabilityHolder, HANDLER : Any>(
    protected val holder: HOLDER,
    private val proxyCreator: ProxyCreator<HOLDER, HANDLER>,
) : HTCapabilityResolver {
    private val handlers: MutableMap<Direction, HANDLER> = mutableEnumMapOf()
    private var readOnlyHandler: HANDLER? = null

    override fun <T : Any> resolve(side: Direction?): T? = when {
        isEmpty(side) -> null
        else -> resolveInternal(side)
    }

    protected abstract fun isEmpty(side: Direction?): Boolean

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> resolveInternal(side: Direction?): T? {
        if (side == null) {
            if (readOnlyHandler == null) {
                readOnlyHandler = proxyCreator.create(null, holder)
            }
            return readOnlyHandler as? T
        }
        return handlers.computeIfAbsent(side) { proxyCreator.create(it, holder) } as? T
    }

    fun interface ProxyCreator<HOLDER, HANDLER> {
        fun create(side: Direction?, holder: HOLDER): HANDLER
    }
}
