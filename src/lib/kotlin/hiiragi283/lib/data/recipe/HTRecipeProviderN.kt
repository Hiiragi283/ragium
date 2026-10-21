package hiiragi283.lib.data.recipe

import hiiragi283.lib.resource.HTSimpleValueWithKey
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.advancements.Advancement
import net.minecraft.core.registries.MultiRegistryBootstrap
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Recipe

abstract class HTRecipeProviderN(
    recipeOutput: BootstrapContext<Recipe<*>>,
    advancementOutput: BootstrapContext<Advancement>
) : RecipeProvider(recipeOutput, advancementOutput) {
    constructor(registries: MultiRegistryBootstrap.BootstrapGetter) : this(
        registries.get(Registries.RECIPE),
        registries.get(Registries.ADVANCEMENT)
    )

    public abstract override fun buildRecipes()

    //    Extensions    //

    fun getHasName(id: Identifier): String = "has_${id.debugPath}"

    fun getHasName(value: HTSimpleValueWithKey<*>): String = getHasName(value.id())

    fun getHasName(tagKey: TagKey<*>): String = getHasName(tagKey.location())

    fun getHasName(tagKey: BlockItemTagId): String = getHasName(tagKey.item())

    fun getHasName(prefix: HTTagPrefix, material: HTMaterialLike): String = getHasName(prefix.itemTagKey(material))
}
