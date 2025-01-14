package hiiragi283.ragium.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.common.block.HTMachineBlock
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredBlock

@JeiPlugin
class RagiumJEIPlugin : IModPlugin {
    companion object {
        @JvmField
        val PLUGIN_ID: ResourceLocation = RagiumAPI.id("default")
    }

    override fun getPluginUid(): ResourceLocation = PLUGIN_ID

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        val handler: ISubtypeInterpreter<ItemStack> = object : ISubtypeInterpreter<ItemStack> {
            override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? = ingredient.machineTier

            override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ingredient.machineTier.text.string
        }

        RagiumAPI.getInstance().machineRegistry.blocks.forEach { holder: DeferredBlock<HTMachineBlock> ->
            registration.registerSubtypeInterpreter(holder.asItem(), handler)
        }
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
    }
}
