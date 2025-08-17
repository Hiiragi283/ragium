package hiiragi283.ragium.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[MenuType]向けの[DeferredRegister]
 */
class HTMenuTypeRegister(namespace: String) : DeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    /**
     * [MenuType.MenuSupplier]から[HTDeferredMenuType]を返します。
     * @param MENU [AbstractContainerMenu]を継承したクラス
     * @param name [MenuType]のID
     * @param constructor [MENU]を返すブロック
     */
    fun <MENU : AbstractContainerMenu> registerType(name: String, constructor: MenuType.MenuSupplier<MENU>): HTDeferredMenuType<MENU> {
        val holder: HTDeferredMenuType<MENU> =
            HTDeferredMenuType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> MenuType(constructor, FeatureFlags.VANILLA_SET) }
        return holder
    }

    /**
     * [IContainerFactory]から[HTDeferredMenuType]を返します。
     * @param MENU [AbstractContainerMenu]を継承したクラス
     * @param name [MenuType]のID
     * @param constructor [MENU]を返すブロック
     */
    fun <MENU : AbstractContainerMenu> registerType(name: String, constructor: IContainerFactory<MENU>): HTDeferredMenuType<MENU> {
        val holder: HTDeferredMenuType<MENU> =
            HTDeferredMenuType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> MenuType(constructor, FeatureFlags.VANILLA_SET) }
        return holder
    }
}
