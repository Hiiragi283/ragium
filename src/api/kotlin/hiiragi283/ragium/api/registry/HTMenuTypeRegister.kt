package hiiragi283.ragium.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredRegister

class HTMenuTypeRegister(namespace: String) : DeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    fun <T : AbstractContainerMenu> registerType(name: String, constructor: MenuType.MenuSupplier<T>): HTDeferredMenuType<T> {
        val holder: HTDeferredMenuType<T> =
            HTDeferredMenuType.createType<T>(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> MenuType(constructor, FeatureFlags.VANILLA_SET) }
        return holder
    }

    fun <T : AbstractContainerMenu> registerType(name: String, constructor: IContainerFactory<T>): HTDeferredMenuType<T> {
        val holder: HTDeferredMenuType<T> =
            HTDeferredMenuType.createType<T>(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> MenuType(constructor, FeatureFlags.VANILLA_SET) }
        return holder
    }
}
