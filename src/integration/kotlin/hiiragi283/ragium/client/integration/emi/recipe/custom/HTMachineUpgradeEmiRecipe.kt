package hiiragi283.ragium.client.integration.emi.recipe.custom

import dev.emi.emi.api.recipe.EmiIngredientRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiResolutionRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType

class HTMachineUpgradeEmiRecipe(private val id: ResourceLocation, private val upgrade: EmiStack) : EmiIngredientRecipe() {
    override fun getIngredient(): EmiIngredient = upgrade

    override fun getStacks(): List<EmiStack> = upgrade
        .get(RagiumDataComponents.MACHINE_UPGRADE_FILTER)
        ?.getAllHolders(RagiumPlatform.INSTANCE.getRegistryAccess())
        ?.map { holderSet: HolderSet<BlockEntityType<*>> ->
            holderSet
                .map(Holder<BlockEntityType<*>>::value)
                .flatMap(BlockEntityType<*>::getValidBlocks)
                .map(EmiStack::of)
        }?.result()
        ?: listOf()

    override fun getRecipeContext(stack: EmiStack, offset: Int): EmiRecipe = EmiResolutionRecipe(upgrade, stack)

    override fun getCategory(): EmiRecipeCategory = RagiumEmiRecipeCategories.MACHINE_UPGRADE

    override fun getId(): ResourceLocation = id
}
