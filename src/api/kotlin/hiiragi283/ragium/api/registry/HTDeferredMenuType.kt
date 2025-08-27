package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTMenuType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * Ragiumで使用する[MenuType]向けの[DeferredHolder]
 */
class HTDeferredMenuType<MENU : AbstractContainerMenu, C : Any> private constructor(key: ResourceKey<MenuType<*>>) :
    DeferredHolder<MenuType<*>, HTMenuType<MENU, C>>(key) {
        companion object {
            @JvmStatic
            fun <MENU : AbstractContainerMenu, C : Any> createType(key: ResourceLocation): HTDeferredMenuType<MENU, C> =
                createType(ResourceKey.create(Registries.MENU, key))

            @JvmStatic
            fun <MENU : AbstractContainerMenu, C : Any> createType(key: ResourceKey<MenuType<*>>): HTDeferredMenuType<MENU, C> =
                HTDeferredMenuType(key)
        }

        /**
         * 指定された[title]と[context]から[HTContainerFactory]に基づいて[SimpleMenuProvider]を返します。
         */
        fun getProvider(title: Component, context: C): SimpleMenuProvider = SimpleMenuProvider(
            { id: Int, inv: Inventory, _: Player -> get().create(id, inv, context) },
            title,
        )

        fun openMenu(
            player: Player,
            title: Component,
            context: C,
            streamCodec: BiCodec<RegistryFriendlyByteBuf, C>,
        ): InteractionResult = openMenu(
            player,
            title,
            context,
        ) { buf: RegistryFriendlyByteBuf -> streamCodec.encode(buf, context) }

        /**
         * 指定された[player], [title], [writer]からGUIを開きます。
         * @param writer [RegistryFriendlyByteBuf]に追加の情報を書き込むブロック
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME]
         */
        fun openMenu(
            player: Player,
            title: Component,
            context: C,
            writer: (RegistryFriendlyByteBuf) -> Unit,
        ): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title, context), writer)
                InteractionResult.CONSUME
            }
        }
    }
