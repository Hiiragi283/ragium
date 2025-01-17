package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.common.inventory.HTDefaultMachineContainerMenu
import hiiragi283.ragium.common.inventory.HTDistillationTowerContainerMenu
import hiiragi283.ragium.common.inventory.HTLargeMachineContainerMenu
import hiiragi283.ragium.common.inventory.HTMultiSmelterContainerMenu
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMenuTypes {
    @JvmField
    val REGISTER: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T : HTContainerMenu> register(path: String, factory: MenuType.MenuSupplier<T>): DeferredHolder<MenuType<*>, MenuType<T>> =
        REGISTER.register(path) { _: ResourceLocation ->
            MenuType(factory, FeatureFlags.DEFAULT_FLAGS)
        }

    @JvmField
    val DEFAULT_MACHINE: DeferredHolder<MenuType<*>, MenuType<HTDefaultMachineContainerMenu>> =
        register("default_machine", ::HTDefaultMachineContainerMenu)

    @JvmField
    val LARGE_MACHINE: DeferredHolder<MenuType<*>, MenuType<HTLargeMachineContainerMenu>> =
        register("large_machine", ::HTLargeMachineContainerMenu)

    @JvmField
    val DISTILLATION_TOWER: DeferredHolder<MenuType<*>, MenuType<HTDistillationTowerContainerMenu>> =
        register("distillation_tower", ::HTDistillationTowerContainerMenu)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<MenuType<*>, MenuType<HTMultiSmelterContainerMenu>> =
        register("multi_smelter", ::HTMultiSmelterContainerMenu)
}
