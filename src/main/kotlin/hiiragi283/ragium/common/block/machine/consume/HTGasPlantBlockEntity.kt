package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome

class HTGasPlantBlockEntity(pos: BlockPos, state: BlockState) :
    HTResourceDrillBlockEntityBase(RagiumBlockEntityTypes.GAS_PLANT, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.GAS_PLANT

    override val fluidMap: Map<TagKey<Biome>, HTFluidVariantStack> = mapOf(
        BiomeTags.IS_END to HTFluidVariantStack(RagiumFluids.NOBLE_GAS, FluidConstants.INGOT),
        BiomeTags.IS_NETHER to HTFluidVariantStack(RagiumFluids.SULFUR_DIOXIDE, FluidConstants.BUCKET),
        BiomeTags.IS_OVERWORLD to HTFluidVariantStack(RagiumFluids.AIR, FluidConstants.BUCKET),
    )
}
