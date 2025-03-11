package hiiragi283.ragium.common.tile.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.internal.RagiumConfig
import hiiragi283.ragium.common.inventory.HTInfuserMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags

class HTStirlingGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.STIRLING_GENERATOR, pos, state) {
    private val inputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setValidator { variant: HTItemVariant -> variant.toStack().getBurnTime(null) > 0 }
        .setCallback(this::setChanged)
        .build("item_input")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_output")

    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setValidator { variant: HTFluidVariant -> variant.isIn(Tags.Fluids.WATER) }
        .setCallback(this::setChanged)
        .build("fluid_input")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        inputSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)

        inputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        inputSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)

        inputTank.readNbt(nbt, registryOps)
    }

    override fun checkCondition(level: ServerLevel, pos: BlockPos, simulate: Boolean): Result<Unit> {
        val burnTime: Int = inputSlot.stack.getBurnTime(null)
        if (burnTime > 0) {
            return checkEnergyGenerate(level, 640, simulate)
        }
        return Result.failure(HTMachineException.GenerateEnergy())
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        val burnTime: Int = inputSlot.stack.getBurnTime(null)
        if (burnTime <= 0) throw HTMachineException.Custom("Invalid furnace fuel found!")
        val requiredWater: Int = RagiumConfig.getStirlingWater(burnTime)

        if (!inputTank.canExtract(requiredWater)) throw HTMachineException.ShrinkFluid()
        if (!inputSlot.canExtract(1)) throw HTMachineException.ShrinkItem()

        inputTank.extract(requiredWater, false)
        inputSlot.extract(1, false)
        outputSlot.insert(
            RagiumItems
                .getMaterialItem(HTTagPrefix.DUST, CommonMaterials.ASH)
                .toStack(RagiumConfig.getStirlingAsh(burnTime)),
            false,
        )
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTInfuserMenu(containerId, playerInventory, blockPos, inputSlot, outputSlot)

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        super.onUpdateEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 2

    //    Fluid    //

    override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getTanks(): Int = 1
}
