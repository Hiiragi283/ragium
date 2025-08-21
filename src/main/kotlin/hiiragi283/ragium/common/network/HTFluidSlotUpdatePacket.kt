package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.network.HTCustomPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidSlotUpdatePacket(val pos: BlockPos, val index: Int, val stack: FluidStack) : HTCustomPayload.S2C {
    companion object {
        @JvmField
        val TYPE = CustomPacketPayload.Type<HTFluidSlotUpdatePacket>(RagiumAPI.id("fluid_slot_update"))

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidSlotUpdatePacket> = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            HTFluidSlotUpdatePacket::pos,
            ByteBufCodecs.VAR_INT,
            HTFluidSlotUpdatePacket::index,
            FluidStack.OPTIONAL_STREAM_CODEC,
            HTFluidSlotUpdatePacket::stack,
            ::HTFluidSlotUpdatePacket,
        )
    }

    override fun type(): CustomPacketPayload.Type<HTFluidSlotUpdatePacket> = TYPE

    override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
        val screen: Screen = minecraft.screen ?: return
        if (screen is HTContainerScreen<*>) {
            val menu: HTContainerMenu = screen.menu
            if (menu is HTContainerWithContextMenu<*>) {
                val blockEntity: BlockEntity = menu.context as? BlockEntity ?: return
                if (blockEntity.blockPos != pos) return
                if (screen is HTFluidScreen) {
                    screen.setFluidStack(index, stack)
                }
            }
        }
    }
}
