package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTMachineRecipePayload(val pos: BlockPos, val recipe: HTMachineRecipe) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipePayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTMachineRecipePayload::pos,
            HTMachineRecipe.PACKET_CODEC,
            HTMachineRecipePayload::recipe,
            ::HTMachineRecipePayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.MACHINE_RECIPE
}
