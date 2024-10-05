package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.HTAlchemicalInfuserHandler
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

object RagiumScreenHandlerTypes {
    @JvmField
    val MACHINE: ScreenHandlerType<HTMachineScreenHandler> =
        register("machine", ::HTMachineScreenHandler)

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
