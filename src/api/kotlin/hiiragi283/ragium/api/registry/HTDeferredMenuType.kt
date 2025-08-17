package hiiragi283.ragium.api.registry

import net.minecraft.core.BlockPos
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
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * Ragiumで使用する[MenuType]向けの[DeferredHolder]
 */
class HTDeferredMenuType<MENU : AbstractContainerMenu> private constructor(key: ResourceKey<MenuType<*>>) :
    DeferredHolder<MenuType<*>, MenuType<MENU>>(key) {
        companion object {
            @JvmStatic
            fun <MENU : AbstractContainerMenu> createType(key: ResourceLocation): HTDeferredMenuType<MENU> = createType(
                ResourceKey.create(
                    Registries.MENU,
                    key,
                ),
            )

            @JvmStatic
            fun <MENU : AbstractContainerMenu> createType(key: ResourceKey<MenuType<*>>): HTDeferredMenuType<MENU> = HTDeferredMenuType(key)
        }

        /**
         * [MenuType]を[MenuConstructor]に変換します。
         */
        fun getConstructor(): MenuConstructor = MenuConstructor { id: Int, inv: Inventory, _: Player -> get().create(id, inv) }

        /**
         * 指定された[title]から[getConstructor]に基づいて[SimpleMenuProvider]を返します。
         */
        fun getProvider(title: Component): SimpleMenuProvider = SimpleMenuProvider(getConstructor(), title)

        /**
         * 指定された[player]と[title]からGUIを開きます。
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME]
         */
        fun openMenu(player: Player, title: Component): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title))
                InteractionResult.CONSUME
            }
        }

        /**
         * 指定された[player], [title], [writer]からGUIを開きます。
         * @param writer [RegistryFriendlyByteBuf]に追加の情報を書き込むブロック
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME]
         */
        fun openMenu(player: Player, title: Component, writer: (RegistryFriendlyByteBuf) -> Unit): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title), writer)
                InteractionResult.CONSUME
            }
        }

        /**
         * 指定された[player], [title], [pos]からGUIを開きます。
         * @param pos GUIを開く座標
         * @return クライアント側の場合は[InteractionResult.SUCCESS]，サーバー側の場合はGUIを開いたうえで[InteractionResult.CONSUME]
         */
        fun openMenu(player: Player, title: Component, pos: BlockPos): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title), pos)
                InteractionResult.CONSUME
            }
        }
    }
