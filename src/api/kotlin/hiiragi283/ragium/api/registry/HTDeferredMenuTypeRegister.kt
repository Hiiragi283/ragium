package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTMenuType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[MenuType]向けの[DeferredRegister]
 */
class HTDeferredMenuTypeRegister(namespace: String) : DeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    fun <MENU : HTContainerMenu, C> registerType(
        name: String,
        factory: HTContainerFactory<MENU, C>,
        decoder: (RegistryFriendlyByteBuf?) -> C,
    ): HTDeferredMenuType<MENU, C> {
        val holder: HTDeferredMenuType<MENU, C> =
            HTDeferredMenuType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> HTMenuType(factory, decoder) }
        return holder
    }
}
