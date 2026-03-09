package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.ragium.common.recipe.modifier.HTEnchantmentDuplicationModifier
import hiiragi283.ragium.common.recipe.modifier.HTPotionDuplicationModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(RagiumAPI.id("blue_print"), HTBluePrintIngredient.TYPE)
        }

        // Duplication Modifier
        event.register(RagiumRegistries.Keys.DUPLICATION_MODIFIER) { helper ->
            helper.register(RagiumAPI.id("enchantment"), HTEnchantmentDuplicationModifier)
            helper.register(RagiumAPI.id("potion"), HTPotionDuplicationModifier)
        }
    }
}
