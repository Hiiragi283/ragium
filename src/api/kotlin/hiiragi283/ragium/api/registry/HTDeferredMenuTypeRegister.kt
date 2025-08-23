package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.inventory.container.type.HTContainerFactory
import hiiragi283.ragium.api.inventory.container.type.HTMenuType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[MenuType]向けの[DeferredRegister]
 */
class HTDeferredMenuTypeRegister(namespace: String) : DeferredRegister<MenuType<*>>(Registries.MENU, namespace) {
    /**
     * [HTContainerFactory]から[HTDeferredMenuType]を返します。
     * @param MENU [HTContainerWithContextMenu]を継承したクラス
     * @param BE [BlockEntity]を継承したクラス
     * @param name [MenuType]のID
     * @param factory [MENU]を返すブロック
     */
    inline fun <MENU : HTContainerWithContextMenu<BE>, reified BE : BlockEntity> registerType(
        name: String,
        factory: HTContainerFactory<MENU, BE>,
    ): HTDeferredMenuType<MENU, BE> {
        val holder: HTDeferredMenuType<MENU, BE> =
            HTDeferredMenuType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> HTMenuType.blockEntity(factory) }
        return holder
    }
}
