package hiiragi283.ragium.api.registry.impl

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
class HTDeferredMenuType<MENU : AbstractContainerMenu> private constructor(key: ResourceKey<MenuType<*>>) :
    HTDeferredHolder<MenuType<*>, MenuType<MENU>>(key) {
        companion object {
            @JvmStatic
            fun <MENU : AbstractContainerMenu> createType(key: ResourceLocation): HTDeferredMenuType<MENU> =
                createType(ResourceKey.create(Registries.MENU, key))

            @JvmStatic
            fun <MENU : AbstractContainerMenu> createType(key: ResourceKey<MenuType<*>>): HTDeferredMenuType<MENU> = HTDeferredMenuType(key)
        }

        /**
         * 指定された[title]から[MenuProvider]を返します。
         * @param title このGUIのタイトル
         * @return [MenuType.create]に基づいた[SimpleMenuProvider]
         */
        fun getProvider(title: Component): MenuProvider = SimpleMenuProvider(
            { id: Int, inv: Inventory, _: Player -> get().create(id, inv) },
            title,
        )

        /**
         * 指定された[title]と[context]から[MenuProvider]を返します。
         * @param C [context]の型
         * @param title このGUIのタイトル
         * @return [get]の戻り値が[HTContainerFactory]を継承していない場合は`null`
         */
        @Suppress("UNCHECKED_CAST")
        fun <C> getProvider(title: Component, context: C): MenuProvider? {
            val menuType: HTMenuTypeWithContext<MENU, C> = get() as? HTMenuTypeWithContext<MENU, C> ?: return null
            return SimpleMenuProvider(menuType.create(context), title)
        }

        /**
         * 指定された[hand], [stack]から[MenuProvider]を返します。
         * @param hand このGUIを開く[stack]を持っている[InteractionHand]
         * @param stack このGUIを開く[ItemStack]
         * @return [get]の戻り値が[HTItemContainerFactory]を継承していない場合は`null`
         */
        @Suppress("UNCHECKED_CAST")
        fun getProvider(hand: InteractionHand, stack: ItemStack): MenuProvider? {
            val menuType: HTItemMenuType<MENU> = get() as? HTItemMenuType<MENU> ?: return null
            val constructor: MenuConstructor = menuType.create(hand, stack) ?: return null
            return SimpleMenuProvider(constructor, stack.hoverName)
        }

        /**
         * 指定された[player], [title], [context]からGUIを開きます。
         * @param C [context]の型
         * @param player このGUIを開く[Player]
         * @param title このGUIのタイトル
         * @param writer [RegistryFriendlyByteBuf]に追加の情報を書き込むブロック
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME], [getProvider]が`null`の場合は[InteractionResult.FAIL]
         */
        fun <C> openMenu(
            player: Player,
            title: Component,
            context: C,
            writer: (RegistryFriendlyByteBuf) -> Unit,
        ): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                val provider: MenuProvider = getProvider(title, context) ?: return InteractionResult.FAIL
                player.openMenu(provider, writer)
                InteractionResult.CONSUME
            }
        }

        /**
         * 指定された[player], [hand], [stack]からGUIを開きます。
         * @param player このGUIを開く[Player]
         * @param hand このGUIを開く[stack]を持っている[InteractionHand]
         * @param stack このGUIを開く[ItemStack]
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME], [getProvider]が`null`の場合は[InteractionResult.FAIL]
         */
        fun openMenu(player: Player, hand: InteractionHand, stack: ItemStack): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                val provider: MenuProvider = getProvider(hand, stack) ?: return InteractionResult.FAIL
                player.openMenu(provider) { buf: RegistryFriendlyByteBuf ->
                    buf.writeEnum(hand)
                    ItemStack.STREAM_CODEC.encode(buf, stack)
                }
                InteractionResult.CONSUME
            }
        }
    }
