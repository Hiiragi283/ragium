package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import io.netty.buffer.ByteBuf
import net.minecraft.core.HolderLookup
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTPrintingRecipe(val ingredient: HTItemIngredient, val origin: HTItemHolderLike<*>, val strategy: CopyStrategy) :
    HTItemAndItemRecipe.Serializable {
    override fun testFirstItem(stack: ItemStack): Boolean = ingredient.test(stack)

    override fun testSecondItem(stack: ItemStack): Boolean = stack.`is`(origin.getItemHolder())

    override fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int> = 1 to 0

    override val time: Int = 100

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack = when (strategy) {
        CopyStrategy.INPUT -> {
            val inputStack: ItemStack = input.first.copyWithCount(1)
            inputStack.applyComponents(input.second.componentsPatch)
            inputStack
        }
        CopyStrategy.ORIGIN -> input.second.copyWithCount(1)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRINTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRINTING.get()

    enum class CopyStrategy : StringRepresentable {
        INPUT,
        ORIGIN,
        ;

        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, CopyStrategy> = BiCodecs.stringEnum(CopyStrategy::getSerializedName)
        }

        override fun getSerializedName(): String = name.lowercase()
    }
}
