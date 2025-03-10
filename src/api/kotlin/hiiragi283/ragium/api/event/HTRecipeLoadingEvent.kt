package hiiragi283.ragium.api.event

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent
import net.neoforged.neoforge.common.conditions.WithConditions

class HTRecipeLoadingEvent(val recipeId: ResourceLocation, val withCondition: WithConditions<Recipe<*>>) :
    Event(),
    ICancellableEvent {
    val recipe: Recipe<*> get() = withCondition.carrier
}
