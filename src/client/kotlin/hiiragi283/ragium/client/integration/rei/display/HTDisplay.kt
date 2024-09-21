package hiiragi283.ragium.client.integration.rei.display

import me.shedaniel.rei.api.common.display.Display
import net.minecraft.recipe.Recipe
import net.minecraft.util.Identifier
import java.util.*

abstract class HTDisplay<T : Recipe<*>>(val recipe: T, private val id: Identifier) : Display {
    override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)
}
