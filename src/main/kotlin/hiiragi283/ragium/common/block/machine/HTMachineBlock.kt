package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.HTEntityBlock
import hiiragi283.ragium.common.block.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTStirlingGeneratorBlockEntity
import hiiragi283.ragium.common.block.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.common.block.processor.*
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTMachineBlock(val type: HTMachineType, properties: Properties) : HTEntityBlock.Horizontal(properties) {
    companion object {
        @JvmStatic
        private val factoryMap: Map<HTMachineType, (BlockPos, BlockState) -> BlockEntity?> = mapOf(
            // Consumer
            HTMachineType.FISHER to ::HTFisherBlockEntity,
            // Generator
            HTMachineType.COMBUSTION_GENERATOR to ::HTCombustionGeneratorBlockEntity,
            HTMachineType.SOLAR_GENERATOR to ::HTSolarGeneratorBlockEntity,
            HTMachineType.STIRLING_GENERATOR to ::HTStirlingGeneratorBlockEntity,
            HTMachineType.THERMAL_GENERATOR to ::HTThermalGeneratorBlockEntity,
            // Processor - Basic
            HTMachineType.ASSEMBLER to ::HTAssemblerBlockEntity,
            HTMachineType.BLAST_FURNACE to ::HTBlastFurnaceBlockEntity,
            HTMachineType.COMPRESSOR to ::HTCompressorBlockEntity,
            HTMachineType.GRINDER to ::HTGrinderBlockEntity,
            HTMachineType.MULTI_SMELTER to ::HTMultiSmelterBlockEntity,
            // Processor - Advanced
            HTMachineType.EXTRACTOR to ::HTExtractorBlockEntity,
            HTMachineType.INFUSER to ::HTInfuserBlockEntity,
            HTMachineType.MIXER to ::HTMixerBlockEntity,
            HTMachineType.REFINERY to ::HTRefineryBlockEntity,
            // Processor - Elite
            HTMachineType.LASER_ASSEMBLY to ::HTLaserAssemblyBlockEntity,
        )
    }

    override fun getDescriptionId(): String = type.translationKey

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        tooltipComponents.add(type.descriptionText)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = factoryMap[type]?.invoke(pos, state)
}
