package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTItemContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTItemMenuType
import hiiragi283.ragium.api.inventory.container.type.HTMenuTypeWithContext
import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

/**
 * Ragiumで使用する[MenuType]向けの[HTDeferredHolder]
 * @see [mekanism.common.registration.impl.ContainerTypeRegistryObject]
 */
sealed class HTDeferredMenuType<MENU : AbstractContainerMenu, TYPE : MenuType<MENU>> : HTDeferredHolder<MenuType<*>, TYPE> {
    constructor(key: ResourceKey<MenuType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.MENU, id)

    /**
     * 指定された[title]から[MenuProvider]を返します。
     * @param title このGUIのタイトル
     * @return [MenuType.create]に基づいた[SimpleMenuProvider]
     */
    fun getVanillaProvider(title: Component): MenuProvider = SimpleMenuProvider(
        { id: Int, inv: Inventory, _: Player -> get().create(id, inv) },
        title,
    )

    //    WithContext    //

    class WithContext<MENU : AbstractContainerMenu, C> : HTDeferredMenuType<MENU, HTMenuTypeWithContext<MENU, C>> {
        constructor(key: ResourceKey<MenuType<*>>) : super(key)

        constructor(id: ResourceLocation) : super(id)

        /**
         * 指定された[title]と[context]から[MenuProvider]を返します。
         * @param C [context]の型
         * @param title このGUIのタイトル
         * @return [get]の戻り値が[HTContainerFactory]を継承していない場合は`null`
         */
        fun getProvider(title: Component, context: C): MenuProvider = SimpleMenuProvider(get().create(context), title)

        /**
         * 指定された[player], [title], [context]からGUIを開きます。
         * @param C [context]の型
         * @param player このGUIを開く[Player]
         * @param title このGUIのタイトル
         * @param writer [RegistryFriendlyByteBuf]に追加の情報を書き込むブロック
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME], [getProvider]が`null`の場合は[InteractionResult.FAIL]
         */
        fun openMenu(
            player: Player,
            title: Component,
            context: C,
            writer: (RegistryFriendlyByteBuf) -> Unit,
        ): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                val provider: MenuProvider = getProvider(title, context)
                player.openMenu(provider, writer)
                InteractionResult.CONSUME
            }
        }
    }

    //    OnHand    //

    class OnHand<MENU : AbstractContainerMenu> : HTDeferredMenuType<MENU, HTItemMenuType<MENU>> {
        constructor(key: ResourceKey<MenuType<*>>) : super(key)

        constructor(id: ResourceLocation) : super(id)

        /**
         * 指定された[hand], [stack]から[MenuProvider]を返します。
         * @param hand このGUIを開く[stack]を持っている[InteractionHand]
         * @param stack このGUIを開く[ItemStack]
         * @return [get]の戻り値が[HTItemContainerFactory]を継承していない場合は`null`
         */
        fun getProvider(hand: InteractionHand?, stack: ItemStack): MenuProvider? {
            val constructor: MenuConstructor = get().create(hand, stack) ?: return null
            return SimpleMenuProvider(constructor, stack.hoverName)
        }

        /**
         * 指定された[player], [hand], [stack]からGUIを開きます。
         * @param player このGUIを開く[Player]
         * @param hand このGUIを開く[stack]を持っている[InteractionHand]
         * @param stack このGUIを開く[ItemStack]
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME], [getProvider]が`null`の場合は[InteractionResult.FAIL]
         */
        fun openMenu(player: Player, hand: InteractionHand?, stack: ItemStack): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                val provider: MenuProvider = getProvider(hand, stack) ?: return InteractionResult.FAIL
                player.openMenu(provider) { buf: RegistryFriendlyByteBuf ->
                    val context = HTItemContainerContext(hand, stack)
                    HTItemContainerContext.CODEC.encode(buf, context)
                }
                InteractionResult.CONSUME
            }
        }
    }
}
