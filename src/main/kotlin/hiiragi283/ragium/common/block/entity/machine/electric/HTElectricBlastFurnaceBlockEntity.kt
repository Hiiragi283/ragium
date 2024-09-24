package hiiragi283.ragium.common.block.entity.machine.electric

import hiiragi283.ragium.common.block.entity.machine.HTMultiMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.RagiumMachineConditions
import hiiragi283.ragium.common.init.RagiumBlockTags
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTElectricBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiMachineBlockEntity(HTMachineType.Multi.ELECTRIC_BLAST_FURNACE, pos, state) {
    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.BLAZING_HEAT

    override fun onProcessed(world: World, pos: BlockPos, recipe: HTMachineRecipe) {
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(Blocks.POLISHED_DEEPSLATE),
        ).addHollow(
            -1..1,
            1,
            1..3,
            HTBlockPredicate.tag(RagiumBlockTags.COILS),
        ).addHollow(
            -1..1,
            2,
            1..3,
            HTBlockPredicate.tag(RagiumBlockTags.COILS),
        ).addHollow(
            -1..1,
            3,
            1..3,
            HTBlockPredicate.block(Blocks.POLISHED_DEEPSLATE),
        )
}
