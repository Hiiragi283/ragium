package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.entity.HTEnergyStorageHolder
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleEnergyStorage
import kotlin.math.max

abstract class HTSingleMachineBlockEntity(private val machineType: HTMachineType.Single, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(machineType, pos, state),
    HTEnergyStorageHolder {
    private var energyStorage = SimpleEnergyStorage(Ragium.RECIPE_COST * 16, 128, 0)

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.putLong("energy", energyStorage.amount)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        energyStorage.amount = nbt.getLong("energy")
    }

    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.ELECTRIC

    override fun onProcessed(world: World, pos: BlockPos, recipe: HTMachineRecipe) {
        // extract energy
        val currentAmount: Long = energyStorage.amount
        val extracted: Long = currentAmount - Ragium.RECIPE_COST
        energyStorage.amount = max(0, extracted)
        // play sound
        /*world.playSound(
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            machineType.processedSound,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f,
            false,
        )*/
    }

    override fun getDisplayName(): Text = machineType.text

    //    HTEnergyStorageHolder    //

    override fun getEnergyStorage(direction: Direction?): EnergyStorage? = energyStorage
}
