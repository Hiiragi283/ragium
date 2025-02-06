package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import hiiragi283.ragium.api.inventory.HTMachineMenuType
import hiiragi283.ragium.common.inventory.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
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
    ): DeferredHolder<MenuType<*>, HTMachineMenuType<T>> = REGISTER.register(path) { _: ResourceLocation -> HTMachineMenuType(factory) }

    @JvmField
    val COMPRESSOR: DeferredHolder<MenuType<*>, HTMachineMenuType<HTCompressorContainerMenu>> =
        registerMachine("compressor", ::HTCompressorContainerMenu)

    @JvmField
    val EXTRACTOR: DeferredHolder<MenuType<*>, HTMachineMenuType<HTExtractorContainerMenu>> =
        registerMachine("extractor", ::HTExtractorContainerMenu)

    @JvmField
    val GRINDER: DeferredHolder<MenuType<*>, HTMachineMenuType<HTGrinderContainerMenu>> =
        registerMachine("grinder", ::HTGrinderContainerMenu)

    @JvmField
    val INFUSER: DeferredHolder<MenuType<*>, HTMachineMenuType<HTInfuserContainerMenu>> =
        registerMachine("infuser", ::HTInfuserContainerMenu)

    @JvmField
    val MIXER: DeferredHolder<MenuType<*>, HTMachineMenuType<HTMixerContainerMenu>> =
        registerMachine("mixer", ::HTMixerContainerMenu)

    @JvmField
    val MULTI_ITEM: DeferredHolder<MenuType<*>, HTMachineMenuType<HTMultiItemContainerMenu>> =
        registerMachine("multi_item", ::HTMultiItemContainerMenu)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<MenuType<*>, HTMachineMenuType<HTMultiSmelterContainerMenu>> =
        registerMachine("multi_smelter", ::HTMultiSmelterContainerMenu)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredHolder<MenuType<*>, HTMachineMenuType<HTPrimitiveBlastFurnaceContainerMenu>> =
        registerMachine("primitive_blast_furnace", ::HTPrimitiveBlastFurnaceContainerMenu)

    @JvmField
    val REFINERY: DeferredHolder<MenuType<*>, HTMachineMenuType<HTRefineryContainerMenu>> =
        registerMachine("refinery", ::HTRefineryContainerMenu)
}
