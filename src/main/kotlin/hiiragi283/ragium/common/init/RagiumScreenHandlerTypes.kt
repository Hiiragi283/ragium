package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachinePacket
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
    val CHEMICAL_MACHINE: ExtendedScreenHandlerType<HTChemicalMachineScreenHandler, HTMachinePacket> =
        registerExtended("chemical_machine", ::HTChemicalMachineScreenHandler, HTMachinePacket.PACKET_CODEC)

    @JvmField
    val FLUID_DRILL: ExtendedScreenHandlerType<HTFluidDrillScreenHandler, HTMachinePacket> =
        registerExtended("fluid_drill", ::HTFluidDrillScreenHandler, HTMachinePacket.PACKET_CODEC)

    @JvmField
    val LARGE_MACHINE: ExtendedScreenHandlerType<HTLargeMachineScreenHandler, HTMachinePacket> =
        registerExtended("large_machine", ::HTLargeMachineScreenHandler, HTMachinePacket.PACKET_CODEC)

    @JvmField
    val SIMPLE_MACHINE: ExtendedScreenHandlerType<HTSimpleMachineScreenHandler, HTMachinePacket> =
        registerExtended("simple_machine", ::HTSimpleMachineScreenHandler, HTMachinePacket.PACKET_CODEC)

    @JvmField
    val STEAM: ExtendedScreenHandlerType<HTSteamGeneratorScreenHandler, HTMachinePacket> =
        registerExtended("steam_generator", ::HTSteamGeneratorScreenHandler, HTMachinePacket.PACKET_CODEC)

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
