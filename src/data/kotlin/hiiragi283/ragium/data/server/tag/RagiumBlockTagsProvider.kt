package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block

class RagiumBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(RagiumAPI.MOD_ID, Registries.BLOCK, context) {
    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        material(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Block>) {
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, block: HTIdLike) ->
            addMaterial(factory, prefix, key).add(block)
        }
    }
}
