package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@ConsistentCopyVisibility
@JvmRecord
data class HTUpdateFluidTankPacket private constructor(val pos: BlockPos, val stacks: List<Optional<ImmutableFluidStack>>) :
    HTCustomPayload.S2C {
        companion object {
            @JvmField
            val TYPE: CustomPacketPayload.Type<HTUpdateFluidTankPacket> =
                CustomPacketPayload.Type(RagiumAPI.id("update_fluid_tank"))

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateFluidTankPacket> = StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                HTUpdateFluidTankPacket::pos,
                ImmutableFluidStack.OPTIONAL_CODEC.listOf().streamCodec,
                HTUpdateFluidTankPacket::stacks,
                ::HTUpdateFluidTankPacket,
            )

            @JvmStatic
            fun create(blockEntity: HTBlockEntity): HTUpdateFluidTankPacket = HTUpdateFluidTankPacket(
                blockEntity.blockPos,
                blockEntity
                    .getFluidTanks(blockEntity.getFluidSideFor())
                    .map(HTFluidTank::getStack)
                    .map(Optional<ImmutableFluidStack>::ofNullable),
            )
        }

        override fun type(): CustomPacketPayload.Type<HTUpdateFluidTankPacket> = TYPE

        override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
            val screen: HTFluidScreen = minecraft.screen as? HTFluidScreen ?: return
            if (!screen.checkPosition(pos)) return
            val widgets: List<HTFluidWidget> = screen.getFluidWidgets()
            if (widgets.isEmpty()) return
            for (i: Int in widgets.indices) {
                widgets[i].setStack(stacks[i].getOrNull())
            }
        }
    }
