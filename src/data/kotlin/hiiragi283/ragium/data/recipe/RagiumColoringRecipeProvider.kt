package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.get
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.ColorCollection
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DifferenceIngredient
import net.neoforged.neoforge.common.crafting.IntersectionIngredient

data object RagiumColoringRecipeProvider : HTRecipeProvider(RagiumAPI.MOD_ID) {
    override fun register() {
        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            val dyeContent: HTFluidContent = RagiumFluids.DYES[color]
            // Water + Solid Dye -> Liquid Dye
            RagiumRecipeBuilders.mixing {
                itemIngredient { +holderSet(color.tag) }
                fluidIngredient {
                    +waterSet()
                    amount /= 4
                }
                fluidResult {
                    +dyeContent
                    amount /= 4
                }
                time /= 2
                recipeId replace id("$name/liquid_dye")
            }.save(context)
            // Liquid Dye -> Solid Dye
            RagiumRecipeBuilders.freezing {
                ingredient {
                    +holderSet(dyeContent)
                    amount /= 4
                }
                catalyst { items { +RagiumItems.BALL_MOLD } }
                result { +Items.DYE[color] }
                time /= 4
                recipeId replace id("dye/$name")
            }.save(context)
            // Sand + Gravel + Liquid Dye -> XX Concrete
            RagiumRecipeBuilders.mixing {
                itemIngredient { +itemSet(Tags.BlockItems.SANDS) }
                itemIngredient { +itemSet(Tags.BlockItems.GRAVELS) }
                fluidIngredient {
                    +holderSet(dyeContent)
                    amount /= 4
                }
                itemResult {
                    +Items.CONCRETE[color]
                    count = 2
                }
                time /= 2
                recipeId replace id("$name/concrete")
            }.save(context)

            // Coloring
            if (color == DyeColor.WHITE) continue
            for (contents: ColoredContents in ColoredContents.entries) {
                RagiumRecipeBuilders.bathing {
                    itemIngredient { +contents.onlyWhites() }
                    fluidIngredient {
                        +holderSet(dyeContent)
                        amount /= 8
                    }
                    result { +contents.items[color].asItem() }
                    time /= 4
                    recipeId replace id("$name/${contents.serializedName}")
                }.save(context)
            }
        }

        // Bleaching
        for (contents: ColoredContents in ColoredContents.entries) {
            RagiumRecipeBuilders.bathing {
                itemIngredient { +contents.excludeWhites() }
                fluidIngredient {
                    +holderSet(RagiumFluids.BLEACH)
                    amount /= 8
                }
                result { +contents.white.asItem() }
                recipeId replace id("white/${contents.serializedName}")
            }.save(context)
        }
    }

    enum class ColoredContents(val allTag: TagKey<Item>, val items: ColorCollection<out ItemLike>) :
        StringRepresentable {
        BANNER(ItemTags.BANNERS, Items.BANNER),
        BED(ItemTags.BEDS, Items.BED),
        HARNESS(ItemTags.HARNESSES, Items.HARNESS),
        SHULKER_BOX(ItemTags.SHULKER_BOXES, Items.DYED_SHULKER_BOX),
        WOOL(ItemTags.WOOL, Items.WOOL),
        WOOL_CARPET(ItemTags.WOOL_CARPETS, Items.CARPET)
        ;

        val white: ItemLike get() = items[DyeColor.WHITE]

        override fun getSerializedName(): String = name.lowercase()
    }

    /**
     * 白色のアイテムのみを判定します。
     */
    fun ColoredContents.onlyWhites(): Ingredient = IntersectionIngredient.of(
        Ingredient.of(holderSet(this.allTag)),
        Ingredient.of(itemSet(Tags.BlockItems.DYED_WHITE))
    )

    /**
     * 白色以外のアイテムを判定します。
     */
    fun ColoredContents.excludeWhites(): Ingredient = DifferenceIngredient.of(
        Ingredient.of(holderSet(this.allTag)),
        Ingredient.of(itemSet(Tags.BlockItems.DYED_WHITE))
    )
}
