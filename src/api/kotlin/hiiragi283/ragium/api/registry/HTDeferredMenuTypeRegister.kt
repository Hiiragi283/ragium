package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTItemContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTItemMenuType
import hiiragi283.ragium.api.inventory.container.type.HTMenuTypeWithContext
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[MenuType]向けの[DeferredRegister]
 * @see [mekanism.common.registration.impl.ContainerTypeDeferredRegister]
 */
class HTDeferredMenuTypeRegister(namespace: String) : DeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    /**
     * 指定された引数から[HTMenuTypeWithContext]を登録します。
     * @param MENU メニューのクラス
     * @param C コンテキストのクラス
     * @param decoder [RegistryFriendlyByteBuf]から[C]に変換するブロック
     * @return 登録された[MenuType]の[HTDeferredMenuType]
     */
    fun <MENU : HTContainerMenu, C : Any> registerType(
        name: String,
        factory: HTContainerFactory<MENU, C>,
        decoder: (RegistryFriendlyByteBuf?) -> C,
    ): HTDeferredMenuType<MENU> {
        val holder: HTDeferredMenuType<MENU> =
            HTDeferredMenuType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation ->
            HTMenuTypeWithContext(factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
                factory.create(containerId, inventory, decoder(buf))
            }
        }
        return holder
    }

    /**
     * 指定された引数から[HTItemMenuType]を登録します。
     * @param MENU メニューのクラス
     * @return 登録された[MenuType]の[HTDeferredMenuType]
     */
    fun <MENU : HTContainerMenu> registerItemType(name: String, factory: HTItemContainerFactory<MENU>): HTDeferredMenuType<MENU> {
        val holder: HTDeferredMenuType<MENU> =
            HTDeferredMenuType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation ->
            HTItemMenuType(factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
                checkNotNull(buf)
                val hand: InteractionHand = buf.readEnum(InteractionHand::class.java)
                val stack: ItemStack = ItemStack.STREAM_CODEC.decode(buf)
                factory.create(containerId, inventory, hand, stack, true)
            }
        }
        return holder
    }
}
