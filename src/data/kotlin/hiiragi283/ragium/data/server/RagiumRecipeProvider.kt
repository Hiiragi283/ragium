package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.data.recipe.HTMaterialRecipeProvider
import hiiragi283.core.common.material.VanillaMaterialItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.data.server.recipe.RagiumAlloyingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumDryingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMeltingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumUtilitiesRecipeProvider
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import java.util.function.Consumer

class RagiumRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(
            HTMaterialRecipeProvider(
                RagiumAPI.MOD_ID,
                RagiumMaterial.entries,
                RagiumBlocks.MATERIALS,
                RagiumItems.MATERIALS,
            ) { prefix: HTPrefixLike, material: HTMaterialLike ->
                RagiumItems.MATERIALS[prefix, material] ?: VanillaMaterialItems.MATERIALS[prefix, material]
            },
        )

        consumer.accept(RagiumAlloyingRecipeProvider)
        consumer.accept(RagiumDryingRecipeProvider)
        consumer.accept(RagiumMeltingRecipeProvider)

        consumer.accept(RagiumMaterialRecipeProvider)
        consumer.accept(RagiumUtilitiesRecipeProvider)
    }
}
