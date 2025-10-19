package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe

interface VanillaRecipeTypes {
    companion object {
        @JvmField
        val INSTANCE: VanillaRecipeTypes = RagiumAPI.getService()
        
        @JvmField
        val CRAFTING = INSTANCE.crafting()
        
        @JvmField
        val SMELTING = INSTANCE.smelting()
        
        @JvmField
        val BLASTING = INSTANCE.blasting()
        
        @JvmField
        val SMOKING = INSTANCE.smoking()
        
        @JvmField
        val STONE_CUTTING = INSTANCE.stonecutting()
        
        @JvmField
        val SMITHING = INSTANCE.smithing()
    }

    fun crafting(): HTRecipeType.Findable<CraftingInput, CraftingRecipe>

    fun smelting(): HTRecipeType.Findable<SingleRecipeInput, SmeltingRecipe>

    fun blasting(): HTRecipeType.Findable<SingleRecipeInput, BlastingRecipe>

    fun smoking(): HTRecipeType.Findable<SingleRecipeInput, SmokingRecipe>

    fun stonecutting(): HTRecipeType.Findable<SingleRecipeInput, StonecutterRecipe>

    fun smithing(): HTRecipeType.Findable<SmithingRecipeInput, SmithingRecipe>
}
