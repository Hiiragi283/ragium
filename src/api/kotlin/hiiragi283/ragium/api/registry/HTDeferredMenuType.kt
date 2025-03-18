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

class HTDeferredMenuType<T : AbstractContainerMenu> private constructor(key: ResourceKey<MenuType<*>>) :
    DeferredHolder<MenuType<*>, MenuType<T>>(key) {
        companion object {
            @JvmStatic
            fun <T : AbstractContainerMenu> createType(key: ResourceLocation): HTDeferredMenuType<T> = createType<T>(
                ResourceKey.create(
                    Registries.MENU,
                    key,
                ),
            )

            @JvmStatic
            fun <T : AbstractContainerMenu> createType(key: ResourceKey<MenuType<*>>): HTDeferredMenuType<T> = HTDeferredMenuType<T>(key)
        }

        fun getConstructor(): MenuConstructor = MenuConstructor { id: Int, inv: Inventory, player: Player -> get().create(id, inv) }

        fun getProvider(title: Component): SimpleMenuProvider = SimpleMenuProvider(getConstructor(), title)

        fun openMenu(player: Player, title: Component): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title))
                InteractionResult.CONSUME
            }
        }

        fun openMenu(player: Player, title: Component, writer: (RegistryFriendlyByteBuf) -> Unit): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title), writer)
                InteractionResult.CONSUME
            }
        }

        fun openMenu(player: Player, title: Component, pos: BlockPos): InteractionResult = when {
            player.level().isClientSide -> InteractionResult.SUCCESS
            else -> {
                player.openMenu(getProvider(title), pos)
                InteractionResult.CONSUME
            }
        }
    }
