package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.common.inventory.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMenuTypes {
    @JvmField
    val REGISTER: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T : HTMachineContainerMenu> registerMachine(
        path: String,
        factory: IContainerFactory<T>,
    ): DeferredHolder<MenuType<*>, MenuType<T>> =
        REGISTER.register(path) { _: ResourceLocation -> MenuType(factory, FeatureFlags.VANILLA_SET) }

    @JvmField
    val EXTRACTOR: DeferredHolder<MenuType<*>, MenuType<HTExtractorContainerMenu>> =
        registerMachine("extractor", ::HTExtractorContainerMenu)

    @JvmField
    val INFUSER: DeferredHolder<MenuType<*>, MenuType<HTInfuserContainerMenu>> =
        registerMachine("infuser", ::HTInfuserContainerMenu)

    @JvmField
    val MIXER: DeferredHolder<MenuType<*>, MenuType<HTMixerContainerMenu>> =
        registerMachine("mixer", ::HTMixerContainerMenu)

    @JvmField
    val MULTI_ITEM: DeferredHolder<MenuType<*>, MenuType<HTMultiItemContainerMenu>> =
        registerMachine("multi_item", ::HTMultiItemContainerMenu)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<MenuType<*>, MenuType<HTMultiSmelterContainerMenu>> =
        registerMachine("multi_smelter", ::HTMultiSmelterContainerMenu)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredHolder<MenuType<*>, MenuType<HTPrimitiveBlastFurnaceContainerMenu>> =
        registerMachine("primitive_blast_furnace", ::HTPrimitiveBlastFurnaceContainerMenu)

    @JvmField
    val REFINERY: DeferredHolder<MenuType<*>, MenuType<HTRefineryContainerMenu>> =
        registerMachine("refinery", ::HTRefineryContainerMenu)

    @JvmField
    val SINGLE_ITEM: DeferredHolder<MenuType<*>, MenuType<HTSingleItemContainerMenu>> =
        registerMachine("single_item", ::HTSingleItemContainerMenu)
}
