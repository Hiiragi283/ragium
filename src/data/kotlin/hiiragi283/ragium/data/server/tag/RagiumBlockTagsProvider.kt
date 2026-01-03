package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class RagiumBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(RagiumAPI.MOD_ID, Registries.BLOCK, context) {
    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        material(factory)
        mineable(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Block>) {
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, block: HTIdLike) ->
            addMaterial(factory, prefix, key).add(block)
        }
    }

    //    Mineable    //

    private fun mineable(factory: BuilderFactory<Block>) {
        val hoe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_HOE)
        sequence {
            yield(RagiumBlocks.MEAT_BLOCK)
            yield(RagiumBlocks.COOKED_MEAT_BLOCK)
        }.forEach(hoe::add)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(RagiumBlocks.MATERIALS.row(HCMaterialPrefixes.STORAGE_BLOCK).values)

            yield(RagiumBlocks.ALLOY_SMELTER)
            yield(RagiumBlocks.CRUSHER)
            yield(RagiumBlocks.CUTTING_MACHINE)

            yield(RagiumBlocks.DRYER)
            yield(RagiumBlocks.MELTER)
            yield(RagiumBlocks.MIXER)
            yield(RagiumBlocks.PYROLYZER)

            yield(RagiumBlocks.PLANTER)

            yield(RagiumBlocks.BATTERY)
            yield(RagiumBlocks.CRATE)
            yield(RagiumBlocks.TANK)
            yield(RagiumBlocks.RESONANT_INTERFACE)
            yield(RagiumBlocks.UNIVERSAL_CHEST)
        }.forEach(pickaxe::add)
    }
}
