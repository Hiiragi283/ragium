package hiiragi283.ragium.data.client.model

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.resources.ResourceLocation

class RagiumBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(RagiumAPI.MOD_ID, context) {
    override fun registerStatesAndModels() {
        registerMaterials()

        RagiumFluids.REGISTER.entries.forEach(::liquidBlock)
    }

    private fun registerMaterials() {
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, material: HTMaterialKey, block: HTSimpleDeferredBlock) ->
            val textureId: ResourceLocation = RagiumAPI.id("block", prefix.name, material.name)
            existTexture(block, textureId, ::altTextureBlock)
        }
    }
}
