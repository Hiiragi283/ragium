package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.common.inventory.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMenuTypes {
    @JvmField
    val REGISTER: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, RagiumAPI.MOD_ID)

    @JvmStatic
    fun <T : HTContainerMenu> registerExtended(path: String, factory: IContainerFactory<T>): DeferredHolder<MenuType<*>, MenuType<T>> =
        REGISTER.register(path) { _: ResourceLocation ->
            IMenuTypeExtension.create(factory)
        }

    @JvmField
    val COMPRESSOR: DeferredHolder<MenuType<*>, MenuType<HTCompressorContainerMenu>> =
        registerExtended("compressor", ::HTCompressorContainerMenu)

    @JvmField
    val EXTRACTOR: DeferredHolder<MenuType<*>, MenuType<HTExtractorContainerMenu>> =
        registerExtended("extractor", ::HTExtractorContainerMenu)

    @JvmField
    val INFUSER: DeferredHolder<MenuType<*>, MenuType<HTInfuserContainerMenu>> =
        registerExtended("infuser", ::HTInfuserContainerMenu)

    @JvmField
    val MIXER: DeferredHolder<MenuType<*>, MenuType<HTMixerContainerMenu>> =
        registerExtended("mixer", ::HTMixerContainerMenu)

    @JvmField
    val MULTI_ITEM: DeferredHolder<MenuType<*>, MenuType<HTMultiItemContainerMenu>> =
        registerExtended("multi_item", ::HTMultiItemContainerMenu)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<MenuType<*>, MenuType<HTMultiSmelterContainerMenu>> =
        registerExtended("multi_smelter", ::HTMultiSmelterContainerMenu)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredHolder<MenuType<*>, MenuType<HTPrimitiveBlastFurnaceContainerMenu>> =
        registerExtended("primitive_blast_furnace", ::HTPrimitiveBlastFurnaceContainerMenu)
}
