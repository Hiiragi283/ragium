package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.data.recipe.HTRockGeneratingRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumRockGeneratingRecipeProvider : HTRecipeProvider.Direct() {
    private val lava: HTFluidIngredient by lazy { fluidCreator.lava(1000) }
    private val water: HTFluidIngredient by lazy { fluidCreator.water(1000) }

    private val packedIce: HTItemIngredient by lazy { itemCreator.fromItem(Items.PACKED_ICE) }
    private val magma: HTItemIngredient by lazy { itemCreator.fromItem(Items.MAGMA_BLOCK) }

    override fun buildRecipeInternal() {
        // Lava + Water -> Cobble
        HTRockGeneratingRecipeBuilder
            .create(lava, water, resultHelper.item(Items.COBBLESTONE))
            .save(output)
        // Lava + Water + Magma Block -> Stone
        HTRockGeneratingRecipeBuilder
            .create(lava, water, resultHelper.item(Items.STONE), magma)
            .save(output)

        overworld()
        nether()
        end()
    }

    @JvmStatic
    private fun overworld() {
        listOf(
            Items.GRANITE,
            Items.DIORITE,
            Items.ANDESITE,
        ).forEach { stone: Item ->
            HTRockGeneratingRecipeBuilder
                .create(lava, water, resultHelper.item(stone), itemCreator.fromItem(stone))
                .save(output)
        }

        // Lava + Packed Ice -> Blackstone
        stoneAndCobble(Items.DEEPSLATE, Items.COBBLED_DEEPSLATE, packedIce)
        // Lava + Packed Ice + Bone -> Calcite
        HTRockGeneratingRecipeBuilder
            .create(
                lava,
                packedIce,
                resultHelper.item(Items.CALCITE),
                itemCreator.fromTagKey(Tags.Items.STORAGE_BLOCKS_BONE_MEAL),
            ).save(output)
        // Lava + Packed Ice + Clay -> Dripstone
        HTRockGeneratingRecipeBuilder
            .create(
                lava,
                packedIce,
                resultHelper.item(Items.DRIPSTONE_BLOCK),
                itemCreator.fromItem(Items.CLAY),
            ).save(output)
    }

    @JvmStatic
    private fun nether() {
        // Lava + Packed Ice + Soul Sand -> Netherrack
        HTRockGeneratingRecipeBuilder
            .create(lava, packedIce, resultHelper.item(Items.NETHERRACK), itemCreator.fromItem(Items.SOUL_SAND))
            .save(output)
        // Lava + Blue Ice -> Blackstone
        stoneAndCobble(Items.BLACKSTONE, RagiumBlocks.SOOTY_COBBLESTONE, itemCreator.fromItem(Items.BLUE_ICE))
        // Lava + Packed Ice + Soul Soil -> Basalt
        HTRockGeneratingRecipeBuilder
            .create(lava, packedIce, resultHelper.item(Items.BASALT), itemCreator.fromItem(Items.SOUL_SOIL))
            .save(output)
    }

    @JvmStatic
    private fun end() {
        val eldritch: HTFluidIngredient = fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 1000)
        // Eldritch Flux + Water -> Eldritch Stone
        HTRockGeneratingRecipeBuilder
            .create(eldritch, water, resultHelper.item(RagiumBlocks.ELDRITCH_STONE))
            .save(output)
        // Eldritch Flux + Packed Ice -> End Stone
        HTRockGeneratingRecipeBuilder
            .create(eldritch, packedIce, resultHelper.item(Items.END_STONE))
            .save(output)
    }

    @JvmStatic
    private fun stoneAndCobble(stone: ItemLike, cobble: ItemLike, right: HTItemIngredient) {
        // Lava + Water -> Cobble
        HTRockGeneratingRecipeBuilder
            .create(lava, right, resultHelper.item(cobble))
            .save(output)
        // Lava + Water + Magma Block -> Stone
        HTRockGeneratingRecipeBuilder
            .create(lava, right, resultHelper.item(stone), magma)
            .save(output)
    }
}
