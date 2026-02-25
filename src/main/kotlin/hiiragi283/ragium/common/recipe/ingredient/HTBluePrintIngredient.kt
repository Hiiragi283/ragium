package hiiragi283.ragium.common.recipe.ingredient

import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.common.item.HTBlueprintItem
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import io.netty.buffer.ByteBuf
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

class HTBluePrintIngredient(private val number: Int) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<ByteBuf, HTBluePrintIngredient> =
            HTBlueprintItem.RANGE_CODEC.fieldOf("number").xmap(::HTBluePrintIngredient, HTBluePrintIngredient::number)

        @JvmField
        val TYPE: IngredientType<HTBluePrintIngredient> = CODEC.toSerializer(::IngredientType)
    }

    override fun test(stack: ItemStack): Boolean = stack.get(RagiumDataComponents.BLUEPRINT_NUMBER) == number

    override fun getItems(): Stream<ItemStack> =
        Stream.of(createItemStack(RagiumItems.BLUEPRINT, RagiumDataComponents.BLUEPRINT_NUMBER, number))

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = TYPE
}
