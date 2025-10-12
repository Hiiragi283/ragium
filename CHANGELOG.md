# Changelog

## v0.9.3b

### Common

- Item: Fix crushed when checking harvesting level with Drill on client side

- Menu: Fix duplicated item when moving with shift-click

### Integration

- All: moved from main module to integration module

## v0.9.2b

### API

- Core: Replaced reference for `DataResult` into `Result`
- Core: Split extension functions to each related packages

- Addon: Redesigned structure of  `RagiumAddon`

- Collection: Replaced `HTTable` and `HTMultiMap` with `ImmutableTable` and `ImmutableMultiMap`

- Data: Added new DataMapType `mobHeadType` to bind mobs and its head item

- Material: Removed `StringRepresentable` from `HTMaterialType` and `HTVariantKey`
- Material: Added `HTRawStorageMaterialVariant` to handle raw blocks

- Menu: Changed `HTContainerMenu` design back to the previous one

- Storage: Changed to a more common design
- Storage: Added `HTStorageAction` to clarify whether the current interference is a simulation.
- Storage: Added `HTStorageStack` to unify `XXStack`s
  - `ItemStack` -> `HTItemStorageStack`
  - `FluidStack` -> `HTFluidStorageStack`
  - `ChemicalStack` -> `HTChemicalStorageStack` (if Mekanism is loaded
- Storage: Redesign `HTMultiCapability`

### Common

- Enchantment: Added Strike
  - When mobs killed, their head is dropped

- Block: Fix blocks with block entity not copying custom name from/to item

- Machine
  - Generator: Added model and recipe for Combustion Generator
  - Processor: Added Planting Chamber

- Device: Fix values of exp orbs which are not collected by Exp Collector increased

- Recipe: Added new ore processing for raw blocks
- 

- World Gen: Added new generation pattern at lower heights for Raginite Ore

## v0.9.1b

### API

- Core: Split abstract methods in `RagiumAPI` into `RagiumPlatform`

- Item: Backported new component `HTDamageResistant` and `HTRepairable` from newer minecraft

- Registry: Added `HTItemHolderLike` which combines `ItemLike` and `HTHolderLike`
- Registry: Added `HTDeferredDataComponentRegister`

- Recipe: Redesign of ingredient format
- Recipe: Moved implementations of recipe builders into `hiiragi283.ragium.impl.data.recipe`

- Storage: Added `HTStackSlot` to unify `HTItemSlot` and `HTFluidTank`
- Storage: Split interfaces for capabilities into immutable and mutable
- Storage: Renamed `IFluidHandlerModifiable` into `HTExtendedFluidHandler`

### Common

- Block: Fixed drums cannot hold fluids

- Enchantment: Redesign of enchantment effect
- Enchantment: Added Extra Range

- Fluid: Added Green Fuel

- Machine
  - Generator: Implemented Thermal Generator 
  - Processor: Pulverizer and Crusher can work lower energy if its tank is filled with Lubricant

- Recipe: Nerf Ancient Debris recipe in Simulation Chamber

- World Gen: Reduced Raginite Ore generation by half

### Client

- Screen: Fixed incorrect fluid widget in Melter

### Integration

- EMI
  - Category: Redesign of recipe registration and category layout
- Mekanism
  - Chemical: Rename Raginite into Ragium
  - Chemical: Added Deep Essence
  - Item: Added Enriched Deep
- Replication
  - Added new matter type Azure and Deep

## v0.9.0b

- Due to a complete overhaul, this is not compatible with previous versions!

## v0.8.0

- Added many compat recipes for common materials

### Added

- API: `IEnergyStorageModifiable` for mutable `IEnergyStorage`

- Block: Added `Forming Press` and its recipe type
- Block: Added `Arcane Infuser` and its recipe type

- Fluid: Added `Crimson Diesel`

- Game Play: Added recipes for `LED` and `LED Block`s

- Item: `Synthetic Fiber/Leather` for alternative to `String` and `Leather`
- Item: Added `Quartz Dust`
- Item: Added `Circuit Board`
- Item: Added `Elite/Ultimate Circuit`
- Item: Added `Luminous Paste`

### Fixed

- Render: Removed unused texture reference in `Milk Drain`

### Changed

- Block: Added functionality to `Solidifier`

- Game Play: Applied `Silk Touch` to `Azure Steel Tools`
- Game Play: Chanced recipes for custom glasses
- Game Play: Changed behavior when items are dropped
- Game Play: Changed gui layout for `Melter`

- Item: Changed textures and recipes for `Basic/Advanced Circuit`
- Item: Renamed `Stone Board` to `Redstone Board`

- Tag: Added new tag `#c:gems/azure` to `Azure Shard`

### Removed

- Fluid: Removed `Eldritch Goo`
- Fluid: Removed `Chocolate`
- Fluid: Removed `Light Fuel`
- Fluid: Removed `Heavy Fuel`

- Tags: Removed unused fluid tags

## v0.7.0

- Added recipes and usages for Deep Steel!

### Added

- API: `HTIntrinsicEnchantment` to handle static enchantments on specific items

- Block: Resonant Debris
    - Generated in `Deep Dark` biome, or killing `Warden` around `Deepslate`

- Enchantment: Sonic Protection
    - Invulnerable to warden's sonic boom attack

- Game Play: Added information tooltips on items
- Game Play: Added `ragium` command to interact energy network

- Item: Deep Scrap
    - Ingredient for `Deep Steel` Ingot like `Netherite Scrap`
- Item: Deep Steel Armors, Tools, and Weapon
    - Crafted from `Diamond equipments` and `Deep Steel Upgrade` in `Smithing Table`
    - Pickaxe: Always has at least `Fortune` V
    - Sword: Always has at least `Noise Canceling` V
    - Chestplate: Always has at least `Sonic Protection`
- Item: Iron/Diamond/Netherite Forge Hammer
- Item: Plastic Plate for future usage

- Integration: Crushing and Alloying recipe supports common materials
- Integration: Recipe integration for `Oritech`
- Integration: Recipe output now unified if `Almost Unified` loaded

### Fixed

- Render: Incorrect render type on Machine Frames
- Render: Show stored energy in energy network on machine's gui

- Tag: Fixed incorrect tag appending for `Azure Steel armors`

### Changed

- API: `HTCookingRecipeBuilder` generates multiple recipes based on `AbstractFurnaceRecipe`
- API: `HTDefinitionRecipeBuilder` supports `ICondition`
- API: `HTItemOutput` and `HTFluidOutput` supports tag-based entry
- API: Integrated `RagiumXXTags` into `RagiumCommonTags` and `RagiumModTags`
- API: Reconstructed `HTMaterialFalimy` for enhancement

- Game Play: Restored `Raginite Ore` generation
- Game Play: Looting of `Trader Catalog` and `Elder Heart` are now affected by `Looting` enchantment
- Game Play: `Azure Steel equipments `are crafted from `Iron equipment` in `Smithing Table`
- Game Play: Increased effect length of `Health Boost` when `Fever Cherry` eaten

- Item: Renamed `Item Magnet` into `Ragi-Magnet`
- Item: Renamed `Exp Magnet` into `Advanced Ragi-Manget`
- Item: Restored functions of `Ragi-Ticket`, use to roll loots in specific structure

- Render: Changed some textures a little

- Translation: Renamed translation keys around tooltips

### Removed

- Block: Removed `Cauldrons`
- Block: Removed `Charger`
- Block: Removed `Tree Tap`

- Game Play: Removing lava traps in Nether
    -j Use [LavaSweeper](https://www.curseforge.com/minecraft/mc-mods/lavasweeper)instead of this :)

- Item: Removed `Ragi-Ticket (Fake)`. Find out `Raginite Ore` in underground

- Recipes: `HTCauldrondroppingRecipe`

- Integration: Removed EMI information entry. Use tooltip instead of this

## v0.6.0

- initial release
