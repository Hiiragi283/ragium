package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTItemContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTItemMenuType
import hiiragi283.ragium.api.inventory.container.type.HTMenuTypeWithContext
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.api.distmarker.Dist
import java.util.function.Function

/**
 * Ragiumで使用する[MenuType]向けの[HTDeferredRegister]
 * @see mekanism.common.registration.impl.ContainerTypeDeferredRegister
 */
class HTDeferredMenuTypeRegister(namespace: String) : HTDeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    /**
     * 指定された引数から[HTMenuTypeWithContext]を登録します。
     * @param MENU メニューのクラス
     * @param C コンテキストのクラス
     * @param decoder [RegistryFriendlyByteBuf]から[C]に変換するブロック
     * @return 登録された[MenuType]の[HTDeferredMenuType.WithContext]
     */
    inline fun <MENU : AbstractContainerMenu, reified C : Any> registerType(
        name: String,
        factory: HTContainerFactory<MENU, C>,
        decoder: Function<RegistryFriendlyByteBuf?, C>,
    ): HTDeferredMenuType.WithContext<MENU, C> = registerType(C::class.java, name, factory, decoder)

    /**
     * 指定された引数から[HTMenuTypeWithContext]を登録します。
     * @param MENU メニューのクラス
     * @param C コンテキストのクラス
     * @param decoder [RegistryFriendlyByteBuf]から[C]に変換するブロック
     * @return 登録された[MenuType]の[HTDeferredMenuType.WithContext]
     */
    fun <MENU : AbstractContainerMenu, C : Any> registerType(
        clazz: Class<C>,
        name: String,
        factory: HTContainerFactory<MENU, C>,
        decoder: Function<RegistryFriendlyByteBuf?, C>,
    ): HTDeferredMenuType.WithContext<MENU, C> {
        val holder = HTDeferredMenuType.WithContext<MENU, C>(createId(name))
        register(name) { _: ResourceLocation ->
            HTMenuTypeWithContext(clazz, factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
                factory.create(containerId, inventory, decoder.apply(buf))
            }
        }
        return holder
    }

    /**
     * 指定された引数から[HTItemMenuType]を登録します。
     * @param MENU メニューのクラス
     * @return 登録された[MenuType]の[HTDeferredMenuType.OnHand]
     */
    fun <MENU : AbstractContainerMenu> registerItemType(
        name: String,
        factory: HTItemContainerFactory<MENU>,
    ): HTDeferredMenuType.OnHand<MENU> {
        val holder = HTDeferredMenuType.OnHand<MENU>(createId(name))
        register(name) { _: ResourceLocation ->
            HTItemMenuType(factory) { containerId: Int, inventory: Inventory, buf: RegistryFriendlyByteBuf? ->
                checkNotNull(buf)
                HTItemContainerContext.CODEC
                    .decode(buf)
                    .map { context: HTItemContainerContext ->
                        factory.create(containerId, inventory, context, Dist.CLIENT)
                    }.getOrThrow()
            }
        }
        return holder
    }
}
