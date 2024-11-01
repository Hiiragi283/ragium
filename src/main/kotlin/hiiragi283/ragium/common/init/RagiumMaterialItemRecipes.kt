package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.data.recipe.HTMaterialItemRecipeRegistry
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.item.Items

object RagiumMaterialItemRecipes {
    @JvmStatic
    fun init() {
        HTMaterialItemRecipeRegistry.register("iron") {
            set(HTMaterialItemRecipeRegistry.DISABLE_BLOCK_RECIPE, Unit)
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.IRON)
            set(HTMaterialItemRecipeRegistry.INGOT, Items.IRON_INGOT)
            // set(HTMaterialItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.NICKEL)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.IRON)
            set(HTMaterialItemRecipeRegistry.RAW, Items.RAW_IRON)
        }

        HTMaterialItemRecipeRegistry.register("gold") {
            set(HTMaterialItemRecipeRegistry.DISABLE_BLOCK_RECIPE, Unit)
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.GOLD)
            set(HTMaterialItemRecipeRegistry.INGOT, Items.GOLD_INGOT)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.GOLD)
            set(HTMaterialItemRecipeRegistry.RAW, Items.RAW_GOLD)
        }

        HTMaterialItemRecipeRegistry.register("copper") {
            set(HTMaterialItemRecipeRegistry.DISABLE_BLOCK_RECIPE, Unit)
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.COPPER)
            set(HTMaterialItemRecipeRegistry.INGOT, Items.COPPER_INGOT)
            set(HTMaterialItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.BAUXITE)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.COPPER)
            set(HTMaterialItemRecipeRegistry.RAW, Items.RAW_COPPER)
        }

        HTMaterialItemRecipeRegistry.register("crude_raginite") {
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.CRUDE_RAGINITE)
            set(HTMaterialItemRecipeRegistry.ORE, RagiumContents.Ores.CRUDE_RAGINITE)
            set(HTMaterialItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.RAGINITE)
            set(HTMaterialItemRecipeRegistry.RAW, RagiumContents.RawMaterials.CRUDE_RAGINITE)
        }
        HTMaterialItemRecipeRegistry.register("raginite") {
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.RAGINITE)
            set(HTMaterialItemRecipeRegistry.ORE_SUB_PRODUCTS, RagiumContents.Dusts.RAGINITE)
            set(HTMaterialItemRecipeRegistry.RAW, RagiumContents.RawMaterials.RAGINITE)
        }

        HTMaterialItemRecipeRegistry.register("ragi_crystal") {
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.RAGI_CRYSTAL)
            set(HTMaterialItemRecipeRegistry.ORE, RagiumContents.Ores.END_RAGI_CRYSTAL)
            set(HTMaterialItemRecipeRegistry.RAW, RagiumContents.Gems.RAGI_CRYSTAL)
        }

        HTMaterialItemRecipeRegistry.register("ragi_alloy") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.RAGI_ALLOY)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.RAGI_ALLOY)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.RAGI_ALLOY)
        }

        HTMaterialItemRecipeRegistry.register("ragi_steel") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.RAGI_STEEL)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.RAGI_STEEL)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.RAGI_STEEL)
        }

        HTMaterialItemRecipeRegistry.register("refined_ragi_steel") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.REFINED_RAGI_STEEL)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.REFINED_RAGI_STEEL)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.REFINED_RAGI_STEEL)
        }

        HTMaterialItemRecipeRegistry.register("aluminum") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.ALUMINUM)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.ALUMINUM)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.ALUMINUM)
        }
        HTMaterialItemRecipeRegistry.register("bauxite") {
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.BAUXITE)
        }
        /*HTMaterialItemRecipeRegistry.register("invar") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.INVAR)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.INVAR)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.INVAR)
        }
        HTMaterialItemRecipeRegistry.register("nickel") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.NICKEL)
            set(HTMaterialItemRecipeRegistry.DUST, RagiumContents.Dusts.NICKEL)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.NICKEL)
        }*/
        HTMaterialItemRecipeRegistry.register("steel") {
            set(HTMaterialItemRecipeRegistry.BLOCK, RagiumContents.StorageBlocks.STEEL)
            set(HTMaterialItemRecipeRegistry.INGOT, RagiumContents.Ingots.STEEL)
            set(HTMaterialItemRecipeRegistry.PLATE, RagiumContents.Plates.STEEL)
        }
    }
}
