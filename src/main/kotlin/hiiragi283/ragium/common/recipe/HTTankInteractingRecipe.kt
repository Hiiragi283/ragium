package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluid
import java.util.*

class HTTankInteractingRecipe(
    val emptyContainer: HTSimpleItemHolderLike,
    val filledContainer: HTSimpleItemHolderLike,
    val fluid: HTSimpleFluidHolderLike,
    val amount: Int,
    val fluidTag: Optional<TagKey<Fluid>>,
) : HTSerializableRecipe<RecipeInput> {
    @Deprecated("Not used")
    override fun test(input: RecipeInput): Boolean = false

    @Deprecated("Not used")
    override fun assemble(input: RecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.TANK_INTERACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.TANK_INTERACTION.get()
}
