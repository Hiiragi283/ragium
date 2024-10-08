package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.api.recipe.HTRecipeBase
import me.shedaniel.rei.api.common.display.Display
import net.minecraft.util.Identifier
import java.util.*

abstract class HTDisplay<T : HTRecipeBase<*>>(val recipe: T, val id: Identifier) : Display {
    override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)
}
