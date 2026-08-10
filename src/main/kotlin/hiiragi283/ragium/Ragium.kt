package hiiragi283.ragium

import hiiragi283.lib.HTConstants
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(::register)
    }

    private fun register(event: RegisterEvent) {
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            helper.register(RagiumAPI.id(RagiumConstants.MELTING), RagiumRecipeSerializers.MELTING)
            helper.register(RagiumAPI.id(HTConstants.SMELTING), RagiumRecipeSerializers.SMELTING)
        }
        event.register(Registries.RECIPE_TYPE) { helper ->
            for (recipeType: HTRecipeType<*> in RagiumRecipeTypes.allTypes) {
                helper.register(recipeType.getId(), recipeType)
            }
        }
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumRegistries.ITEM_RESULT_TYPE)
    }
}
