package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTHorizontalMachineBlock
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.tile.consumer.HTFisherBlockEntity
import hiiragi283.ragium.common.tile.generator.*
import hiiragi283.ragium.common.tile.processor.*
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HTMachineBlockImpl(machineType: HTMachineType, properties: Properties) : HTHorizontalMachineBlock(machineType, properties) {
    companion object {
        @JvmStatic
        private val factoryMap: Map<HTMachineType, (BlockPos, BlockState) -> BlockEntity?> = mapOf(
            // Consumer
            HTMachineType.FISHER to ::HTFisherBlockEntity,
            // Generator
            HTMachineType.COMBUSTION_GENERATOR to ::HTCombustionGeneratorBlockEntity,
            HTMachineType.ENCH_GENERATOR to ::HTEnchantmentGeneratorBlockEntity,
            HTMachineType.SOLAR_GENERATOR to ::HTSolarGeneratorBlockEntity,
            HTMachineType.STIRLING_GENERATOR to ::HTStirlingGeneratorBlockEntity,
            HTMachineType.THERMAL_GENERATOR to ::HTThermalGeneratorBlockEntity,
            // Processor - Basic
            HTMachineType.ASSEMBLER to ::HTAssemblerBlockEntity,
            HTMachineType.AUTO_CHISEL to ::HTAutoChiselBlockEntity,
            HTMachineType.COMPRESSOR to ::HTCompressorBlockEntity,
            HTMachineType.ELECTRIC_FURNACE to ::HTElectricFurnaceBlockEntity,
            HTMachineType.GRINDER to ::HTGrinderBlockEntity,
            // Processor - Advanced
            HTMachineType.EXTRACTOR to ::HTExtractorBlockEntity,
            HTMachineType.GROWTH_CHAMBER to ::HTGrowthChamberBlockEntity,
            HTMachineType.INFUSER to ::HTInfuserBlockEntity,
            HTMachineType.MIXER to ::HTMixerBlockEntity,
            HTMachineType.REFINERY to ::HTRefineryBlockEntity,
            HTMachineType.SOLIDIFIER to ::HTSolidifierBlockEntity,
            // Processor - Elite
            HTMachineType.BREWERY to ::HTBreweryBlockEntity,
            HTMachineType.LASER_ASSEMBLY to ::HTLaserAssemblyBlockEntity,
            HTMachineType.MULTI_SMELTER to ::HTMultiSmelterBlockEntity,
        )
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = factoryMap[machineType]?.invoke(pos, state)
}
