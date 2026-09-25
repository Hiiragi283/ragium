package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.alchemy.RagiumPotions
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionBrewing
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent

@EventBusSubscriber
data object RagiumEventHandlers {
    @SubscribeEvent
    private fun registerBrewingRecipes(event: RegisterBrewingRecipesEvent) {
        val builder: PotionBrewing.Builder = event.builder
        RagiumPotions.FROSTBITE.registerMix(builder, Items.SNOW_BLOCK)
        RagiumPotions.CHEMICAL_BURN.registerMix(
            builder,
            RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.SULFUR)
        )
    }
}
