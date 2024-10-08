package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.HTAlchemicalInfuserHandler
import hiiragi283.ragium.common.screen.HTGeneratorScreenHandler
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

object RagiumScreenHandlerTypes {
    @JvmField
    val GENERATOR: ScreenHandlerType<HTGeneratorScreenHandler> =
        register("generator", ::HTGeneratorScreenHandler)

    @JvmField
    val PROCESSOR: ScreenHandlerType<HTProcessorScreenHandler> =
        register("processor", ::HTProcessorScreenHandler)

    @JvmField
    val ALCHEMICAL_INFUSER: ScreenHandlerType<HTAlchemicalInfuserHandler> =
        register("alchemical_infuser", ::HTAlchemicalInfuserHandler)

    @JvmStatic
    private fun <T : ScreenHandler> register(name: String, factory: ScreenHandlerType.Factory<T>): ScreenHandlerType<T> = Registry.register(
        Registries.SCREEN_HANDLER,
        RagiumAPI.id(name),
        ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES),
    )
}
