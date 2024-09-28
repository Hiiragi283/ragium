package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumMachineTypes {
    //    Custom    //

    @JvmField
    val BURNING_BOX: HTMachineType<HTMachineRecipe> = register("burning_box")

    //    Multi    //

    @JvmField
    val BLAST_FURNACE: HTMachineType<HTMachineRecipe> = register("blast_furnace")

    @JvmField
    val DISTILLATION_TOWER: HTMachineType<HTMachineRecipe> = register("distillation_tower")

    @JvmStatic
    private fun register(
        name: String,
        machineCondition: (World, BlockPos) -> Boolean = RagiumMachineConditions.ELECTRIC,
    ): HTMachineType<HTMachineRecipe> = HTMachineType.register(object : HTMachineType<HTMachineRecipe> {
        override val id: Identifier = Ragium.id(name)
        override val recipeType: RecipeType<HTMachineRecipe> = RagiumRecipeTypes.MACHINE
        override val machineCondition: (World, BlockPos) -> Boolean = machineCondition
    })

    @JvmStatic
    fun init() {
        buildList {
            // single
            addAll(Single.entries)
            // custom
            add(BURNING_BOX)
            // multi
            add(BLAST_FURNACE)
            add(DISTILLATION_TOWER)
        }.forEach(HTMachineType.Companion::register)
    }

    //    Single    //

    enum class Single(override val machineCondition: (World, BlockPos) -> Boolean = RagiumMachineConditions.ELECTRIC) :
        HTMachineType<HTMachineRecipe> {
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
        ROCK_GENERATOR(RagiumMachineConditions.ROCK_GENERATOR),
        ;

        override val id: Identifier = Ragium.id(name.lowercase())
        override val recipeType: RecipeType<HTMachineRecipe> = RagiumRecipeTypes.MACHINE
    }
}
