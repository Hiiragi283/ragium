package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.common.screen.HTGeneratorScreenHandler
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
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
    val GENERATOR: ScreenHandlerType<HTGeneratorScreenHandler> =
        register("generator", ::HTGeneratorScreenHandler)

    @JvmField
    val PROCESSOR: ExtendedScreenHandlerType<HTProcessorScreenHandler, HTMachinePacket> =
        registerExtended("processor", ::HTProcessorScreenHandler, HTMachinePacket.PACKET_CODEC)

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
