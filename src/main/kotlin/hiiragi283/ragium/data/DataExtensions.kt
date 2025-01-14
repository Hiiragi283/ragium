package hiiragi283.ragium.data

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.data.recipes.*
import net.neoforged.neoforge.common.data.LanguageProvider

//    LanguageProvider    //

fun LanguageProvider.add(tier: HTMachineTier, value: String, prefix: String) {
    add(tier.translationKey, value)
    add(tier.prefixKey, prefix)
}

fun LanguageProvider.add(machine: HTMachineKey, value: String, desc: String = "") {
    add(machine.translationKey, value)
    add(machine.descriptionKey, desc)
}

fun LanguageProvider.add(material: HTMaterialKey, value: String) {
    add(material.translationKey, value)
}

fun LanguageProvider.add(prefix: HTTagPrefix, value: String) {
    add(prefix.translationKey, value)
}

//    RecipeBuilder    //

private fun RecipeBuilder.savePrefixed(output: RecipeOutput, prefix: String) {
    /*val id: ResourceLocation = RecipeBuilder.getDefaultRecipeId(result)
    val namespace: String = id.namespace
    val fixedOutput: RecipeOutput = if (namespace == "minecraft" || namespace == RagiumAPI.MOD_ID) {
        output
    } else output.withConditions(ModLoadedCondition(namespace))*/
    save(output, RecipeBuilder.getDefaultRecipeId(result).withPrefix(prefix))
}

fun ShapedRecipeBuilder.defineMaterial(symbol: Char, provider: HTMaterialProvider): ShapedRecipeBuilder =
    define(symbol, provider.prefixedTagKey)

fun ShapedRecipeBuilder.define(symbol: Char, prefix: HTTagPrefix, material: HTMaterialKey): ShapedRecipeBuilder =
    define(symbol, prefix.createTag(material))

fun ShapedRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "shaped/")
}

fun ShapelessRecipeBuilder.requiresMaterial(provider: HTMaterialProvider): ShapelessRecipeBuilder = requires(provider.prefixedTagKey)

fun ShapelessRecipeBuilder.requires(prefix: HTTagPrefix, material: HTMaterialKey): ShapelessRecipeBuilder =
    requires(prefix.createTag(material))

fun ShapelessRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "shapeless/")
}

fun SimpleCookingRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "cooking/")
}

fun SingleItemRecipeBuilder.savePrefixed(output: RecipeOutput) {
    savePrefixed(output, "stonecutting/")
}
