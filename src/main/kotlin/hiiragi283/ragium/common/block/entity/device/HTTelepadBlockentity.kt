package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumMenuTypes
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

    private lateinit var tank: HTVariableFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTVariableFluidStackTank.input(
            listener,
            RagiumConfig.COMMON.deviceCollectorTankCapacity,
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

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.store("teleport_pos", HTTeleportPos.CODEC, teleportPos)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        teleportPos = input.read("teleport_pos", HTTeleportPos.CODEC)
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
