package hiiragi283.ragium.impl.data.recipe

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize
import blusunrize.immersiveengineering.api.crafting.TagOutput
import blusunrize.immersiveengineering.data.recipes.builder.AlloyRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.ArcFurnaceRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.BaseHelpers
import blusunrize.immersiveengineering.data.recipes.builder.IERecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTArcFurnaceRecipeBuilder private constructor() :
    IERecipeBuilder<HTArcFurnaceRecipeBuilder>(),
    BaseHelpers.ItemInput<HTArcFurnaceRecipeBuilder>,
    BaseHelpers.ItemOutput<HTArcFurnaceRecipeBuilder> {
        companion object {
            @JvmStatic
            fun builder(): HTArcFurnaceRecipeBuilder = HTArcFurnaceRecipeBuilder()
        }

        private var count = 0
        private val builder: AlloyRecipeBuilder = AlloyRecipeBuilder.builder()
        private val builder1: ArcFurnaceRecipeBuilder = ArcFurnaceRecipeBuilder.builder()

        fun input(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTArcFurnaceRecipeBuilder =
            input(prefix.itemTagKey(material), count)

        fun output(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): HTArcFurnaceRecipeBuilder =
            output(prefix.itemTagKey(material), count)

        //    BaseHelpers    //

        override fun input(input: IngredientWithSize): HTArcFurnaceRecipeBuilder = apply {
            builder.input(input)
            if (count == 0) {
                builder1.input(input)
            } else {
                builder1.additive(input)
            }
            count++
        }

        override fun output(output: TagOutput): HTArcFurnaceRecipeBuilder = apply {
            builder.output(output)
            builder1.output(output)
        }

        fun build(output: RecipeOutput, id: ResourceLocation) {
            builder.setTime(200).build(output, id.withPrefix("alloying/"))
            builder1.setTime(200).setEnergy(102400).build(output, id.withPrefix("arc_furnace/alloy/"))
        }
    }
