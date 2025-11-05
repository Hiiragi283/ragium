package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.streams.asSequence

class HTTelepadBlockentity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(RagiumBlocks.TELEPAD, pos, state) {
    companion object {
        @JvmStatic
        fun validateStructure(level: Level, pos: BlockPos): Boolean = BlockPos
            .betweenClosedStream(-1, 0, -1, 1, 0, 1)
            .asSequence()
            .map(pos::offset)
            .filter { posIn: BlockPos -> posIn != pos }
            .map(level::getBlockState)
            .all(RagiumBlocks.DEVICE_CASING::isOf)
    }

    lateinit var tank: HTVariableFluidStackTank
        private set

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        tank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener,
                RagiumConfig.COMMON.deviceCollectorTankCapacity,
                filter = RagiumFluidContents.DEW_OF_THE_WARP::isOf,
            ),
        )
        return builder.build()
    }

    var teleportPos: HTTeleportPos? = null
        private set

    fun updateDestination(teleportPos: HTTeleportPos) {
        if (RagiumPlatform.INSTANCE.getLevel(teleportPos.dimension) != null) {
            this.teleportPos = teleportPos
            setOnlySave()
        }
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.store("teleport_pos", HTTeleportPos.CODEC, teleportPos)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        teleportPos = input.read("teleport_pos", HTTeleportPos.CODEC)
    }

    /*override fun onRightClicked(context: HTBlockInteractContext): InteractionResult = when {
        validateStructure(
            context.level,
            context.pos,
        ) -> RagiumMenuTypes.TELEPAD.openMenu(context.player, name, this, ::writeExtraContainerData)

        else -> {
            context.player.displayClientMessage(Component.literal("Telepad must be surrounded by Device Casing!"), true)
            InteractionResult.FAIL
        }
    }*/
}
