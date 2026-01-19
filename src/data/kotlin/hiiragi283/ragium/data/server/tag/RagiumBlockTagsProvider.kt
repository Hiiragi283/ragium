package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class RagiumBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(RagiumAPI.MOD_ID, Registries.BLOCK, context) {
    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        material(factory)
        mineable(factory)
        misc(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Block>) {
        HTMaterialContentsAccess.INSTANCE.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) ->
            if (key.getNamespace() != modId) return@forEach
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
            yieldAll(HTMaterialContentsAccess.INSTANCE.getAllBlocks().filter { it.getNamespace() == modId })

            yield(RagiumBlocks.ALLOY_SMELTER)
            yield(RagiumBlocks.CRUSHER)
            yield(RagiumBlocks.CUTTING_MACHINE)
            yield(RagiumBlocks.FORMING_PRESS)

            yield(RagiumBlocks.DRYER)
            yield(RagiumBlocks.MELTER)
            yield(RagiumBlocks.MIXER)
            yield(RagiumBlocks.PYROLYZER)
            yield(RagiumBlocks.SOLIDIFIER)

            yield(RagiumBlocks.FERMENTER)
            yield(RagiumBlocks.PLANTER)

            yield(RagiumBlocks.ENCHANTER)

            yield(RagiumBlocks.BATTERY)
            yield(RagiumBlocks.CRATE)
            yield(RagiumBlocks.TANK)
            yield(RagiumBlocks.RESONANT_INTERFACE)
            yield(RagiumBlocks.UNIVERSAL_CHEST)

            yield(RagiumBlocks.IMITATION_SPAWNER)

            yield(RagiumBlocks.CREATIVE_BATTERY)
            yield(RagiumBlocks.CREATIVE_CRATE)
            yield(RagiumBlocks.CREATIVE_TANK)
        }.forEach(pickaxe::add)
    }

    //    Misc    //

    private fun misc(factory: BuilderFactory<Block>) {
    }
}
