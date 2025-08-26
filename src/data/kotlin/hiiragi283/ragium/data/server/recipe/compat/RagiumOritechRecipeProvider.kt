package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.CentrifugeRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.OritechRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.ORITECH) {
    override fun buildRecipeInternal() {
        centrifuge()
        foundry()
        atomicForge()
        particle()
    }

    @JvmStatic
    private fun centrifuge() {
        CentrifugeRecipeBuilder
            .build()
            .input(gemOrDust(RagiumMaterialType.ELDRITCH_PEARL))
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX)
        // .export(output, "eldritch_flux")
    }

    @JvmStatic
    private fun foundry() {
        FoundryRecipeBuilder
            .build()
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .input(ingotOrDust(HTVanillaMaterialType.COPPER))
            .result(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY))
            .export(output, RagiumConst.RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .input(ingotOrDust(HTVanillaMaterialType.GOLD))
            .result(RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY))
            .export(output, RagiumConst.ADVANCED_RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(HTVanillaMaterialType.AMETHYST))
            .input(gemOrDust(HTVanillaMaterialType.LAPIS))
            .result(RagiumItems.getGem(RagiumMaterialType.AZURE), 2)
            .export(output, "azure_shard")

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(RagiumMaterialType.AZURE))
            .input(ingotOrDust(HTVanillaMaterialType.IRON))
            .result(RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL))
            .export(output, RagiumConst.AZURE_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(RagiumItems.DEEP_SCRAP)
            .input(ingotOrDust(RagiumMaterialType.AZURE_STEEL))
            .result(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL))
            .export(output, RagiumConst.DEEP_STEEL)
    }

    @JvmStatic
    private fun atomicForge() {
        AtomicForgeRecipeBuilder
            .build()
            .input(gemOrDust(HTVanillaMaterialType.DIAMOND))
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .result(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL))
            .time(20)
            .export(output, RagiumConst.RAGI_CRYSTAL)
    }

    @JvmStatic
    private fun particle() {
        ParticleCollisionRecipeBuilder
            .build()
            .input(HTItemMaterialVariant.DUST, HTVanillaMaterialType.OBSIDIAN)
            .input(Items.DEEPSLATE)
            .result(RagiumItems.DEEP_SCRAP)
            .time(2500)
            .export(output, "deep_scrap")
    }

    //    Extension    //

    fun OritechRecipeBuilder.input(variant: HTItemMaterialVariant, material: HTMaterialType): OritechRecipeBuilder =
        input(variant.itemTagKey(material))

    fun OritechRecipeBuilder.result(item: ItemLike, count: Int = 1): OritechRecipeBuilder = result(ItemStack(item, count))

    fun OritechRecipeBuilder.fluidInput(content: HTFluidContent<*, *, *>): OritechRecipeBuilder = fluidInput(content.commonTag)

    fun OritechRecipeBuilder.fluidOutput(content: HTFluidContent<*, *, *>): OritechRecipeBuilder = fluidOutput(content.get())
}
