package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.common.world.HTDataDriveManager
import hiiragi283.ragium.common.world.dataDriveManager
import net.minecraft.block.BlockState
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTDriveScannerBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.DRIVE_SCANNER, pos, state) {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val manager: HTDataDriveManager = world.dataDriveManager ?: return
        val recipeEntry: RecipeEntry<HTMachineRecipe> = world
            .recipeManager
            .listAllOfType(RagiumRecipeTypes.MACHINE)
            .filter { it.value.requireScan }
            .filter { it.id !in manager }
            .randomOrNull() ?: return
        manager.add(recipeEntry.id)
        Ragium.log { info("Scanned recipe; ${recipeEntry.id}") }
    }
}
