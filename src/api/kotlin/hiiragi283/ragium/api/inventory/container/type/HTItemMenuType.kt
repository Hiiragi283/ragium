package hiiragi283.ragium.api.inventory.container.type

import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.neoforged.api.distmarker.Dist
import net.neoforged.neoforge.network.IContainerFactory

/**
 * [HTItemContainerFactory]を受け取る[HTMenuType]の拡張クラス
 * @see mekanism.common.inventory.container.type.MekanismItemContainerType
 */
class HTItemMenuType<MENU : AbstractContainerMenu>(factory: HTItemContainerFactory<MENU>, constructor: IContainerFactory<MENU>) :
    HTMenuType<MENU, HTItemContainerFactory<MENU>>(factory, constructor) {
    /**
     * 指定された[hand]と[stack]から[MenuConstructor]を返します。
     * @return [ImmutableItemStack]が`null`の場合は`null`
     */
    fun create(hand: InteractionHand?, stack: ImmutableItemStack?): MenuConstructor? {
        if (stack == null) return null
        return MenuConstructor { containerId: Int, inventory: Inventory, _: Player ->
            factory.create(containerId, inventory, hand, stack, Dist.DEDICATED_SERVER)
        }
    }
}
