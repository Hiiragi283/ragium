package hiiragi283.ragium.common.recipe.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.ComposterBlock

data object HTBioExtractingRecipe : HTItemWithCatalystRecipe {
    @JvmField
    val RECIPE_ID: ResourceLocation = RagiumAPI.id(RagiumConst.EXTRACTING, "crude_bio_from_items")

    @JvmStatic
    fun getCrudeBio(chance: Float): ImmutableFluidStack? {
        if (chance <= 0f) return null
        return RagiumFluidContents.CRUDE_BIO.toImmutableStack((1000 * chance.toFraction()).toInt())
    }

    override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? = input
        .item(0)
        ?.unwrap()
        ?.let(ComposterBlock::getValue)
        ?.let(::getCrudeBio)

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = null

    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testItem(0) { stack: ImmutableItemStack -> ComposterBlock.getValue(stack.unwrap()) > 0f }
        val bool2: Boolean = input.testItem(1) { stack: ImmutableItemStack -> stack.isOf(Items.COMPOSTER) }
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BIO_EXTRACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()

    override fun getRequiredCount(): Int = 1
}
