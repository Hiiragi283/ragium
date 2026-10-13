package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTColoredCollection
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.holdersets.AndHolderSet
import net.neoforged.neoforge.registries.holdersets.NotHolderSet
import java.util.concurrent.CompletableFuture

class RagiumColoringRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
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
            }.save(exporter)
            // Liquid Dye -> Solid Dye
            RagiumRecipeBuilders.freezing {
                ingredient {
                    +holderSet(dyeContent)
                    amount /= 4
                }
                catalyst { items { +RagiumItems.BALL_MOLD } }
                result { +VanillaColoredCollections.DYE[color] }
                time /= 4
                recipeId replace id("dye/$name")
            }.save(exporter)
            // Sand + Gravel + Liquid Dye -> XX Concrete
            RagiumRecipeBuilders.mixing {
                itemIngredient { +holderSet(Tags.Items.SANDS) }
                itemIngredient { +holderSet(Tags.Items.GRAVELS) }
                fluidIngredient {
                    +holderSet(dyeContent)
                    amount /= 4
                }
                itemResult {
                    +VanillaColoredCollections.CONCRETE[color]
                    count = 2
                }
                time /= 2
                recipeId replace id("$name/concrete")
            }.save(exporter)

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
                }.save(exporter)
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
            }.save(exporter)
        }
    }

    override fun getName(): String = "Coloring Recipes"

    enum class ColoredContents(val allTag: TagKey<Item>, val items: HTColoredCollection<ItemLike>) :
        StringRepresentable {
        BANNER(ItemTags.BANNERS, VanillaColoredCollections.BANNER),
        BED(ItemTags.BEDS, VanillaColoredCollections.BED),
        HARNESS(ItemTags.HARNESSES, VanillaColoredCollections.HARNESS),
        SHULKER_BOX(ItemTags.SHULKER_BOXES, VanillaColoredCollections.SHULKER_BOX),
        WOOL(ItemTags.WOOL, VanillaColoredCollections.WOOL),
        WOOL_CARPET(ItemTags.WOOL_CARPETS, VanillaColoredCollections.CARPET)
        ;

        val white: ItemLike get() = items[DyeColor.WHITE]

        override fun getSerializedName(): String = name.lowercase()
    }

    /**
     * 白色のアイテムのみを判定します。
     */
    fun ColoredContents.onlyWhites(): HolderSet<Item> = AndHolderSet(
        holderSet(this.allTag),
        holderSet(Tags.Items.DYED_WHITE)
    )

    /**
     * 白色以外のアイテムを判定します。
     */
    fun ColoredContents.excludeWhites(): HolderSet<Item> = AndHolderSet(
        holderSet(this.allTag),
        NotHolderSet(registries.lookupOrThrow(Registries.ITEM), holderSet(Tags.Items.DYED_WHITE))
    )
}
