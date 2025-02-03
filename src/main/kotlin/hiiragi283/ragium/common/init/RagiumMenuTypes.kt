package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.common.inventory.HTDefaultMachineContainerMenu
import hiiragi283.ragium.common.inventory.HTExtractorContainerMenu
import hiiragi283.ragium.common.inventory.HTLargeMachineContainerMenu
import hiiragi283.ragium.common.inventory.HTMultiSmelterContainerMenu
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMenuTypes {
    @JvmField
    val REGISTER: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T : HTContainerMenu> register(path: String, factory: MenuType.MenuSupplier<T>): DeferredHolder<MenuType<*>, MenuType<T>> =
        REGISTER.register(path) { _: ResourceLocation -> MenuType(factory, FeatureFlags.DEFAULT_FLAGS) }

    @JvmStatic
    fun <T : HTContainerMenu> registerExtended(path: String, factory: IContainerFactory<T>): DeferredHolder<MenuType<*>, MenuType<T>> =
        REGISTER.register(path) { _: ResourceLocation ->
            IMenuTypeExtension.create(factory)
        }

    @JvmField
    val EXTRACTOR: DeferredHolder<MenuType<*>, MenuType<HTExtractorContainerMenu>> =
        registerExtended("extractor", ::HTExtractorContainerMenu)

    @JvmField
    val DEFAULT_MACHINE: DeferredHolder<MenuType<*>, MenuType<HTDefaultMachineContainerMenu>> =
        registerExtended("default_machine", ::HTDefaultMachineContainerMenu)

    @JvmField
    val LARGE_MACHINE: DeferredHolder<MenuType<*>, MenuType<HTLargeMachineContainerMenu>> =
        registerExtended("large_machine", ::HTLargeMachineContainerMenu)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<MenuType<*>, MenuType<HTMultiSmelterContainerMenu>> =
        registerExtended("multi_smelter", ::HTMultiSmelterContainerMenu)
}
