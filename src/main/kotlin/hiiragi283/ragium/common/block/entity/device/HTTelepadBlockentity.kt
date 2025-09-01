package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTTelepadBlockentity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(HTDeviceVariant.TELEPAD, pos, state),
    HTFluidInteractable {
    companion object {
        @JvmStatic
        fun validateStructure(level: Level, pos: BlockPos): Boolean = BlockPos
            .betweenClosedStream(-1, 0, -1, 1, 0, 1)
            .map(pos::offset)
            .filter { posIn: BlockPos -> posIn != pos }
            .map(level::getBlockState)
            .allMatch(RagiumBlocks.DEVICE_CASING::isOf)
    }

    private lateinit var tank: HTFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTFluidStackTank.input(
            listener,
            RagiumAPI.getConfig().getDeviceTankCapacity(),
            filter = RagiumFluidContents.DEW_OF_THE_WARP::isOf,
        )
        return HTSimpleFluidTankHolder.input(null, tank)
    }

    var teleportPos: HTTeleportPos? = null
        private set

    fun updateDestination(teleportPos: HTTeleportPos) {
        val server: MinecraftServer = RagiumAPI.getInstance().getCurrentServer() ?: return
        if (server.getLevel(teleportPos.dimension) != null) {
            this.teleportPos = teleportPos
            setOnlySave()
        }
    }

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.writeNullable(HTTeleportPos.CODEC, "teleport_pos", teleportPos)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(HTTeleportPos.CODEC, "teleport_pos").ifSuccess { teleportPos = it }
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = when {
        validateStructure(level, pos) -> RagiumMenuTypes.TELEPAD.openMenu(player, name, this, ::writeExtraContainerData)

        else -> {
            player.displayClientMessage(Component.literal("Telepad must be surrounded by Device Casing!"), true)
            InteractionResult.FAIL
        }
    }

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)
}
