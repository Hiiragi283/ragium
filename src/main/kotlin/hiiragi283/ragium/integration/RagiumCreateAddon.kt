package hiiragi283.ragium.integration

import com.simibubi.create.AllBlocks
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.heat.HTHeatTier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

@HTAddon("create")
object RagiumCreateAddon : RagiumAddon {
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlock(
            HTHeatTier.BLOCK_CAPABILITY,
            { level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?, direction: Direction ->
                when (state.getValue(BlazeBurnerBlock.HEAT_LEVEL)) {
                    BlazeBurnerBlock.HeatLevel.FADING -> HTHeatTier.LOW
                    BlazeBurnerBlock.HeatLevel.KINDLED -> HTHeatTier.MEDIUM
                    BlazeBurnerBlock.HeatLevel.SEETHING -> HTHeatTier.HIGH
                    else -> HTHeatTier.NONE
                }
            },
            AllBlocks.BLAZE_BURNER.get(),
        )
    }

    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::registerBlockCapabilities)
    }
}
