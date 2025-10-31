package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.network.HTCustomPayload
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@ConsistentCopyVisibility
@JvmRecord
data class HTUpdateFluidTankPacket private constructor(val pos: BlockPos, val index: Int, val stack: Optional<ImmutableFluidStack>) :
    HTCustomPayload.S2C {
        companion object {
            @JvmField
            val TYPE: CustomPacketPayload.Type<HTUpdateFluidTankPacket> =
                CustomPacketPayload.Type(RagiumAPI.id("update_fluid_tank"))

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTUpdateFluidTankPacket> = StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                HTUpdateFluidTankPacket::pos,
                ByteBufCodecs.VAR_INT,
                HTUpdateFluidTankPacket::index,
                ImmutableFluidStack.OPTIONAL_CODEC.streamCodec,
                HTUpdateFluidTankPacket::stack,
                ::HTUpdateFluidTankPacket,
            )

            @JvmStatic
            fun create(blockEntity: HTBlockEntity, index: Int): HTUpdateFluidTankPacket? {
                val tank: HTFluidTank = blockEntity.getFluidTanks(blockEntity.getFluidSideFor()).getOrNull(index) ?: return null
                return HTUpdateFluidTankPacket(blockEntity.blockPos, index, tank.getStack())
            }
        }

        constructor(pos: BlockPos, index: Int, stack: ImmutableFluidStack?) : this(pos, index, Optional.ofNullable(stack))

        override fun type(): CustomPacketPayload.Type<HTUpdateFluidTankPacket> = TYPE

        override fun handle(player: AbstractClientPlayer, minecraft: Minecraft) {
            val screen: HTFluidScreen = minecraft.screen as? HTFluidScreen ?: return
            if (!screen.checkPosition(pos)) return
            screen.getFluidWidgets().getOrNull(index)?.setStack(stack.getOrNull())
        }
    }
