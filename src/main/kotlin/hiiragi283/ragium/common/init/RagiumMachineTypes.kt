package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier

object RagiumMachineTypes {
    @JvmField
    val BLAST_FURNACE: HTMachineType<HTMachineRecipe> = register("blast_furnace")

    @JvmField
    val DISTILLATION_TOWER: HTMachineType<HTMachineRecipe> = register("distillation_tower")

    @JvmStatic
    private fun register(name: String): HTMachineType<HTMachineRecipe> = HTMachineType.register(Ragium.id(name), RagiumRecipeTypes.MACHINE)

    @JvmStatic
    fun init() {
        buildList {
            addAll(Single.entries)
            add(BLAST_FURNACE)
            add(DISTILLATION_TOWER)
        }.forEach(HTMachineType.Companion::register)
    }

    enum class Single : HTMachineType<HTMachineRecipe> {
        ALLOY_FURNACE,
        ASSEMBLER,
        CENTRIFUGE,
        CHEMICAL_REACTOR,
        COMPRESSOR,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER,
        ROCK_GENERATOR,
        ;

        override val id: Identifier = Ragium.id(name.lowercase())
        override val recipeType: RecipeType<HTMachineRecipe> = RagiumRecipeTypes.MACHINE
    }
}
