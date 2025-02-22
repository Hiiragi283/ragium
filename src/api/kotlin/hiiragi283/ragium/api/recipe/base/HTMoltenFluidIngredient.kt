package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import java.util.stream.Stream

class HTMoltenFluidIngredient(val key: HTMaterialKey) : FluidIngredient() {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMoltenFluidIngredient> = HTMaterialKey.CODEC
            .xmap(::HTMoltenFluidIngredient, HTMoltenFluidIngredient::key)
            .fieldOf("material")

        @JvmField
        val TYPE: FluidIngredientType<HTMoltenFluidIngredient> = FluidIngredientType(CODEC)
    }

    override fun test(stack: FluidStack): Boolean {
        val keyIn: HTMaterialKey = stack.get(RagiumAPI.getInstance().getMoltenMaterialComponent()) ?: return false
        return key == keyIn
    }

    override fun generateStacks(): Stream<FluidStack> = Stream.of(RagiumAPI.getInstance().createMoltenMetalStack(key, 1000))

    override fun isSimple(): Boolean = true

    override fun getType(): FluidIngredientType<*> = TYPE

    override fun hashCode(): Int = key.hashCode()

    override fun equals(obj: Any?): Boolean = (obj as? HTMoltenFluidIngredient)?.key == key
}
