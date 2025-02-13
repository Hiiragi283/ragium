package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.extension.moveNextOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.inventory.HTLootSpawnerContainerMenu
import hiiragi283.ragium.common.item.HTMachineItemHandlerImpl
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class HTLookSpawnerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.LOOT_SPAWNER, pos, state, HTMachineType.LOOT_SPAWNER) {
    private val spawnerInput: HTMachineItemHandlerImpl =
        object : HTMachineItemHandlerImpl(1, this@HTLookSpawnerBlockEntity::setChanged) {
            override fun isItemValid(slot: Int, stack: ItemStack): Boolean = stack.has(RagiumComponentTypes.SPAWNER_CONTENT)
        }

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(listOf(spawnerInput.createSlot(0)))

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override fun process(level: ServerLevel, pos: BlockPos) {
        val entityType: EntityType<*> =
            spawnerInput
                .getStackInSlot(0)
                .get(RagiumComponentTypes.SPAWNER_CONTENT)
                ?.entityType
                ?: throw HTMachineException.Custom(false, "Failed to find spawner data!")
        val virtualEntity: Entity =
            entityType.create(level) ?: throw HTMachineException.Custom(false, "Failed to create internal entity!")
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(entityType.defaultLootTable)
        val lootParams: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, virtualEntity)
            .withParameter(LootContextParams.ORIGIN, blockPos.toVec3())
            .withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().generic())
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, owner)
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, owner)
            .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, owner)
            .create(LootContextParamSets.ENTITY)
        for (stack: ItemStack in lootTable.getRandomItems(lootParams)) {
            moveNextOrDrop(level, pos, stack)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTLootSpawnerContainerMenu =
        HTLootSpawnerContainerMenu(containerId, playerInventory, blockPos, spawnerInput)

    override fun interactWithFluidStorage(player: Player): Boolean = false
}
