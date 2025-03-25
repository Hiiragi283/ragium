package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

class HTDeviceEmiRecipe(id: ResourceLocation, device: ItemLike, output: EmiStack) :
    BasicEmiRecipe(RagiumEmiCategories.DEVICE, id, 0, 0),
    HTSimpleEmiRecipe {
    override fun getDisplayWidth(): Int = super<HTSimpleEmiRecipe>.displayWidth

    override fun getDisplayHeight(): Int = super<HTSimpleEmiRecipe>.displayHeight

    init {
        inputs.add(EmiStack.of(device))
        outputs.add(output)
    }

    override fun getFirstInput(): EmiIngredient = inputs[0]

    override fun getFirstOutput(): EmiStack = outputs[0]
}
