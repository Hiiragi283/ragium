package hiiragi283.ragium.api.inventory.container.type

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.neoforged.neoforge.network.IContainerFactory

/**
 * [HTContainerFactory]を受け取る[HTMenuType]の拡張クラス
 * @see mekanism.common.inventory.container.type.MekanismContainerType
 */
class HTMenuTypeWithContext<MENU : AbstractContainerMenu, C>(
    private val clazz: Class<C>,
    factory: HTContainerFactory<MENU, C>,
    constructor: IContainerFactory<MENU>,
) : HTMenuType<MENU, HTContainerFactory<MENU, C>>(
        factory,
        constructor,
    ),
    HTContainerFactory<MENU, C> by factory {
    /**
     * 指定された[context]から[MenuConstructor]を返します。
     */
    fun create(context: C): MenuConstructor = MenuConstructor { containerId: Int, inventory: Inventory, _: Player ->
        factory.create(containerId, inventory, context)
    }

    fun createUnchecked(obj: Any): MenuConstructor? = when {
        clazz.isInstance(obj) -> {
            val context: C = clazz.cast(obj)
            create(context)
        }
        else -> {
            RagiumAPI.LOGGER.error("Failed to cast ${obj::class.java.simpleName} into ${clazz.simpleName}")
            null
        }
    }
}
