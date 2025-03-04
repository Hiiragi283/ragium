package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineMenu
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

    @JvmField
    val POTION_BUNDLE: DeferredHolder<MenuType<*>, MenuType<HTPotionBundleMenu>> =
        REGISTER.register("potion_bundle") { _: ResourceLocation ->
            MenuType(::HTPotionBundleMenu, FeatureFlags.VANILLA_SET)
        }

    //    Machine    //

    @JvmStatic
    fun <T : HTMachineMenu> registerMachine(path: String, factory: IContainerFactory<T>): DeferredHolder<MenuType<*>, MenuType<T>> =
        REGISTER.register(path) { _: ResourceLocation -> MenuType(factory, FeatureFlags.VANILLA_SET) }

    @JvmField
    val ASSEMBLER: DeferredHolder<MenuType<*>, MenuType<HTAssemblerMenu>> =
        registerMachine("assembler", ::HTAssemblerMenu)

    @JvmField
    val BLAST_FURNACE: DeferredHolder<MenuType<*>, MenuType<HTBlastFurnaceMenu>> =
        registerMachine("blast_furnace", ::HTBlastFurnaceMenu)

    @JvmField
    val BREWERY: DeferredHolder<MenuType<*>, MenuType<HTBreweryMenu>> =
        registerMachine("brewery", ::HTBreweryMenu)

    @JvmField
    val EXTRACTOR: DeferredHolder<MenuType<*>, MenuType<HTExtractorMenu>> =
        registerMachine("extractor", ::HTExtractorMenu)

    @JvmField
    val INFUSER: DeferredHolder<MenuType<*>, MenuType<HTInfuserMenu>> =
        registerMachine("infuser", ::HTInfuserMenu)

    @JvmField
    val MIXER: DeferredHolder<MenuType<*>, MenuType<HTMixerMenu>> =
        registerMachine("mixer", ::HTMixerMenu)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<MenuType<*>, MenuType<HTMultiSmelterMenu>> =
        registerMachine("multi_smelter", ::HTMultiSmelterMenu)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredHolder<MenuType<*>, MenuType<HTPrimitiveBlastFurnaceMenu>> =
        registerMachine("primitive_blast_furnace", ::HTPrimitiveBlastFurnaceMenu)

    @JvmField
    val REFINERY: DeferredHolder<MenuType<*>, MenuType<HTRefineryMenu>> =
        registerMachine("refinery", ::HTRefineryMenu)

    @JvmField
    val SINGLE_ITEM: DeferredHolder<MenuType<*>, MenuType<HTSingleItemMenu>> =
        registerMachine("single_item", ::HTSingleItemMenu)

    @JvmField
    val SOLIDIFIER: DeferredHolder<MenuType<*>, MenuType<HTSolidifierMenu>> =
        registerMachine("solidifier", ::HTSolidifierMenu)
}
