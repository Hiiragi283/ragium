package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.recipe.ingredient.HTBluePrintIngredient
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(RagiumAPI.id("blue_print"), HTBluePrintIngredient.TYPE)
        }
    }
}
