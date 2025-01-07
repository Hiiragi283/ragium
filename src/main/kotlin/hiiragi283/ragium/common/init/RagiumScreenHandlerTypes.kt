package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.*
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

object RagiumScreenHandlerTypes {
    @JvmField
    val CHEMICAL_MACHINE: ScreenHandlerType<HTChemicalMachineScreenHandler> =
        register("chemical_machine", ::HTChemicalMachineScreenHandler)

    @JvmField
    val DISTILLATION_TOWER: ScreenHandlerType<HTDistillationTowerScreenHandler> =
        register("distillation_tower", ::HTDistillationTowerScreenHandler)

    @JvmField
    val GRINDER: ScreenHandlerType<HTGrinderScreenHandler> =
        register("grinder", ::HTGrinderScreenHandler)

    @JvmField
    val LARGE_CHEMICAL_REACTOR: ScreenHandlerType<HTLargeChemicalReactorScreenHandler> =
        register("large_chemical_reactor", ::HTLargeChemicalReactorScreenHandler)

    @JvmField
    val LARGE_MACHINE: ScreenHandlerType<HTLargeMachineScreenHandler> =
        register("large_machine", ::HTLargeMachineScreenHandler)

    @JvmField
    val ROCK_GENERATOR: ScreenHandlerType<HTRockGeneratorScreenHandler> =
        register("rock_generator", ::HTRockGeneratorScreenHandler)

    @JvmField
    val SMALL_MACHINE: ScreenHandlerType<HTSmallMachineScreenHandler> =
        register("small_machine", ::HTSmallMachineScreenHandler)

    @JvmField
    val SIMPLE_MACHINE: ScreenHandlerType<HTSimpleMachineScreenHandler> =
        register("simple_machine", ::HTSimpleMachineScreenHandler)

    @JvmStatic
    private fun <T : ScreenHandler, D : Any> registerExtended(
        name: String,
        factory: ExtendedScreenHandlerType.ExtendedFactory<T, D>,
        packetCodec: PacketCodec<RegistryByteBuf, D>,
    ): ExtendedScreenHandlerType<T, D> = Registry.register(
        Registries.SCREEN_HANDLER,
        RagiumAPI.id(name),
        ExtendedScreenHandlerType(factory, packetCodec),
    )

    @JvmStatic
    private fun <T : ScreenHandler> register(name: String, factory: ScreenHandlerType.Factory<T>): ScreenHandlerType<T> = Registry.register(
        Registries.SCREEN_HANDLER,
        RagiumAPI.id(name),
        ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES),
    )
}
